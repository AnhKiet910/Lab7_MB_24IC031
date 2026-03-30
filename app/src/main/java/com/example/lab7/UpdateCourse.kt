package com.example.lab7

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore

val greenColor = Color(0xFF0F9D58)

class UpdateCourse : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {

                Scaffold(
                    topBar = {
                        TopAppBar(
                            backgroundColor = greenColor,
                            title = {
                                Text(
                                    text = "Update Course",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                )
                            }
                        )
                    }
                ) { innerPadding ->

                    Text(
                        modifier = Modifier.padding(innerPadding),
                        text = "Cap nhat du lieu."
                    )

                    firebaseUI(
                        LocalContext.current,
                        intent.getStringExtra("courseName"),
                        intent.getStringExtra("courseDuration"),
                        intent.getStringExtra("courseDescription"),
                        intent.getStringExtra("courseID")
                    )
                }
            }
        }
    }


    @Composable
    fun firebaseUI(
        context: Context,
        name: String?,
        duration: String?,
        description: String?,
        courseID: String?
    ) {

        val courseName = remember { mutableStateOf(name) }
        val courseDuration = remember { mutableStateOf(duration) }
        val courseDescription = remember { mutableStateOf(description) }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.White),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextField(
                value = courseName.value.toString(),
                onValueChange = { courseName.value = it },
                placeholder = { Text("Enter course name") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 15.sp
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = courseDuration.value.toString(),
                onValueChange = { courseDuration.value = it },
                placeholder = { Text("Enter duration") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 15.sp
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = courseDescription.value.toString(),
                onValueChange = { courseDescription.value = it },
                placeholder = { Text("Enter description") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 15.sp
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {

                    if (TextUtils.isEmpty(courseName.value.toString())) {

                        Toast.makeText(
                            context,
                            "Enter course name",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        updateDataToFirebase(
                            courseID,
                            courseName.value,
                            courseDuration.value,
                            courseDescription.value,
                            context
                        )
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)

            ) {

                Text(
                    text = "Update Data",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }


    private fun updateDataToFirebase(
        courseID: String?,
        name: String?,
        duration: String?,
        description: String?,
        context: Context
    ) {

        val updatedCourse =
            Course(courseID, name, duration, description)

        val db = FirebaseFirestore.getInstance()

        db.collection("Courses")
            .document(courseID.toString())
            .set(updatedCourse)

            .addOnSuccessListener {

                Toast.makeText(
                    context,
                    "Course Updated",
                    Toast.LENGTH_SHORT
                ).show()

                context.startActivity(
                    Intent(context, CourseDetailsActivity::class.java)
                )
            }

            .addOnFailureListener {

                Toast.makeText(
                    context,
                    "Update failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}