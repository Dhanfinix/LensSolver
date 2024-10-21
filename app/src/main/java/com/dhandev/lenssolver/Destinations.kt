package com.dhandev.lenssolver

enum class Destinations {
    HOME{
        override val route = "HomeScreen"
        override val routeWithArgs = "HomeScreen?capturedUri={capturedUri}"
        override val argument = "capturedUri"
    },
    CAMERA{
        override val route = "CameraScreen"
    };

    abstract val route : String
    open val routeWithArgs : String = ""
    open val argument : String = ""
}