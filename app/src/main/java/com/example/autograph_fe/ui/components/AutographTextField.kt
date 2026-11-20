package com.example.autograph_fe.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.autograph_fe.R
import com.example.autograph_fe.ui.theme.AutographTheme

@Composable
fun AutographTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    errorMessage: String? = null,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onSubmit: () -> Unit = {},
    enabled: Boolean = true,
    belowField: (@Composable () -> Unit)? = null,
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(12.dp)
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    // Lets us ask "is this field focused right now?"
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val isError = errorMessage != null
    val ringColor = if (isError) colors.error else colors.primary
    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> colors.error
            isFocused -> colors.primary
            else -> colors.outline
        },
        label = "borderColor",
    )
    val ringAlpha by animateFloatAsState(
        targetValue = if (isFocused || isError) 0.14f else 0f,
        label = "ringAlpha",
    )

    Column(modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = colors.onSurfaceVariant,
            modifier = Modifier.padding(start = 2.dp, bottom = 7.dp),
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = colors.onSurface),
            cursorBrush = SolidColor(colors.primary),
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType,
                imeAction = imeAction,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    defaultKeyboardAction(ImeAction.Done)   // still hide the keyboard
                    onSubmit()
                },
            ),
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                // The focus ring: a 3dp "shadow" with no blur, drawn just outside the box
                .dropShadow(shape, Shadow(radius = 0.dp, spread = 3.dp, color = ringColor.copy(alpha = ringAlpha)))
                .background(colors.surface, shape)
                .border(1.dp, borderColor, shape),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 14.dp, end = if (isPassword) 2.dp else 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(Modifier.weight(1f)) {
                        if (value.isEmpty() && placeholder != null) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.onSurface.copy(alpha = 0.5f),
                            )
                        }
                        innerTextField()   // the actual typing area
                    }
                    if (isPassword) {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(
                                    if (passwordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility
                                ),
                                contentDescription = stringResource(
                                    if (passwordVisible) R.string.hide_password else R.string.show_password
                                ),
                                tint = colors.onSurfaceVariant,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
                }
            },
        )

        belowField?.invoke()

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal),
                color = colors.error,
                modifier = Modifier.padding(start = 2.dp, top = 6.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AutographTextFieldPreview() {
    AutographTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            AutographTextField(value = "", onValueChange = {}, label = "Email", placeholder = "you@example.com")
            AutographTextField(value = "maya@", onValueChange = {}, label = "Email",
                errorMessage = "Enter a valid email address")
            AutographTextField(value = "secret123", onValueChange = {}, label = "Password",
                isPassword = true)
        }
    }
}
