package rs.raf.banka4mobile.presentation.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.util.Locale

private val GradientColor = Color(0xFF005EAD)

@Composable
fun HomeScreen(
    onOpenVerification: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onEvent(HomeContract.UiEvent.ScreenOpened)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { sideEffect: HomeContract.SideEffect ->
            when (sideEffect) {
                HomeContract.SideEffect.NavigateToVerification -> onOpenVerification()
                is HomeContract.SideEffect.ShowToast -> {
                    Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    HomeScreenContent(
        state = state,
        onPrevious = { viewModel.onEvent(HomeContract.UiEvent.PreviousAccountClicked) },
        onNext = { viewModel.onEvent(HomeContract.UiEvent.NextAccountClicked) },
        onTransferClick = {
            viewModel.onEvent(
                HomeContract.UiEvent.QuickActionClicked(HomeContract.QuickAction.TRANSFER)
            )
        },
        onExchangeClick = {
            viewModel.onEvent(
                HomeContract.UiEvent.QuickActionClicked(HomeContract.QuickAction.EXCHANGE)
            )
        },
        onCardsClick = {
            viewModel.onEvent(
                HomeContract.UiEvent.QuickActionClicked(HomeContract.QuickAction.CARDS)
            )
        },
        onOpenVerification = { viewModel.onEvent(HomeContract.UiEvent.OpenVerificationClicked) },
    )
}

@Composable
private fun HomeScreenContent(
    state: HomeContract.UiState,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onTransferClick: () -> Unit,
    onExchangeClick: () -> Unit,
    onCardsClick: () -> Unit,
    onOpenVerification: () -> Unit,
) {
    val selectedAccount = state.selectedAccount

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        when {
            state.isLoading -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = GradientColor)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Ucitavanje...", color = Color(0xFF5A5A5A))
                }
            }

            state.errorMessage != null -> {
                Text(
                    text = "Greska pri ucitavanju: ${state.errorMessage}",
                    color = Color(0xFFB3261E),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            selectedAccount != null -> {
                AccountSwitcher(
                    selectedAccount = selectedAccount,
                    onPrevious = onPrevious,
                    onNext = onNext,
                    modifier = Modifier.align(Alignment.TopCenter)
                )

                BalanceCircle(
                    account = selectedAccount,
                    modifier = Modifier.align(Alignment.Center)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(bottom = 22.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top
                ) {
                    HomeActionItem("Transfer", Icons.Default.SwapHoriz, onTransferClick)
                    HomeActionItem("Menjacnica", Icons.Default.CurrencyExchange, onExchangeClick)
                    HomeActionItem("Kartice", Icons.Default.CreditCard, onCardsClick)
                    HomeActionItem("Verifikacija", Icons.Default.VerifiedUser, onOpenVerification)
                }
            }
        }
    }
}

@Composable
private fun AccountSwitcher(
    selectedAccount: HomeContract.AccountItem,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 28.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onPrevious) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Prethodni racun",
                    tint = GradientColor
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = selectedAccount.accountType,
                    style = MaterialTheme.typography.titleLarge,
                    color = GradientColor,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = selectedAccount.accountNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF5A5A5A)
                )
            }

            IconButton(onClick = onNext) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Sledeci racun",
                    tint = GradientColor
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp),
            thickness = 1.dp,
            color = GradientColor.copy(alpha = 0.20f)
        )
    }
}

@Composable
private fun BalanceCircle(
    account: HomeContract.AccountItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(308.dp)
            .drawBehind {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = size.minDimension * 0.5f
                drawCircle(
                    brush = Brush.radialGradient(
                        colorStops = arrayOf(
                            0.30f to GradientColor.copy(alpha = 0.26f),
                            1.00f to Color.Transparent
                        ),
                        center = center,
                        radius = radius
                    )
                )

            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(220.dp)
                .background(color = Color.White, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "stanje",
                    color = Color(0xFF7A7A7A),
                    fontWeight = FontWeight.Medium,
                    fontSize = 19.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = String.format(Locale.US, "%.2f", account.balance),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = account.currency,
                    color = Color(0xFF7A7A7A),
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

@Composable
private fun HomeActionItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE9F2FF),
                contentColor = GradientColor
            )
        ) {
            Icon(imageVector = icon, contentDescription = label)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color(0xFF3E3E3E),
            style = MaterialTheme.typography.bodySmall,

            )
    }
}