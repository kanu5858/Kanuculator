package com.kanu.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
    var isScientificMode by remember { mutableStateOf(false) }

    val backgroundColor = Color(0xFF000000)
    val displayTextColor = Color.White
    val secondaryTextColor = Color(0xFF7C7C7C)
    val numberButtonColor = Color(0xFF17171C)
    val operatorButtonColor = Color(0xFF005DB2)
    val specialButtonColor = Color(0xFF2D2D2D)
    val scientificButtonColor = Color(0xFF212121)

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
                    .padding(vertical = 24.dp)
            ) {
                // Radical/Scientific Toggle Button
                IconButton(
                    onClick = { isScientificMode = !isScientificMode },
                    modifier = Modifier.align(Alignment.TopEnd),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (isScientificMode) operatorButtonColor else Color.White
                    )
                ) {
                    Text(text = "√", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }

                Column(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    horizontalAlignment = Alignment.End
                ) {
                    // Top row: History / Last calculation
                    Text(
                        text = expression,
                        style = MaterialTheme.typography.headlineSmall,
                        color = secondaryTextColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Bottom row: Active Input
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
                                    displayText.length > 15 -> 32.sp
                                    displayText.length > 10 -> 42.sp
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
                            displayText = if (displayText.length > 1) {
                                if (displayText.endsWith(" ")) displayText.dropLast(3) else displayText.dropLast(1)
                            } else "0"
                            if (displayText.isEmpty()) displayText = "0"
                        }
                    }
                    "=" -> {
                        try {
                            var cleanExpr = displayText
                                .replace("×", "*")
                                .replace("÷", "/")
                                .replace("√", "sqrt")
                                .replace("π", "pi")
                                .replace("log", "log10")
                                .replace("ln", "log")
                            
                            // Auto-close brackets for safety
                            val openCount = cleanExpr.count { it == '(' }
                            val closeCount = cleanExpr.count { it == ')' }
                            if (openCount > closeCount) {
                                cleanExpr += ")".repeat(openCount - closeCount)
                            }

                            val result = ExpressionBuilder(cleanExpr).build().evaluate()
                            expression = "$displayText ="
                            displayText = formatResult(result)
                        } catch (e: Exception) {
                            displayText = "Error"
                        }
                    }
                    "+", "-", "*", "/", "%", "^" -> {
                        val op = when(label) {
                            "*" -> "×"
                            "/" -> "÷"
                            else -> label
                        }
                        if (expression.endsWith("=")) expression = ""
                        if (displayText == "0") displayText = "0 $op " else displayText += " $op "
                    }
                    "sin", "cos", "tan", "log", "ln", "√" -> {
                        val func = when(label) {
                            "√" -> "√("
                            else -> "$label("
                        }
                        if (expression.endsWith("=")) {
                            expression = ""
                            displayText = func
                        } else {
                            if (displayText == "0") displayText = func else displayText += func
                        }
                    }
                    "(", ")" -> {
                        if (expression.endsWith("=")) { expression = ""; displayText = label }
                        else if (displayText == "0") displayText = label
                        else displayText += label
                    }
                    "π", "e" -> {
                        if (expression.endsWith("=")) { expression = ""; displayText = label }
                        else if (displayText == "0") displayText = label
                        else displayText += label
                    }
                    else -> { // Numbers and dot
                        if (expression.endsWith("=")) {
                            expression = ""
                            displayText = label
                        } else {
                            displayText = if (displayText == "0") label else displayText + label
                        }
                    }
                }
            }

            // Scientific Panel
            if (isScientificMode) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val sciButtons = listOf("sin", "cos", "tan", "log", "ln", "(", ")", "√", "^", "π", "e")
                    sciButtons.forEach { label ->
                        Button(
                            onClick = { onButtonClick(label) },
                            modifier = Modifier.height(48.dp),
                            shape = MaterialTheme.shapes.medium,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = scientificButtonColor,
                                contentColor = Color.LightGray
                            )
                        ) {
                            Text(text = label, fontSize = 16.sp)
                        }
                    }
                }
            }

            // Keypad Grid
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
                            val isClear = label == "C"
                            
                            val containerColor = when {
                                isOperator -> operatorButtonColor
                                isSpecial -> specialButtonColor
                                else -> numberButtonColor
                            }

                            val contentColor = if (isClear) Color.Red else Color.White

                            Button(
                                onClick = { onButtonClick(label) },
                                modifier = if (label == "=") Modifier.fillMaxWidth().weight(1f) 
                                           else Modifier.fillMaxWidth().height(80.dp),
                                shape = MaterialTheme.shapes.large,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = containerColor,
                                    contentColor = contentColor
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
                                    fontWeight = if (isClear) FontWeight.Bold else FontWeight.Medium
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
