package com.example.roomdatabasedemoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ItemRecyclerAdapter(
    private val list: ArrayList<EmployeeEntity>,
    private val updateMethod: (position: Int) -> Unit,
    private val deleteMethod: (position: Int) -> Unit
) :
    RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_employee_entry, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentEmployee = list[position]
        holder.tvRvName.text = currentEmployee.name
        holder.tvRvEmail.text = currentEmployee.email
        if (position % 2 == 0) {
            holder.parentLayout.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.gray
                )
            )
        } else {
            holder.parentLayout.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        }
        holder.ibRvEdit.setOnClickListener {
            updateMethod.invoke(position)
        }
        holder.ibRvDelete.setOnClickListener {
            deleteMethod.invoke(position)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parentLayout: LinearLayout
        val tvRvName: TextView
        val tvRvEmail: TextView
        val ibRvEdit : ImageButton
        val ibRvDelete: ImageButton

        init {
            parentLayout = itemView.findViewById(R.id.parentLayout)
            tvRvName = itemView.findViewById(R.id.tvRvName)
            tvRvEmail = itemView.findViewById(R.id.tvRvEmail)
            ibRvEdit = itemView.findViewById(R.id.ibRvEdit)
            ibRvDelete = itemView.findViewById(R.id.ibRvDelete)
        }
    }
}