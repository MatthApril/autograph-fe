package com.example.autograph_fe.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.autograph_fe.R
import com.example.autograph_fe.ui.theme.AutographTheme
import kotlin.math.PI
import kotlin.math.sin

const val OTP_LENGTH = 6

/** Keeps digits only, at most [length]. Turns a pasted "482 917" into "482917". */
fun sanitizeOtpInput(input: String, length: Int = OTP_LENGTH): String =
    input.filter { it.isDigit() }.take(length)

/**
 * One invisible text field holding the whole code, drawn as six boxes.
 * Typing, deleting and pasting work like any text field, because it is one.
 */
@Composable
fun OtpCodeInput(
    code: String,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = OTP_LENGTH,
    isError: Boolean = false,
    enabled: Boolean = true,
    onComplete: (String) -> Unit = {},
    focusRequester: FocusRequester = remember { FocusRequester() },
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    // A short sideways shake every time an error appears: progress runs from 0 to 1
    val shakeProgress = remember { Animatable(0f) }
    LaunchedEffect(isError) {
        if (isError) {
            shakeProgress.snapTo(0f)
            shakeProgress.animateTo(1f, animationSpec = tween(durationMillis = 400, easing = LinearEasing))
        }
    }

    val description = stringResource(R.string.otp_input_description, code.length, length)

    // Hide the selection handles of the invisible text
    val noSelection = TextSelectionColors(handleColor = Color.Transparent, backgroundColor = Color.Transparent)

    CompositionLocalProvider(LocalTextSelectionColors provides noSelection) {
        BasicTextField(
            // The cursor always sits at the end, so backspace always removes the last digit
            value = TextFieldValue(text = code, selection = TextRange(code.length)),
            onValueChange = { newValue ->
                val digits = sanitizeOtpInput(newValue.text, length)
                if (digits != code) {
                    onCodeChange(digits)
                    if (digits.length == length) onComplete(digits)
                }
            },
            enabled = enabled,
            singleLine = true,
            textStyle = TextStyle(color = Color.Transparent),   // the boxes draw the digits
            cursorBrush = SolidColor(Color.Transparent),        // and the caret
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,     // digits-only keypad
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = { if (code.length == length) onComplete(code) }),
            interactionSource = interactionSource,
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .offset {
                    // Three left-right swings that start at 10dp and fade to nothing
                    val p = shakeProgress.value
                    val dx = sin(p * 6 * PI).toFloat() * 10f * (1f - p)
                    IntOffset(dx.dp.roundToPx(), 0)
                }
                .semantics {
                    contentType = ContentType.SmsOtpCode            // lets the keyboard suggest codes
                    contentDescription = description
                },
            decorationBox = { innerTextField ->
                Box {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val activeIndex = code.length.coerceAtMost(length - 1)
                        repeat(length) { index ->
                            OtpBox(
                                digit = code.getOrNull(index),
                                isActive = enabled && isFocused && index == activeIndex,
                                isError = isError,
                                enabled = enabled,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                    // The real field must stay in the layout to receive taps and typing
                    Box(Modifier.matchParentSize()) { innerTextField() }
                }
            },
        )
    }
}

@Composable
private fun OtpBox(
    digit: Char?,
    isActive: Boolean,
    isError: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(12.dp)
    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> colors.error
            isActive -> colors.primary
            digit != null -> colors.onSurfaceVariant   // filled boxes get a darker border
            else -> colors.outline
        },
        animationSpec = tween(durationMillis = 150),
        label = "otpBorder",
    )
    val showRing = isError || isActive
    val ringColor = if (isError) colors.error else colors.primary

    Box(
        modifier = modifier
            .height(56.dp)
            .graphicsLayer { alpha = if (enabled) 1f else 0.5f }
            .dropShadow(
                shape = shape,
                shadow = Shadow(radius = 0.dp, spread = 3.dp, color = ringColor.copy(alpha = if (showRing) 0.14f else 0f)),
            )
            .background(colors.surface, shape)
            .border(1.dp, borderColor, shape),
        contentAlignment = Alignment.Center,
    ) {
        when {
            digit != null -> Text(
                text = digit.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = colors.onSurface,
            )
            isActive -> BlinkingCaret()
        }
    }
}

@Composable
private fun BlinkingCaret() {
    val transition = rememberInfiniteTransition(label = "caret")
    val caretAlpha by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 500), RepeatMode.Reverse),
        label = "caretAlpha",
    )
    Box(
        Modifier
            .size(width = 2.dp, height = 24.dp)
            .graphicsLayer { alpha = caretAlpha }   // read here: redraws without recomposing
            .background(MaterialTheme.colorScheme.primary),
    )
}

@Preview(name = "Typing", showBackground = true)
@Composable
private fun OtpTypingPreview() = AutographTheme {
    OtpCodeInput(code = "482", onCodeChange = {}, modifier = Modifier.padding(16.dp))
}

@Preview(name = "Wrong code", showBackground = true)
@Composable
private fun OtpErrorPreview() = AutographTheme {
    OtpCodeInput(code = "111111", onCodeChange = {}, isError = true, modifier = Modifier.padding(16.dp))
}

@Preview(name = "Expired", showBackground = true)
@Composable
private fun OtpDisabledPreview() = AutographTheme {
    OtpCodeInput(code = "", onCodeChange = {}, enabled = false, modifier = Modifier.padding(16.dp))
}
