package com.example.base_project.Activities.Home.Models

import com.example.base_project.Utils.Network.BaseModel
class ProductResponse(
    var createdAt: String? = null,
    var name: String? = null,
    var avatar: String? = null,
    var description: String? = null,
    var id: String? = null,
    var origin: String? = null
):BaseModel()