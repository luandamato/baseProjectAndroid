package com.example.base_project.Utils.Network

import com.example.base_project.Utils.PreferencesHelper
import java.io.Serializable

class GenericResponse: BaseModel()
class GenericRequest: BaseModel()

abstract class BaseModel: Serializable {
//    val token = PreferencesHelper.sessionCookie
//    var codAcao: Int? = null
//    var statusVersao: Int? = null
//    var msgExterna: String? = null
//    var msgInterna: String? = null
//    var msgExternaExibir: String? = null
}