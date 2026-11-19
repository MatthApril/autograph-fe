package com.example.autograph_fe.ui.screens.auth

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autograph_fe.R
import com.example.autograph_fe.ui.components.ThemeToggleButton
import com.example.autograph_fe.ui.theme.AutographTheme

/** Back arrow left, small title centered, theme toggle right. */
@Composable
fun AuthTopBar(
    title: String,
    onBack: () -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxWidth().height(56.dp).padding(horizontal = 8.dp)) {
        IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.back),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(20.dp),
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Center),
        )
        ThemeToggleButton(
            isDark = isDarkTheme,
            onToggle = onToggleTheme,
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}

/** —— or —— */
@Composable
fun LabeledDivider(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        HorizontalDivider(Modifier.weight(1f), color = MaterialTheme.colorScheme.outline)
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 16.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HorizontalDivider(Modifier.weight(1f), color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
fun GoogleSignInButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int? = null,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(46.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        if (iconRes != null) {
            Image(painterResource(iconRes), contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = stringResource(R.string.continue_with_google),
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp),
        )
    }
}

@Composable
fun TermsCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
) {
    val colors = MaterialTheme.colorScheme
    val boxShape = RoundedCornerShape(6.dp)
    val linkStyle = TextLinkStyles(
        SpanStyle(color = AutographTheme.extraColors.link, textDecoration = TextDecoration.Underline)
    )
    // Read the strings first; then build one sentence with two tappable links inside it
    val prefix = stringResource(R.string.terms_prefix)
    val terms = stringResource(R.string.terms)
    val and = stringResource(R.string.terms_and)
    val privacy = stringResource(R.string.privacy_policy)
    val sentence = buildAnnotatedString {
        append(prefix)
        withLink(LinkAnnotation.Clickable(tag = "terms", styles = linkStyle) { onTermsClick() }) { append(terms) }
        append(and)
        withLink(LinkAnnotation.Clickable(tag = "privacy", styles = linkStyle) { onPrivacyClick() }) { append(privacy) }
    }

    Row(
        modifier = modifier.toggleable(
            value = checked,
            role = Role.Checkbox,                 // screen readers announce "checkbox, checked"
            onValueChange = onCheckedChange,
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(if (checked) colors.primary else colors.surface, boxShape)
                .border(
                    width = 1.dp,
                    color = when {
                        checked -> colors.primary
                        isError -> colors.error
                        else -> colors.outline
                    },
                    shape = boxShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (checked) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = colors.onPrimary,
                    modifier = Modifier.size(14.dp),
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        Text(
            text = sentence,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal),
            color = colors.onSurfaceVariant,
        )
    }
}

/** Three bars plus Weak / Good / Strong. Shows nothing until something is typed. */
@Composable
fun PasswordStrengthMeter(strength: Int, modifier: Modifier = Modifier) {
    if (strength == 0) return

    val color = if (strength == 1) MaterialTheme.colorScheme.error else AutographTheme.extraColors.success
    val label = stringResource(
        when (strength) {
            1 -> R.string.strength_weak
            2 -> R.string.strength_good
            else -> R.string.strength_strong
        }
    )

    Row(
        modifier = modifier.fillMaxWidth().padding(start = 1.dp, top = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        repeat(3) { index ->
            Box(
                Modifier
                    .size(width = 52.dp, height = 3.dp)
                    .background(
                        color = if (index < strength) color else MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(4.dp),
                    )
            )
        }
        Spacer(Modifier.weight(1f))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold),
            color = color,
        )
    }
}

@Composable
fun TextLink(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = AutographTheme.extraColors.link,
        modifier = modifier
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = 6.dp, horizontal = 2.dp),   // a bigger tap target than the text itself
    )
}

@Composable
fun AuthFooter(prompt: String, action: String, onAction: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = prompt,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.width(2.dp))
        TextLink(text = action, onClick = onAction)
    }
}
