package com.example.base_project.Activities.NewProduct.Model

import com.example.base_project.Utils.Network.BaseModel

class ProductRequest (
    var name: String? = null,
    var description: String? = null,
    var origin: String? = null
) : BaseModel()