package com.example.ParkinsonApp.Screens.Patient

import EmotionUI
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ParkinsonApp.DataTypes.EmotionState
import com.example.ParkinsonApp.DataTypes.MedicationUI
import com.example.ParkinsonApp.Firebase.FirebaseRepository
import com.example.ParkinsonApp.ViewModels.PatientViewModel
import emotionsList

@Composable
fun PatientMainScreen(
    patientViewModel: PatientViewModel,
    onMedicationBoxClicked: () -> Unit,
    onMealBoxClicked: () -> Unit,
    onLogout: () -> Unit,
    paddingValues: PaddingValues
) {

    val patientData by patientViewModel.patientData.collectAsStateWithLifecycle()

    val patientName = patientData?.lastName ?: ("" + patientData?.firstName) ?: ""
    val nextMedicationDetail by patientViewModel.nextMedicationDetail.collectAsState()
    val waterIntake by patientViewModel.waterIntake.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Patient's Name
        PatientGreetingSection(patientName)

        // State Pager
        PatientStatePager(
            emotionsList,
            onFaceClicked = { emotionName, emotionState ->
                patientViewModel.onStateFaceClicked(emotionName, emotionState)
            }
        )

        // Medication Box
        NextMedicationBox(
            nextMedicationDetail,
            onMedicationBoxClicked
        )



        WaterIntakeBox(
            waterIntake = waterIntake ,
            onAddGlass = {
                patientViewModel.incrementWaterIntake()
            },
            onRemoveGlass = {
                patientViewModel.decrementWaterIntake()
            }
        )
    }
}


@Composable
fun NextMedicationBox(
    medicationDetail: MedicationUI?,
    onMedicationBoxClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onMedicationBoxClicked() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        if (medicationDetail != null) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Call, // Change to appropriate icon
                    contentDescription = "Medication Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Next Medication",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Name: ${medicationDetail.name}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Dosage: ${medicationDetail.dosage}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Time: ${medicationDetail.time}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else {
            // Handle case where there is no upcoming medication
            Text(
                text = "No Medications Scheduled",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun PatientStatePager(
    emotions: List<EmotionUI>,
    onFaceClicked: (emotionName: String, emotionState: EmotionState) -> Unit
) {
    val pagerState = rememberPagerState { emotions.size }
    val coroutineScope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState,
        pageSize = PageSize.Fill,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) { page ->
        val currentEmotion = emotions[page]
        StatePage(
            currentEmotion,
            onFaceSelected = { emotionName, emotionState ->
                onFaceClicked(currentEmotion.name, emotionState)
                coroutineScope.launch {
                    val nextPage = (pagerState.currentPage + 1) % emotions.size
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        )
    }
}



@Composable
fun StatePage(
    emotionUI: EmotionUI,
    onFaceSelected: (emotionName: String, emotionState: EmotionState) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "How is your ${emotionUI.name} today?",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            emotionUI.faces.forEach { face ->
                IconButton(onClick = { onFaceSelected( emotionUI.name, face.emotionState) }) {
                    Image(
                        painter = painterResource(id = face.imageResId),
                        contentDescription = face.emotionState.name,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}




@Composable
fun PatientGreetingSection(patientName: String) {
    Text(
        text = "Welcome, $patientName!",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun WaterIntakeBox(
    waterIntake: Int,
    onAddGlass: () -> Unit,
    onRemoveGlass: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Water Intake Today",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$waterIntake Glasses",
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { onRemoveGlass() }) {
                    Text(text = "-")
                }
                Button(onClick = { onAddGlass() }) {
                    Text(text = "+")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewPatientMainScreen() {
    PatientMainScreen(
        patientViewModel = PatientViewModel(firebaseRepository = FirebaseRepository()),
        onMedicationBoxClicked = {},
        onMealBoxClicked = {},
        onLogout = {},
        paddingValues = PaddingValues(16.dp)
    )
}

