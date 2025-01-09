package com.example.ParkinsonApp.Screens.Doctor

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ParkinsonApp.DataTypes.PatientAction
import com.example.ParkinsonApp.Firebase.FirebaseRepository
import com.example.ParkinsonApp.Navigation.SharedViewModel
import com.example.ParkinsonApp.R

@Composable
fun DoctorMainScreen(
    sharedViewModel: SharedViewModel,
    onAddPatientClick: () -> Unit,
    onYourPatientsClick: () -> Unit,
    onYourProfileClick: () -> Unit,
    onRecentActionClicked: (String) -> Unit
) {
    val recentActions = remember { getRecentPatientActions() }
    val pagerState = rememberPagerState { recentActions.size }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Swiper for recent patient actions
        RecentActionsPager(
            recentActions = recentActions,
            pagerState = pagerState,
            onRecentActionClicked = onRecentActionClicked
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Buttons
        ActionButtonsSection(
            onAddPatientClick = onAddPatientClick,
            onYourPatientsClick = onYourPatientsClick,
            onYourProfileClick = onYourProfileClick
        )
    }
}

@Composable
fun RecentActionsPager(
    recentActions: List<PatientAction>,
    pagerState: PagerState,
    onRecentActionClicked: (String) -> Unit
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) { page ->
        if (page < recentActions.size) {
            val action = recentActions[page]
            RecentActionCard(
                action = action,
                onRecentActionClicked = onRecentActionClicked
            )
        }
    }
}

@Composable
fun RecentActionCard(
    action: PatientAction,
    onRecentActionClicked: (String) -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable { 
                onRecentActionClicked(action.patientId) 
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            // Patient's photo
            Image(
                painter = painterResource(id = action.patientImageRes),
                contentDescription = "Patient Photo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(50.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Patient's name
            Text(
                text = "${action.patientFirstName} ${action.patientLastName}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Action description with icon
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = action.actionIconRes),
                    contentDescription = "Action Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = action.actionDescription,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun ActionButtonsSection(
    onAddPatientClick: () -> Unit,
    onYourPatientsClick: () -> Unit,
    onYourProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Button 1: Add a Patient
        ActionButton(
            text = "Add Patient",
            iconResId = R.drawable.add_patient,
            onClick = onAddPatientClick
        )

        // Button 2: Your Patients
        ActionButton(
            text = "Your Patients",
            iconResId = R.drawable.patients,
            onClick = onYourPatientsClick
        )

        // Button 3: Your Profile
        ActionButton(
            text = "Your Profile",
            iconResId = R.drawable.doctor,
            onClick = onYourProfileClick
        )
    }
}

@Composable
fun ActionButton(
    text: String,
    iconResId: Int,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .size(100.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun getRecentPatientActions(): List<PatientAction> {
    // Placeholder data; replace with actual data retrieval in your app
    return listOf(
        PatientAction(
            patientFirstName = "Joe",
            patientLastName = "Doe",
            patientImageRes = R.drawable.add_patient, // Replace with actual image resource
            actionDescription = "Added a glass of water",
            actionIconRes = R.drawable.medication_24px, // Replace with actual icon resource,
            patientId = "patientId1"
        ),
        PatientAction(
            patientFirstName = "Jane",
            patientLastName = "Smith",
            patientImageRes = R.drawable.patient_list_24px,
            actionDescription = "Confirmed medication intake",
            actionIconRes = R.drawable.medication_24px,
            patientId = "patientId2"
        ),
        // Add more actions as needed (up to 5 recent actions)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDoctorMainScreen() {
    DoctorMainScreen(
        sharedViewModel = SharedViewModel(firebaseRepository = FirebaseRepository()),
        onAddPatientClick = {},
        onYourPatientsClick = {},
        onYourProfileClick = {},
        onRecentActionClicked = {}
    )
}


