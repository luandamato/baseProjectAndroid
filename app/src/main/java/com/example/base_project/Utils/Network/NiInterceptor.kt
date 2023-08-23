package com.kniebuhr.baseandroid.data

import com.example.base_project.Utils.Extensions.toObjectClass
import com.example.base_project.Utils.Network.BaseModel
import com.example.base_project.Utils.PreferencesHelper
import com.google.gson.Gson
import com.google.gson.JsonParseException
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody

private const val REQUEST_HEADER_COOKIE = "Cookie"
private const val REQUEST_HEADER_APPLICATION = "Application"
private const val UNAUTHORIZED_CODE = 401
private const val MEDIA_TYPE = "application/json; charset=utf-8"

class NiInterceptor: Interceptor {

    companion object{
        const val SEGUE_FLUXO_NORMAL = 1
        const val SUGERIR_ATUALIZACAO = 2
        const val ATUALIZACAO_OBRIGATORIA = 3
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        return createNewResponseWithBody(response)
    }

    private fun createNewResponseWithBody(response: Response): Response {
        val responseBody = response.body()?.string()
        checkResponse(responseBody)
        val newResponseBody = if (responseBody != null) ResponseBody.create(MediaType.parse(MEDIA_TYPE), responseBody) else null
        return response.newBuilder().body(newResponseBody).build()
    }

    private fun checkResponse(responseBody: String?) {
        try {
            val model = responseBody.toObjectClass(BaseModel::class.java)
//            val cookie = model.token
//            if (!cookie.isNullOrBlank() && PreferencesHelper.sessionCookie != cookie) {
//                PreferencesHelper.sessionCookie = cookie
//            }

            checkVersionApp(model, responseBody)

//            if (model.codAcao != 1) {
//                throw NiException(model.codAcao, if (model.msgExternaExibir == "1") model.msgExterna else "Erro desconhecido", model.msgExternaExibir == "1", responseBody)
//            }



        } catch (e: JsonParseException) {

        }
    }

    private fun checkVersionApp(model: BaseModel, responseBody: String?) {
//        when(model.statusVersao){
//            SEGUE_FLUXO_NORMAL -> return
//            SUGERIR_ATUALIZACAO ->  throw NiException(model.codAcao, model.msgExterna , model.msgExternaExibir == "1", responseBody, model.statusVersao!!)
//            ATUALIZACAO_OBRIGATORIA ->  throw NiException(model.codAcao, model.msgExterna , model.msgExternaExibir == "1", responseBody, model.statusVersao!!)
//        }
    }
}