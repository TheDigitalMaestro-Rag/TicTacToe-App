package com.raghu.xoapp

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.collections.listOf

@Composable
fun XOScreen() {
    val board = remember { mutableStateListOf("", "", "", "", "", "", "", "", "") }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf<String?>(null) }
    var isDraw by remember { mutableStateOf(false) }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1A1A2E),
            Color(0xFF16213E),
            Color(0xFF0F3460)
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                AnimatedTitle()

                Spacer(modifier = Modifier.height(20.dp))

                CurrentPlayerCard(currentPlayer = currentPlayer, winner = winner)

                Spacer(modifier = Modifier.height(30.dp))

                GameBoard(
                    board = board,
                    currentPlayer = currentPlayer,
                    winner = winner,
                    onCellClick = { index ->
                        if (board[index].isEmpty() && winner == null) {
                            board[index] = currentPlayer
                            val res = checkWinner(board)
                            if (res != null) {
                                winner = res
                            } else if (board.all { it.isNotEmpty() }) {
                                isDraw = true
                                winner = "Draw"
                            } else {
                                currentPlayer = if (currentPlayer == "X") "O" else "X"
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (winner != null) {
                    WinnerMessage(winner = winner!!)

                    Spacer(modifier = Modifier.height(20.dp))

                    ResetButton {
                        board.indices.forEach { board[it] = "" }
                        currentPlayer = "X"
                        winner = null
                        isDraw = false
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedTitle() {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = repeatable(
            iterations = Integer.MAX_VALUE,
            animation = tween(1000)
        ),
        label = "titleAnimation"
    )

    Text(
        text = "✨ Tic Tac Toe ✨",
        fontSize = 42.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.scale(scale),
        color = Color.White,
        style = MaterialTheme.typography.headlineMedium.copy(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFE94560),
                    Color(0xFFF5A623),
                    Color(0xFF4ECDC4)
                )
            )
        )
    )
}

@Composable
fun CurrentPlayerCard(currentPlayer: String, winner: String?) {
    val playerColor = if (currentPlayer == "X")
        Color(0xFFE94560) else Color(0xFF4ECDC4)

    val playerBrush = Brush.horizontalGradient(
        colors = if (currentPlayer == "X")
            listOf(Color(0xFFE94560), Color(0xFFFF6B6B))
        else
            listOf(Color(0xFF4ECDC4), Color(0xFF44CF6C))
    )

    Card(
        modifier = Modifier
            .width(200.dp)
            .height(80.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = playerBrush,
                    shape = RoundedCornerShape(15.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (winner == null) "Player $currentPlayer's Turn" else "Game Over",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun GameBoard(
    board: List<String>,
    currentPlayer: String,
    winner: String?,
    onCellClick: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        for (row in 0..2) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
            ) {
                for (col in 0..2) {
                    val index = row * 3 + col
                    GameCell(
                        value = board[index],
                        onClick = { onCellClick(index) },
                        isActive = winner == null && board[index].isEmpty()
                    )
                }
            }
        }
    }
}

@Composable
fun GameCell(value: String, onClick: () -> Unit, isActive: Boolean) {
    val cellColor = when {
        value == "X" -> Color(0xFFE94560)
        value == "O" -> Color(0xFF4ECDC4)
        else -> Color(0xFF2C3E50)
    }

    val cellBrush = Brush.radialGradient(
        colors = listOf(
            cellColor,
            cellColor.copy(alpha = 0.7f)
        ),
        radius = 100f,
        center = Offset(50f, 50f)
    )

    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(15.dp),
                ambientColor = if (value.isNotEmpty()) cellColor else Color.Black,
                spotColor = if (value.isNotEmpty()) cellColor else Color.Black
            )
            .clip(RoundedCornerShape(15.dp))
            .background(
                brush = if (value.isNotEmpty()) cellBrush else Brush.verticalGradient(
                    colors = listOf(Color(0xFF34495E), Color(0xFF2C3E50))
                )
            )
            .then(
                if (isActive) Modifier.clickable { onClick() } else Modifier
            )
            .border(
                width = 2.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFFE94560), Color(0xFF4ECDC4))
                ),
                shape = RoundedCornerShape(15.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (value.isNotEmpty()) {
            AnimatedCellContent(value = value, color = Color.White)
        } else if (isActive) {
            // Show subtle hint of next player
            Text(
                text = if (isActive) "" else "",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun AnimatedCellContent(value: String, color: Color) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(300),
        label = "cellAnimation"
    )

    Text(
        text = value,
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = Modifier.scale(scale)
    )
}

@Composable
fun WinnerMessage(winner: String) {
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFFFD700),
            Color(0xFFFFA500),
            Color(0xFFFFD700)
        )
    )

    Card(
        modifier = Modifier
            .width(280.dp)
            .height(60.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = gradientBrush,
                    shape = RoundedCornerShape(30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (winner == "Draw") "🤝 It's a Draw! 🤝" else "🏆 Winner: Player $winner! 🏆",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A2E),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ResetButton(onReset: () -> Unit) {
    Button(
        onClick = onReset,
        modifier = Modifier
            .width(200.dp)
            .height(50.dp),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        elevation = ButtonDefaults.buttonElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFFE94560), Color(0xFFF5A623))
                    ),
                    shape = RoundedCornerShape(25.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🔄 New Game 🔄",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

fun checkWinner(board: List<String>): String? {
    val winningPositions = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // Rows
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // Columns
        listOf(0, 4, 8), listOf(2, 4, 6)  // Diagonals
    )

    for (pos in winningPositions) {
        val (a, b, c) = pos
        if (board[a].isNotEmpty() && board[a] == board[b] && board[a] == board[c]) {
            return board[a]
        }
    }
    return null
}