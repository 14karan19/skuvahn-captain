package com.skuvahncaptain.module.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skuvahncaptain.databinding.ItemDropdownBinding
import com.skuvahncaptain.module.home.listeners.SchoolClickListener
import com.skuvahncaptain.module.home.models.SchoolResponseModel

class SchoolsDropdownAdapter(
    private val schools: ArrayList<SchoolResponseModel.School>
) : RecyclerView.Adapter<SchoolsHolder>() {

    var listener: SchoolClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolsHolder {
        return SchoolsHolder(
            ItemDropdownBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SchoolsHolder, position: Int) {

        val item = schools[position]

        holder.binding.command.text = item.name

        holder.binding.mainLayout.setOnClickListener {
            listener?.onSchoolClick(item)
        }
    }

    override fun getItemCount() = schools.size
}

class SchoolsHolder(val binding: ItemDropdownBinding) :
    RecyclerView.ViewHolder(binding.root)