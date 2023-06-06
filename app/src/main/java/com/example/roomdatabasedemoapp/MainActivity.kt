package com.example.roomdatabasedemoapp

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabasedemoapp.databinding.ActivityMainBinding
import com.example.roomdatabasedemoapp.databinding.DialogBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var itemRecyclerAdapter: ItemRecyclerAdapter
    private lateinit var listOfEmployees: ArrayList<EmployeeEntity>

    // Dialog ki layout set or wese recycler view k item view ki layout set kerni hai dono ki kerni hai

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
                itemRecyclerAdapter = ItemRecyclerAdapter(listOfEmployees,
                    { position ->
                        showEditUpdateDialog(position, employeeDao)
                    }, { position ->
                        showDeleteDialog(position, employeeDao)
                    })
                showAllEmployees(listOfEmployees)
            }
        }


        //! add record button
        binding?.btnAddRecord?.setOnClickListener {
            addRecord(employeeDao)
        }

    }

    private fun showEditUpdateDialog(position: Int, employeeDao: EmployeeDao) {
        val dialog = Dialog(this, R.style.ThemeDialog)
        val dialogBinding = DialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setTitle("Update Entry")
        dialogBinding.etDialogName.setText(listOfEmployees[position].name)
        dialogBinding.etDialogEmail.setText(listOfEmployees[position].email)
        dialogBinding.btnDialogUpdate.setOnClickListener {
            lifecycleScope.launch {
                val name = dialogBinding.etDialogName.text.toString()
                val email = dialogBinding.etDialogEmail.text.toString()
                if (name.isNotEmpty() && email.isNotEmpty()) {
                    employeeDao.update(EmployeeEntity(listOfEmployees[position].id, name, email))
                    Toast.makeText(this@MainActivity, "Record Updated!!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    Toast.makeText(this@MainActivity, "Enter all fields!!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        dialogBinding.btnDialogCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun showDeleteDialog(position: Int, employeeDao: EmployeeDao) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val name = listOfEmployees[position].name
        builder.setTitle("Delete")
        builder.setMessage("Are you sure you want to delete entry: $name")
        builder.setPositiveButton("Yes") { dialog, _ ->
            lifecycleScope.launch {
                employeeDao.delete(listOfEmployees[position])
                dialog.dismiss()
                Toast.makeText(this@MainActivity, "Entry Deleted!!", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun showAllEmployees(listOfAllEmployees: ArrayList<EmployeeEntity>) {
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
        } else {
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