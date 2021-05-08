package pl.jakubokrasa.bikeroutes.core.base.platform

sealed class UiState {
    object Idle : UiState()
    object Pending : UiState()
}
