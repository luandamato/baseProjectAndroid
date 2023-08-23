package com.example.base_project.Utils.Network;

import com.google.gson.JsonParseException;
import com.kniebuhr.baseandroid.data.NiException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {
    private static final String ERROR_UNKNOWN_HOST = "Rede Indisponível";
    private static final String ERROR_TIMEOUT_ERROR = "Tempo Excedido.";
    private static final String ERROR_PARSE_PRODUCTION = "Pode ser necessário atualizar seu aplicativo.";
    private static final String ERROR_PARSE_DEBUG = "JsonParseException";

    private final RxJava2CallAdapterFactory original;

    private RxErrorHandlingCallAdapterFactory() {
        original = RxJava2CallAdapterFactory.create();
    }

    public static CallAdapter.Factory create() {
        return new RxErrorHandlingCallAdapterFactory();
    }

    @Override
    public CallAdapter<Type, Object> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper(retrofit, original.get(returnType, annotations, retrofit));
    }

    private static class RxCallAdapterWrapper implements CallAdapter<Type, Object> {
        private final Retrofit retrofit;
        private final CallAdapter<?, ?> wrapped;

        RxCallAdapterWrapper(Retrofit retrofit, CallAdapter<?, ?> wrapped) {
            this.retrofit = retrofit;
            this.wrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object adapt(Call call) {
            return ((Single) wrapped.adapt(call)).onErrorResumeNext(new Function<Throwable, SingleSource>() {
                @Override
                public SingleSource apply(Throwable throwable) throws Exception {
                    return Single.error(parseException(throwable));
                }
            });
        }

        private Throwable parseException(Throwable throwable) {
            if (throwable instanceof HttpException) {
                // We had non-2XX http error
                HttpException httpException = (HttpException) throwable;
                Response response = httpException.response();
                return new NiException(response.raw().code(), response.raw().request().url().toString(), false, null, 1);
            } else if (throwable instanceof IOException) {
                // A network or conversion error happened
                if (throwable instanceof UnknownHostException) {
                    // Network error
                    return new NiException(404, ERROR_UNKNOWN_HOST, true, null, 1);
                } else if (throwable instanceof SocketTimeoutException) {
                    // Timeout error
                    return new NiException(-1, ERROR_TIMEOUT_ERROR, true, null, 1);
                } else if (throwable instanceof ConnectException) {
                    // Network error
                    return new NiException(404, ERROR_UNKNOWN_HOST, true, null, 1);
                }

            }  else if (throwable instanceof NiException) {
                return throwable;
            }

            // We don't know what happened. We need to simply convert to an unknown error
            return new NiException(-1, throwable.getMessage(), true, null, 1);
        }
    }
}
