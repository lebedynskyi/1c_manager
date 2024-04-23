package com.simple.games.tradeassist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simple.games.tradeassist.core.round
import com.simple.games.tradeassist.core.theme.TradeAssistTheme
import java.lang.StringBuilder

@Composable
fun KeyBoard(
    name: String,
    value: Float?,
    onEntered: (Float) -> Unit = {}
) {

    var action by remember<MutableState<Action?>> {
        mutableStateOf(null)
    }

    var trigger by remember {
        mutableIntStateOf(0)
    }
    System.err.println(trigger)

    val leftBuilder by remember {
        mutableStateOf(StringBuilder(value?.toString() ?: ""))
    }

    var currentBuilder by remember {
        mutableStateOf(leftBuilder)
    }

    val rightBuilder by remember {
        mutableStateOf(StringBuilder())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(((56 * 5) + 24).dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp),
                text = name,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        HorizontalDivider(thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .weight(3f)
                    .background(MaterialTheme.colorScheme.surfaceDim),
            ) {
                Text(
                    text = "$leftBuilder ${action?.toString() ?: ""} $rightBuilder",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            VerticalDivider(thickness = 0.5.dp)
            Box(
                modifier = Modifier
                    .clickable {
                        if (leftBuilder.isNotEmpty()) {
                            action = Action.Plus
                            currentBuilder = rightBuilder
                        }
                    }
                    .size(56.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.errorContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(0.dp)
                )
            }
        }

        HorizontalDivider(thickness = 0.5.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {

            Box(
                modifier = Modifier
                    .clickable {
                        currentBuilder.append("1")
                        trigger += 1
                    }
                    .size(56.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "1",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(0.dp)
                )
            }
            VerticalDivider(thickness = 0.5.dp)
            Box(
                modifier = Modifier
                    .clickable {
                        currentBuilder.append("2")
                        trigger += 1
                    }
                    .size(56.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "2",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(0.dp)
                )
            }
            VerticalDivider(thickness = 0.5.dp)
            Box(
                modifier = Modifier
                    .clickable {
                        currentBuilder.append("3")
                        trigger += 1
                    }
                    .size(56.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "3",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(0.dp)
                )
            }
            VerticalDivider(thickness = 0.5.dp)
            Box(
                modifier = Modifier
                    .clickable {
                        if (leftBuilder.isNotEmpty()) {
                            action = Action.Minus
                            currentBuilder = rightBuilder
                        }
                    }
                    .size(56.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.errorContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "-",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(0.dp)
                )
            }
        }
        HorizontalDivider(thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {

            Box(
                modifier = Modifier
                    .clickable {
                        currentBuilder.append("4")
                        trigger += 1
                    }
                    .size(56.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "4",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(0.dp)
                )
            }
            VerticalDivider(thickness = 0.5.dp)
            Box(
                modifier = Modifier
                    .clickable {
                        currentBuilder.append("5")
                        trigger += 1
                    }
                    .size(56.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "5",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(0.dp)
                )
            }
            VerticalDivider(thickness = 0.5.dp)
            Box(
                modifier = Modifier
                    .clickable {
                        currentBuilder.append("6")
                        trigger += 1
                    }
                    .size(56.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "6",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(0.dp)
                )
            }
            VerticalDivider(thickness = 0.5.dp)
            Box(
                modifier = Modifier
                    .clickable {
                        if (leftBuilder.isNotEmpty()) {
                        action = Action.Multiply
                        currentBuilder = rightBuilder
                        }
                    }
                    .size(56.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.errorContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "*",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(0.dp)
                )
            }
        }
        HorizontalDivider(thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {

            Box(
                modifier = Modifier
                    .clickable {
                        currentBuilder.append("7")
                        trigger += 1
                    }
                    .size(56.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "7",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(0.dp)
                )
            }
            VerticalDivider(thickness = 0.5.dp)
            Box(
                modifier = Modifier
                    .clickable {
                        currentBuilder.append("8")
                        trigger += 1
                    }
                    .size(56.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "8",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(0.dp)
                )
            }
            VerticalDivider(thickness = 0.5.dp)
            Box(
                modifier = Modifier
                    .clickable {
                        currentBuilder.append("9")
                        trigger += 1
                    }
                    .size(56.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "9",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(0.dp)
                )
            }
            VerticalDivider(thickness = 0.5.dp)
            Box(
                modifier = Modifier
                    .clickable {
                        if (leftBuilder.isNotEmpty()) {
                        action = Action.Divide
                        currentBuilder = rightBuilder}
                    }
                    .size(56.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.errorContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "/",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(0.dp)
                )
            }
        }
        HorizontalDivider(thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {

            Box(
                modifier = Modifier
                    .clickable {
                        if (rightBuilder.isNotEmpty()) {
                            rightBuilder.deleteCharAt(rightBuilder.length - 1)
                        } else if (action != null) {
                            action = null
                            currentBuilder = leftBuilder
                        } else if (leftBuilder.isNotEmpty()) {
                            leftBuilder.deleteCharAt(leftBuilder.length - 1)
                        }
                        trigger += 1
                    }
                    .size(56.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "DEL" + "",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(0.dp)
                )
//                Image(painter = rememberVectorPainter(image = Icons.AutoMirrored.Outlined.ArrowBack), contentDescription = null)
            }
            VerticalDivider(thickness = 0.5.dp)
            Box(
                modifier = Modifier
                    .clickable {
                        currentBuilder.append("0")
                        trigger += 1
                    }
                    .size(56.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "0",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(0.dp)
                )
            }
            VerticalDivider(thickness = 0.5.dp)
            Box(
                modifier = Modifier
                    .clickable {
                        currentBuilder.append(".")
                        trigger +=1
                    }
                    .size(56.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ".",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(0.dp)
                )
            }
            VerticalDivider(thickness = 0.5.dp)
            Box(
                modifier = Modifier
                    .clickable {
                        val f1 = leftBuilder.toString().toFloatOrNull() ?: return@clickable
                        val f2 = rightBuilder.toString().toFloatOrNull()
                        val act = action
                        if (f2 != null && act != null) {
                            onEntered(act.operate(f1, f2).round(2))
                        } else {
                            onEntered(f1)
                        }
                    }
                    .size(56.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.errorContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "=",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(0.dp)
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewCompose() {
    TradeAssistTheme {
        KeyBoard("Поверка", value = 130.04F)
    }
}

private sealed class Action {
    abstract fun operate(f1: Float, f2: Float): Float

    data object Plus : Action() {
        override fun toString() = "+"
        override fun operate(f1: Float, f2: Float) = f1 + f2
    }

    data object Minus : Action() {
        override fun toString() = "-"
        override fun operate(f1: Float, f2: Float) = f1 - f2
    }

    data object Divide : Action() {
        override fun toString() = "/"
        override fun operate(f1: Float, f2: Float) = f1 / f2
    }

    data object Multiply : Action() {
        override fun toString() = "*"
        override fun operate(f1: Float, f2: Float) = f1 * f2
    }
}