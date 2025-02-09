package com.example.ParkinsonApp.Screens.Doctor

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ParkinsonApp.DataTypes.PatientAction
import com.example.ParkinsonApp.Firebase.FirebaseRepository
import com.example.ParkinsonApp.R
import com.example.ParkinsonApp.ViewModels.DoctorViewModel

@Composable
fun DoctorMainScreen(
    sharedViewModel: DoctorViewModel,
    onAddPatientClick: () -> Unit,
    onYourPatientsClick: () -> Unit,
    onYourProfileClick: () -> Unit,
    onRecentActionClicked: (String) -> Unit,
    paddingValues: PaddingValues
) {



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

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
    if (recentActions.isNotEmpty()) {
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
    } else {
        // Display a message when there are no recent actions
        Text(
            text = "No recent patient actions.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
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
            .padding(8.dp)
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
            // Assuming you have patient images stored with resource IDs or URLs
            // For demonstration, using a placeholder image
            Image(
                painter = painterResource(id = R.drawable.doctor),
                contentDescription = "Patient Photo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Patient's name
            Text(
                text = "${action.patientFirstName} ${action.patientLastName}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Action description
            Text(
                text = action.actionDescription,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Timestamp
            val formattedTime = remember(action.timestamp) {
                // Format the timestamp to a readable date/time string
                // You can use SimpleDateFormat or DateTimeFormatter
                // Placeholder implementation
                "Time: ${action.timestamp}"
            }
            Text(
                text = formattedTime,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
            iconResId = R.drawable.add_patient, // Replace with your actual icon resource ID
            onClick = onAddPatientClick
        )

        // Button 2: Your Patients
        ActionButton(
            text = "Your Patients",
            iconResId = R.drawable.patients, // Replace with your actual icon resource ID
            onClick = onYourPatientsClick
        )

        // Button 3: Your Profile
        ActionButton(
            text = "Your Profile",
            iconResId = R.drawable.doctor, // Replace with your actual icon resource ID
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
        shape = MaterialTheme.shapes.medium,
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



