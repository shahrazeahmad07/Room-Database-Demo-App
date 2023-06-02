package com.example.roomdatabasedemoapp

import android.app.Application

class EmployeeApp : Application() {
    val db by lazy {
        EmployeeDatabase.getInstance(this)
    }
}