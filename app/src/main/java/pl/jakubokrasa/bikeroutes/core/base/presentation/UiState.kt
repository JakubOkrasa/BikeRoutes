package pl.jakubokrasa.bikeroutes.core.base.presentation

sealed class UiState {
    object Idle : UiState()
    object Pending : UiState()
}
