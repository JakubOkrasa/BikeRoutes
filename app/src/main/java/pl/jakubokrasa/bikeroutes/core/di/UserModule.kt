package pl.jakubokrasa.bikeroutes.core.di

import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val userModule = module {
    single { FirebaseAuth.getInstance() }
}