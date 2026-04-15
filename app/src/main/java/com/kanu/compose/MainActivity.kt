package com.kanu.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanu.compose.ui.theme.ComposeTheme

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Calculator Display
        Text(
            text = displayText,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 32.dp),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 2
        )

        val onButtonClick: (String) -> Unit = { label ->
            when (label) {
                "C" -> displayText = "0"
                "⌫" -> {
                    displayText = if (displayText.length > 1) {
                        displayText.dropLast(1)
                    } else {
                        "0"
                    }
                }
                "=" -> {
                    // Calculation logic will go here
                }
                else -> {
                    displayText = if (displayText == "0") label else displayText + label
                }
            }
        }

        // Keypad Grid organized by Columns for perfect width alignment
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    column.forEach { label ->
                        if (label == "=") {
                            Button(
                                onClick = { onButtonClick(label) },
                                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            ) {
                                Text(text = label, fontSize = 24.sp, color = Color.White)
                            }
                        } else {
                            Button(
                                onClick = { onButtonClick(label) },
                                modifier = Modifier.fillMaxWidth().height(80.dp),
                                shape = CircleShape
                            ) {
                                Text(text = label, fontSize = 24.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    ComposeTheme {
        Calculator()
    }
}

