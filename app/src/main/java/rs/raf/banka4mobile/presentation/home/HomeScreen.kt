package rs.raf.banka4mobile.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import rs.raf.banka4mobile.presentation.home.HomeContract.SideEffect
import rs.raf.banka4mobile.presentation.home.HomeContract.UiEvent

private val GradientStart = Color(0xFF0E1E5B)
private val GradientEnd = Color(0xFF270071)
private val AccentBlue = Color(0xFF2E5BDB)
private val SoftPurple = Color(0xFF5B3CC4)

@Composable
fun HomeScreen(
    onOpenVerification: () -> Unit,
    onOpenExchange: () -> Unit,
    onOpenProfile: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.onEvent(UiEvent.ScreenOpened)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { sideEffect ->
            when (sideEffect) {
                is SideEffect.NavigateToVerification -> onOpenVerification()
                is SideEffect.NavigateToExchange -> onOpenExchange()
                is SideEffect.NavigateToProfile -> onOpenProfile()
            }
        }
    }

    HomeScreenContent(
        state = state,
        onOpenVerification = { viewModel.onEvent(UiEvent.OpenVerificationClicked) },
        onOpenExchange = { viewModel.onEvent(UiEvent.OpenExchangeClicked) },
        onOpenProfile = { viewModel.onEvent(UiEvent.OpenProfileClicked) }
    )
}

@Composable
private fun HomeScreenContent(
    state: HomeContract.UiState,
    onOpenVerification: () -> Unit,
    onOpenExchange: () -> Unit,
    onOpenProfile: () -> Unit
) {
    val verificationInteraction = remember { MutableInteractionSource() }
    val exchangeInteraction = remember { MutableInteractionSource() }
    val profileInteraction = remember { MutableInteractionSource() }

    val verificationPressed = verificationInteraction.collectIsPressedAsState().value
    val exchangePressed = exchangeInteraction.collectIsPressedAsState().value
    val profilePressed = profileInteraction.collectIsPressedAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF5F7FC),
                        Color(0xFFEFF3FB)
                    )
                )
            )
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Banka 4 Mobile",
            style = MaterialTheme.typography.headlineMedium,
            color = GradientEnd,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Izaberite funkcionalnost koju želite da otvorite.",
            color = Color(0xFF5A6475),
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(40.dp))

        when {
            state.isLoading -> {
                CircularProgressIndicator(color = AccentBlue)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Učitavanje...",
                    color = Color(0xFF5A5A5A)
                )
            }

            state.errorMessage != null -> {
                Text(
                    text = "Greška pri učitavanju: ${state.errorMessage}",
                    color = Color(0xFFB3261E)
                )
            }

            else -> {
                Button(
                    onClick = onOpenExchange,
                    interactionSource = exchangeInteraction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (exchangePressed) Color(0xFF224CC2) else AccentBlue,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Text(
                        text = "Menjačnica",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onOpenVerification,
                    interactionSource = verificationInteraction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (verificationPressed) Color(0xFF162A7A) else GradientEnd,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Text(
                        text = "Prikaži kod",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onOpenProfile,
                    interactionSource = profileInteraction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (profilePressed) Color(0xFF4B2BB1) else SoftPurple,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Text(
                        text = "Profil",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}