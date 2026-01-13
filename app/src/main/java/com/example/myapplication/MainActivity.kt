package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                StudentApp()
            }
        }
    }
}

@Composable
fun StudentApp() {
    val navController = rememberNavController()
    val viewModel: StudentViewModel = viewModel()

    NavHost(navController = navController, startDestination = "student_list") {
        composable("student_list") {
            StudentListScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            "student_detail/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getString("studentId")
            val student = viewModel.students.collectAsState().value.find { it.id == studentId }
            student?.let {
                StudentDetailScreen(student = it)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen(navController: NavController, viewModel: StudentViewModel) {
    val students by viewModel.filteredStudents.collectAsState()
    val searchTerm by viewModel.searchTerm

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Danh sách sinh viên") }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TextField(
                value = searchTerm,
                onValueChange = { viewModel.onSearchTermChange(it) },
                label = { Text("Tìm kiếm theo tên hoặc MSSV") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            LazyColumn {
                items(students) { student ->
                    StudentListItem(student = student) {
                        navController.navigate("student_detail/${student.id}")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StudentListItem(student: Student, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            GlideImage(
                model = student.thumbnail,
                contentDescription = "Student thumbnail",
                modifier = Modifier.size(80.dp)
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = student.hoten, style = MaterialTheme.typography.titleMedium)
                Text(text = student.mssv, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailScreen(student: Student) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(student.hoten) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            GlideImage(
                model = student.thumbnail,
                contentDescription = "Student thumbnail",
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Họ tên: ${student.hoten}")
            Text(text = "MSSV: ${student.mssv}")
            Text(text = "Ngày sinh: ${student.ngaysinh}")
            Text(text = "Email: ${student.email}")
        }
    }
}