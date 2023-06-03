package com.example.roomdatabasedemoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabasedemoapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var itemRecyclerAdapter: ItemRecyclerAdapter
    private lateinit var listOfEmployees: ArrayList<EmployeeEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //! creating employeeDao object
        val employeeDao = (application as EmployeeApp).db.employeeDao()


        //! getting all employees:
        lifecycleScope.launch {
            employeeDao.fetchAllEmployee().collect {
                listOfEmployees = ArrayList(it)
                itemRecyclerAdapter = ItemRecyclerAdapter(listOfEmployees)
                showAllEmployees(listOfEmployees)
            }
        }



        //! add record button
        binding?.btnAddRecord?.setOnClickListener {
            addRecord(employeeDao)
        }

    }

    fun showAllEmployees(listOfAllEmployees: ArrayList<EmployeeEntity>) {
        if (listOfAllEmployees.isNotEmpty()) {
            //! setting recyclerView:
            binding?.recyclerView?.layoutManager = LinearLayoutManager(this)
            binding?.recyclerView?.adapter = itemRecyclerAdapter
            binding?.tvNoData?.visibility = View.GONE
            binding?.recyclerView?.visibility = View.VISIBLE
        } else {
            binding?.tvNoData?.visibility = View.VISIBLE
            binding?.recyclerView?.visibility = View.GONE
        }
    }

    private fun addRecord(employeeDao: EmployeeDao) {
        val name = binding?.etName?.text.toString()
        val email = binding?.etEmail?.text.toString()
        if (name.isNotEmpty() && email.isNotEmpty()) {
            lifecycleScope.launch {
                employeeDao.insert(EmployeeEntity(name = name, email = email))
                binding?.etName?.text?.clear()
                binding?.etEmail?.text?.clear()
                Toast.makeText(applicationContext, "Record Added!!", Toast.LENGTH_SHORT).show()
            }
        }else {
            Toast.makeText(this, "Enter all fields!!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (binding != null) {
            binding = null
        }
    }
}