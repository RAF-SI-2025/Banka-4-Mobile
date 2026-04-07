package rs.raf.banka4mobile.presentation.home

interface HomeContract {

    data class UiState(
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val accounts: List<AccountItem> = emptyList(),
        val selectedAccountIndex: Int = 0
    ) {
        val selectedAccount: AccountItem?
            get() = accounts.getOrNull(selectedAccountIndex)
    }


    data class AccountItem(
        val id: String,
        val accountType: String,
        val accountNumber: String,
        val balance: Double,
        val currency: String
    )

    enum class QuickAction {
        TRANSFER,
        EXCHANGE,
        CARDS
    }

    sealed class UiEvent {
        data object ScreenOpened : UiEvent()
        data object PreviousAccountClicked : UiEvent()
        data object NextAccountClicked : UiEvent()
        data class QuickActionClicked(val action: QuickAction) : UiEvent()
        data object OpenVerificationClicked : UiEvent()
    }

    sealed class SideEffect {
        data object NavigateToVerification : SideEffect()
        data class ShowToast(val message: String) : SideEffect()
    }

}