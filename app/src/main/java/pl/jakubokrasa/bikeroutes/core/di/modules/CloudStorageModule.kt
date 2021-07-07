package pl.jakubokrasa.bikeroutes.core.di.modules

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.koin.dsl.module

val cloudStorageModule = module {
    single { Firebase.storage }
    single { get<FirebaseStorage>().reference }

}