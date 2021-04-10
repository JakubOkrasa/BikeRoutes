package pl.jakubokrasa.bikeroutes.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.core.user.data.remote.UserRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.map.data.RouteRepositoryImpl

val userModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseDatabase.getInstance() }
    single {  get<FirebaseDatabase>().getReference("users") }
    single { UserRepositoryImpl(get()) }
}