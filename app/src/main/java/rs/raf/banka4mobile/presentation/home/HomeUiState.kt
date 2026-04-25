package rs.raf.banka4mobile.presentation.home

interface HomeContract {

    data class UiState(
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    sealed class UiEvent {
        data object ScreenOpened : UiEvent()
        data object OpenVerificationClicked : UiEvent()
        data object OpenExchangeClicked : UiEvent()
        data object OpenProfileClicked : UiEvent()
    }

    sealed class SideEffect {
        data object NavigateToVerification : SideEffect()
        data object NavigateToExchange : SideEffect()
        data object NavigateToProfile : SideEffect()
    }

}