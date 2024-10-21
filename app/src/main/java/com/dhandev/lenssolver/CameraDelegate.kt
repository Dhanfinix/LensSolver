package com.dhandev.lenssolver

import android.net.Uri

interface CameraDelegate {
    fun onCaptured(uri: Uri)
    fun onBackClicked()
}