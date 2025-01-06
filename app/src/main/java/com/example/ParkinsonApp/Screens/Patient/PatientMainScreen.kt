package com.example.ParkinsonApp.Screens.Patient

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import com.example.ParkinsonApp.Navigation.SharedViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.ParkinsonApp.Firebase.FirebaseRepository
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color

@Composable
fun PatientMainScreen(
    sharedViewModel: SharedViewModel,
    onMedicationBoxClicked: () -> Unit,
    onMealBoxClicked: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Patient's Name
        PatientGreetingSection(patientName = "John Doe")

        // State Pager
        val stateList = listOf("Anxiety", "Concentration", "Mood")
        PatientStatePager(
            stateList = stateList,
            onFaceClicked = { state, faceIndex ->
                sharedViewModel.onStateFaceClicked(state, faceIndex)
            }
        )

        // Medication Box
        NextMedicationBox(
            nextMedicationTime = "08:00 AM",
            onMedicationBoxClicked = onMedicationBoxClicked
        )

        // Meal Box
        NextMealBox(
            nextMealTime = "12:00 PM",
            onMealBoxClicked = onMealBoxClicked,
            mealStatus = true // Example status
        )

        // Water Intake
        WaterIntakeBox(
            waterIntake = 5,
            onAddGlass = {
                sharedViewModel.incrementWaterIntake()
            },
            onRemoveGlass = {
                sharedViewModel.decrementWaterIntake()
            }
        )
    }
}

@Composable
fun NextMealBox(
    nextMealTime: String,
    onMealBoxClicked: () -> Unit,
    mealStatus: Boolean // true for green (ready), false for red (not ready)
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onMealBoxClicked() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Meal Status Icon",
                tint = if (mealStatus) Color.Green else Color.Red,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Next Meal Time",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = nextMealTime,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun NextMedicationBox(
    nextMedicationTime: String,
    onMedicationBoxClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onMedicationBoxClicked() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Medication Icon",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Next Medication Time",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = nextMedicationTime,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun PatientStatePager(
    stateList: List<String>,
    onFaceClicked: (state: String, faceIndex: Int) -> Unit
) {
    val pagerState = rememberPagerState { stateList.size }
    val coroutineScope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState,
        pageSize = PageSize.Fill,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) { page ->
        val currentState = stateList[page]
        StatePage(
            stateName = currentState,
            onFaceSelected = { faceIndex ->
                onFaceClicked(currentState, faceIndex)
                // Move to the next page using coroutineScope
                coroutineScope.launch {
                    val nextPage = (pagerState.currentPage + 1) % stateList.size
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        )
    }
}



@Composable
fun StatePage(
    stateName: String,
    onFaceSelected: (faceIndex: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "How is your $stateName today?",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Faces representation (Happy, Neutral, Sad)
        val faces = listOf(Icons.Default.Face, Icons.Default.Face, Icons.Default.Face)
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            faces.forEachIndexed { index, icon ->
                IconButton(onClick = { onFaceSelected(index) }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPatientMainScreen() {
    val sharedViewModel = SharedViewModel(firebaseRepository = FirebaseRepository())
    PatientMainScreen(
        sharedViewModel = sharedViewModel,
        onMedicationBoxClicked = {},
    onMealBoxClicked = {},
        onLogout = { /* Handle logout action */ }
    )
}