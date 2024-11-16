package pt.isel.chimp.authentication.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

/**
 * The authentication text field.
 *
 * @param label label to show
 * @param value value to show
 * @param onValueChange callback to be invoked when the text is changed
 * @param modifier modifier to be applied to the text field
 * @param required whether the text field is required or not
 * @param maxLength maximum length of the text field
 * @param forbiddenCharacters characters that are not allowed in the text field
 * @param errorMessage error message to show
 * @param visualTransformation visual transformation to apply to the text field
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    required: Boolean = false,
    maxLength: Int? = null,
    forbiddenCharacters: List<Char> = listOf<Char>(' ', '\n', '\t'),
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        label = {
            Text(
                text = "$label${if (required) " *" else ""}" +
                        if (errorMessage != null) " - $errorMessage" else ""
            )
        },
        value = value,
        onValueChange = {
            var filteredValue = it.filter { c -> c !in forbiddenCharacters }
            if (maxLength != null && filteredValue.length > maxLength)
                filteredValue = filteredValue.substring(0 until maxLength)

            onValueChange(filteredValue)
        },
        singleLine = true,
        modifier = modifier,
        isError = errorMessage != null,
        visualTransformation = visualTransformation,
        keyboardOptions =
        if (label == "Password") {
            KeyboardOptions(
                keyboardType = KeyboardType.Password,
                autoCorrect = false,
            ) } else {
            KeyboardOptions(
                keyboardType = KeyboardType.Text,
            )
        }
    )
}
