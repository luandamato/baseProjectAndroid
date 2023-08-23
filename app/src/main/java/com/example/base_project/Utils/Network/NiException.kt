package com.kniebuhr.baseandroid.data

class NiException(val statusCode: Int?, message: String?, val displayMessage: Boolean?, val response: String? = null, val statusVersao: Int = 1): Throwable(message)