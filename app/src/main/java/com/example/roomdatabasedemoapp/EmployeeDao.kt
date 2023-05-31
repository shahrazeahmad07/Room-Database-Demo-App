package com.example.roomdatabasedemoapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {
    @Insert
    suspend fun insert(employeeEntity: EmployeeEntity)

    @Update
    suspend fun update(employeeEntity: EmployeeEntity)

    @Delete
    suspend fun delete(employeeEntity: EmployeeEntity)

    @Query("Select * From `employee-table`")
    fun fetchAllEmployee(): Flow<List<EmployeeEntity>>

    @Query("Select * from `employee-table` where id=:id")
    fun fetchEmployeeById(id: String) : Flow<EmployeeEntity>
}