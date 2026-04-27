package com.raghu.xoapp

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.collections.listOf

@Composable
fun XOScreen(){
    val board = remember { mutableStateListOf("","","","","","","","","") }
    val currentPlayer = remember { mutableStateOf("X") }
    val winner = remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Tic Tac Toe Game",
            fontSize = 50.sp,
            color = Color.Magenta)
        Spacer(modifier = Modifier.height(100.dp))
        for (row in 0..2){
            Row{
                for(col in 0..2){
                    val index = row*3 + col
                    Box(
                        modifier = Modifier.size(100.dp)
                            .border(1.dp,Color.Black)
                            .clickable{
                                if (board[index].isEmpty() && winner.value == null){
                                    board[index] = currentPlayer.value
                                    val res = checkWinner(board)
                                    if (res != null){
                                        winner.value = res
                                    }else {
                                        currentPlayer.value =
                                            if (currentPlayer.value == "X") "0" else "X"
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Text(text = board[index], fontSize = 32.sp)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if(winner.value != null){
            Text(text = "Winner is ${winner.value}",
                fontSize = 24.sp,
                color = Color.Green)
        }
    }
}

fun checkWinner(board:List<String>):String?{
    val winningPositions = listOf(listOf(0,1,2),listOf(3,4,5), listOf(6,7,8),
                                listOf(6,7,8),listOf(1,4,7),listOf(2,5,8),
                                listOf(0,4,8),listOf(2,4,6)
    )

    for(pos in winningPositions){
        val (a,b,c) = pos
        if(board[a].isNotEmpty() && board[a]==board[b] && board[a]==board[c]){
            return board[a]
        }
    }
    return null
}