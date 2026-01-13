package com.example.myapplication

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StudentViewModel : ViewModel() {
    private val repository = StudentRepository()

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students.asStateFlow()

    private val _filteredStudents = MutableStateFlow<List<Student>>(emptyList())
    val filteredStudents: StateFlow<List<Student>> = _filteredStudents.asStateFlow()

    var searchTerm = mutableStateOf("")
        private set

    init {
        fetchStudents()
    }

    private fun fetchStudents() {
        viewModelScope.launch {
            try {
                val studentList = repository.getStudents()
                _students.value = studentList
                _filteredStudents.value = studentList
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun onSearchTermChange(newSearchTerm: String) {
        searchTerm.value = newSearchTerm
        filterStudents()
    }

    private fun filterStudents() {
        val searchTerm = searchTerm.value.trim()
        if (searchTerm.isEmpty()) {
            _filteredStudents.value = _students.value
            return
        }

        _filteredStudents.value = _students.value.filter {
            it.hoten.contains(searchTerm, ignoreCase = true) ||
                    it.mssv.contains(searchTerm, ignoreCase = true)
        }
    }
}