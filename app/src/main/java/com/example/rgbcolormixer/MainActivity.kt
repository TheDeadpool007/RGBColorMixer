package com.example.rgbcolormixer

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rgbcolormixer.ui.theme.RGBColorMixerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RGBColorMixerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RGBColorMixerUI()
                }
            }
        }
    }
}

@Composable
fun RGBColorMixerUI() {
    val context = LocalContext.current

    var redEnabled by remember { mutableStateOf(true) }
    var greenEnabled by remember { mutableStateOf(true) }
    var blueEnabled by remember { mutableStateOf(true) }

    var redValue by remember { mutableStateOf(1f) }
    var greenValue by remember { mutableStateOf(1f) }
    var blueValue by remember { mutableStateOf(1f) }

    var redBackup by remember { mutableStateOf(1f) }
    var greenBackup by remember { mutableStateOf(1f) }
    var blueBackup by remember { mutableStateOf(1f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        RGBControl(
            label = "Red",
            enabled = redEnabled,
            value = redValue,
            onValueChange = { redValue = it },
            onEnableToggle = {
                redEnabled = it
                if (!it) {
                    redBackup = redValue
                    redValue = 0f
                } else {
                    redValue = redBackup
                }
            },
            showError = { msg -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
        )
        RGBControl(
            label = "Green",
            enabled = greenEnabled,
            value = greenValue,
            onValueChange = { greenValue = it },
            onEnableToggle = {
                greenEnabled = it
                if (!it) {
                    greenBackup = greenValue
                    greenValue = 0f
                } else {
                    greenValue = greenBackup
                }
            },
            showError = { msg -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
        )
        RGBControl(
            label = "Blue",
            enabled = blueEnabled,
            value = blueValue,
            onValueChange = { blueValue = it },
            onEnableToggle = {
                blueEnabled = it
                if (!it) {
                    blueBackup = blueValue
                    blueValue = 0f
                } else {
                    blueValue = blueBackup
                }
            },
            showError = { msg -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            redValue = 1f; greenValue = 1f; blueValue = 1f
            redBackup = 1f; greenBackup = 1f; blueBackup = 1f
            redEnabled = true; greenEnabled = true; blueEnabled = true
        }) {
            Text("Reset")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(redValue, greenValue, blueValue))
        )
    }
}

@Composable
fun RGBControl(
    label: String,
    enabled: Boolean,
    value: Float,
    onValueChange: (Float) -> Unit,
    onEnableToggle: (Boolean) -> Unit,
    showError: (String) -> Unit
) {
    var textValue by remember { mutableStateOf(TextFieldValue(value.toString())) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = enabled, onCheckedChange = onEnableToggle)
            Text(text = label, fontSize = 18.sp, modifier = Modifier.padding(start = 8.dp))
            OutlinedTextField(
                value = textValue,
                onValueChange = {
                    textValue = it
                    val parsed = it.text.toFloatOrNull()
                    if (parsed != null && parsed in 0f..1f) {
                        onValueChange(parsed)
                    } else {
                        showError("Please enter a value between 0.0 and 1.0")
                    }
                },
                enabled = enabled,
                singleLine = true,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .width(100.dp)
            )
        }
        Slider(
            value = value,
            onValueChange = {
                onValueChange(it)
                textValue = TextFieldValue(String.format("%.2f", it))
            },
            valueRange = 0f..1f,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RGBPreview() {
    RGBColorMixerTheme {
        RGBColorMixerUI()
    }
}


