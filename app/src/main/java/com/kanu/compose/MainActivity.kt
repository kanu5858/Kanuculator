package com.kanu.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanu.compose.ui.theme.ComposeTheme
import net.objecthunter.exp4j.ExpressionBuilder
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Calculator(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Calculator(modifier: Modifier = Modifier) {
    var displayText by remember { mutableStateOf("0") }
    var expression by remember { mutableStateOf("") }

    // Colors based on the black calculator image
    val backgroundColor = Color(0xFF000000)
    val displayTextColor = Color.White
    val secondaryTextColor = Color(0xFF7C7C7C)
    val numberButtonColor = Color(0xFF17171C)
    val operatorButtonColor = Color(0xFF005DB2)
    val specialButtonColor = Color(0xFF2D2D2D)

    Surface(
        modifier = modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Display Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    // Secondary display for expression history
                    Text(
                        text = expression,
                        style = MaterialTheme.typography.headlineSmall,
                        color = secondaryTextColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Main display
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (expression.endsWith("=")) {
                            Text(
                                text = "= ",
                                style = MaterialTheme.typography.displayLarge.copy(
                                    fontSize = if (displayText.length > 10) 32.sp else 48.sp,
                                    fontWeight = FontWeight.Light
                                ),
                                color = displayTextColor
                            )
                        }
                        Text(
                            text = displayText,
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = when {
                                    displayText.length > 10 -> 40.sp
                                    displayText.length > 7 -> 54.sp
                                    else -> 72.sp
                                },
                                fontWeight = FontWeight.Medium
                            ),
                            color = displayTextColor,
                            maxLines = 1,
                            overflow = TextOverflow.Clip
                        )
                    }
                }
            }

            val onButtonClick: (String) -> Unit = { label ->
                when (label) {
                    "C" -> {
                        displayText = "0"
                        expression = ""
                    }
                    "⌫" -> {
                        if (displayText != "0") {
                            displayText = if (displayText.length > 1) displayText.dropLast(1) else "0"
                        }
                    }
                    "=" -> {
                        try {
                            val fullExpression = if (expression.isNotEmpty() && !expression.last().isDigit() && expression.last() != '.') {
                                if (displayText == "0") expression.dropLast(1).trim() else expression + displayText
                            } else {
                                displayText
                            }
                            val result = ExpressionBuilder(fullExpression.replace("×", "*").replace("÷", "/")).build().evaluate()
                            expression = "$fullExpression ="
                            displayText = formatResult(result)
                        } catch (e: Exception) {
                            displayText = "Error"
                        }
                    }
                    "+", "-", "*", "/", "%" -> {
                        val op = when(label) {
                            "*" -> "×"
                            "/" -> "÷"
                            else -> label
                        }
                        expression = "$displayText $op"
                        displayText = "0"
                    }
                    else -> {
                        if (expression.endsWith("=")) {
                            expression = ""
                            displayText = label
                        } else {
                            displayText = if (displayText == "0") label else displayText + label
                        }
                    }
                }
            }

            // Keypad Grid (Keeping your button order)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val columns = listOf(
                    listOf("C", "7", "4", "1", "%"),
                    listOf("/", "8", "5", "2", "0"),
                    listOf("*", "9", "6", "3", "."),
                    listOf("⌫", "-", "+", "=")
                )

                columns.forEach { column ->
                    Column(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        column.forEach { label ->
                            val isOperator = label in listOf("/", "*", "-", "+", "=")
                            val isSpecial = label in listOf("C", "⌫", "%")
                            
                            val containerColor = when {
                                isOperator -> operatorButtonColor
                                isSpecial -> specialButtonColor
                                else -> numberButtonColor
                            }

                            Button(
                                onClick = { onButtonClick(label) },
                                modifier = if (label == "=") Modifier.fillMaxWidth().weight(1f) 
                                           else Modifier.fillMaxWidth().height(80.dp),
                                shape = MaterialTheme.shapes.large,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = containerColor,
                                    contentColor = Color.White
                                ),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = when(label) {
                                        "/" -> "÷"
                                        "*" -> "×"
                                        else -> label
                                    },
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatResult(result: Double): String {
    return if (result == result.toLong().toDouble()) {
        result.toLong().toString()
    } else {
        DecimalFormat("#.#######").format(result)
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    ComposeTheme {
        Calculator()
    }
}
