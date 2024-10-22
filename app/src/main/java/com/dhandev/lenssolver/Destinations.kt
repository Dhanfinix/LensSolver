package com.dhandev.lenssolver

import kotlinx.serialization.Serializable

/** Only use simple data type, the workaround is implementing Gson converter
 * @see <a href=https://developer.android.com/develop/ui/compose/navigation#retrieving-complex-data>Documentation</a>
 * */
@Serializable
data class HomeDestination(
    val capturedUriString : String? = null
)

/** If the destination doesn't need any argument, just use object
 * */
@Serializable
object CameraDestination