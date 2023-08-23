package com.example.base_project.Utils

import com.example.base_project.BuildConfig

object Constants {

    private const val PACKAGE_NAME = BuildConfig.APPLICATION_ID

    const val AUTHORITY = "$PACKAGE_NAME.fileprovider"

    const val GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id=$PACKAGE_NAME"

    const val EXTRA_PICTURE = "$PACKAGE_NAME.EXTRA_PICTURE"
    const val EXTRA_TEXT = "$PACKAGE_NAME.EXTRA_TEXT"
    const val EXTRA_IMAGE = "$PACKAGE_NAME.EXTRA_IMAGE"
    const val EXTRA_URL = "$PACKAGE_NAME.EXTRA_URL"
    const val EXTRA_VALUE = "$PACKAGE_NAME.EXTRA_VALUE"
    const val EXTRA_MODEL = "$PACKAGE_NAME.EXTRA_MODEL"

}