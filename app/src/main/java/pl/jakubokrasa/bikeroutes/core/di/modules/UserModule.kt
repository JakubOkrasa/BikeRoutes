package pl.jakubokrasa.bikeroutes.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.core.user.data.remote.UserRepositoryImpl
import pl.jakubokrasa.bikeroutes.core.user.domain.UserAuth
import pl.jakubokrasa.bikeroutes.core.user.auth.UserAuthImpl
import pl.jakubokrasa.bikeroutes.core.user.domain.*
import pl.jakubokrasa.bikeroutes.core.user.presentation.UserViewModel

val userModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseDatabase.getInstance() }
    single {  get<FirebaseDatabase>().getReference("users") }
    factory<UserRepository> { UserRepositoryImpl(get()) }
    factory<UserAuth> { UserAuthImpl(get()) }

    single { FirebaseFirestore.getInstance() }

    factory { CreateUserUseCase(get(), get()) }
    factory { DeleteCurrentUserUseCase(get(), get()) }
    factory { ResetPasswordUseCase(get()) }
    factory { SignInUseCase(get()) }
    factory { LogOutUseCase(get()) }



    viewModel { UserViewModel(get(), get(), get(), get(), get(), get(), get()) }
}