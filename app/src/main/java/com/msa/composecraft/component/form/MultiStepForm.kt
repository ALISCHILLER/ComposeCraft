package com.msa.composecraft.component.form

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MultiStepForm(viewModel: MultiStepFormViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState() // استفاده از collectAsState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedContent(
            targetState = uiState.currentStep,
            transitionSpec = {
                slideInHorizontally { width -> width } + fadeIn() with
                        slideOutHorizontally { width -> -width } + fadeOut()
            }
        ) { step ->
            when (step) {
                0 -> Step1(viewModel)
                1 -> Step2(viewModel)
                2 -> Step3(viewModel)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.currentStep > 0) {
                Button(onClick = { viewModel.previousStep() }) {
                    Text("قبلی")
                }
            }

            if (uiState.currentStep < 2) {
                Button(
                    onClick = { viewModel.nextStep() },
                    enabled = uiState.isCurrentStepValid
                ) {
                    Text("بعدی")
                }
            } else {
                Button(
                    onClick = { viewModel.submitForm() },
                    enabled = uiState.isCurrentStepValid
                ) {
                    Text("ارسال")
                }
            }
        }

        if (uiState.errorMessage.isNotEmpty()) {
            Text(
                text = uiState.errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun Step1(viewModel: MultiStepFormViewModel) {
    val name by viewModel.name.collectAsState() // استفاده از collectAsState

    TextField(
        value = name,
        onValueChange = { viewModel.updateName(it) },
        label = { Text("نام") },
        isError = viewModel.uiState.value.nameError.isNotEmpty()
    )
    if (viewModel.uiState.value.nameError.isNotEmpty()) {
        Text(
            text = viewModel.uiState.value.nameError,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun Step2(viewModel: MultiStepFormViewModel) {
    val email by viewModel.email.collectAsState() // استفاده از collectAsState

    TextField(
        value = email,
        onValueChange = { viewModel.updateEmail(it) },
        label = { Text("ایمیل") },
        isError = viewModel.uiState.value.emailError.isNotEmpty()
    )
    if (viewModel.uiState.value.emailError.isNotEmpty()) {
        Text(
            text = viewModel.uiState.value.emailError,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun Step3(viewModel: MultiStepFormViewModel) {
    val phone by viewModel.phone.collectAsState() // استفاده از collectAsState

    TextField(
        value = phone,
        onValueChange = { viewModel.updatePhone(it) },
        label = { Text("تلفن") },
        isError = viewModel.uiState.value.phoneError.isNotEmpty()
    )
    if (viewModel.uiState.value.phoneError.isNotEmpty()) {
        Text(
            text = viewModel.uiState.value.phoneError,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

class MultiStepFormViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MultiStepFormState())
    val uiState: StateFlow<MultiStepFormState> = _uiState

    val name = MutableStateFlow("")
    val email = MutableStateFlow("")
    val phone = MutableStateFlow("")

    fun updateName(value: String) {
        name.value = value
        validateName()
    }

    fun updateEmail(value: String) {
        email.value = value
        validateEmail()
    }

    fun updatePhone(value: String) {
        phone.value = value
        validatePhone()
    }

    fun nextStep() {
        if (_uiState.value.isCurrentStepValid) {
            _uiState.value = _uiState.value.copy(currentStep = _uiState.value.currentStep + 1)
        }
    }

    fun previousStep() {
        _uiState.value = _uiState.value.copy(currentStep = _uiState.value.currentStep - 1)
    }

    fun submitForm() {
        if (_uiState.value.isCurrentStepValid) {
            println("Form Submitted: ${_uiState.value}")
        }
    }

    private fun validateName() {
        _uiState.value = _uiState.value.copy(
            nameError = if (name.value.isEmpty()) "نام الزامی است" else ""
        )
    }

    private fun validateEmail() {
        _uiState.value = _uiState.value.copy(
            emailError = if (email.value.isEmpty() || !email.value.contains("@")) "ایمیل معتبر نیست" else ""
        )
    }

    private fun validatePhone() {
        _uiState.value = _uiState.value.copy(
            phoneError = if (phone.value.isEmpty() || phone.value.length != 11) "تلفن معتبر نیست" else ""
        )
    }
}

data class MultiStepFormState(
    val currentStep: Int = 0,
    val nameError: String = "",
    val emailError: String = "",
    val phoneError: String = "",
    val errorMessage: String = "",
    val isCurrentStepValid: Boolean = false
)

@Preview
@Composable
private fun MultiStepFormPreview() {
    MultiStepForm()
}