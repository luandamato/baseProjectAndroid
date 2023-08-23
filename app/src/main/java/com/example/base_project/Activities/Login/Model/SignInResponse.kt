package com.example.base_project.Activities.Login.Model

import com.example.base_project.Utils.Network.BaseModel

class SignInResponse(
    var id: Int? = null,
    var username: String? = null,
    var email: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var gender: String? = null,
    var image: String? = null,
):BaseModel()