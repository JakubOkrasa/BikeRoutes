package pl.jakubokrasa.bikeroutes.features.map.domain.usecase

//class PutRouteSaveDataUseCase(private val routeRepository: LocalRepository, private val preferenceHelper: PreferenceHelper): UseCase<Unit, DataSaveRoute>() {
//    override suspend fun action(params: DataSaveRoute) {
//        val id = routeRepository.getCurrentRouteId()
//        routeRepository.updateRouteDescription(id, params.description)
//        routeRepository.updateRouteDistance(id, params.distance)
//        routeRepository.updateRouteName(id, params.name) // causes error
//        preparePrefsForNextRoute()
//    }
//
//    private fun preparePrefsForNextRoute() {
//        with(preferenceHelper.preferences) {
//            edit {
//                remove(PREF_KEY_LAST_LAT)
//                remove(PREF_KEY_LAST_LNG)
//                remove(PREF_KEY_DISTANCE_SUM)
//            }
//        }
//    }
//}

