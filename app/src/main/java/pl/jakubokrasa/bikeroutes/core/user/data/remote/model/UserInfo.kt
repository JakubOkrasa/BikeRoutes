package pl.jakubokrasa.bikeroutes.core.user.data.remote.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserInfo(
    val email: String? = ""
) {

}
