package com.example.base_project.Activities.Login.Model

import com.example.base_project.Utils.Network.BaseModel

class SignInRequest (
    var username: String? = null,
    var password: String? = null
): BaseModel()