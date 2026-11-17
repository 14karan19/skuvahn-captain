package com.skuvahncaptain.module.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skuvahncaptain.databinding.ItemRoutesBinding
import com.skuvahncaptain.module.home.listeners.*
import com.skuvahncaptain.module.home.models.RouteResponseModel

class RoutesAdapter(
    private val list: ArrayList<RouteResponseModel.Route>
) : RecyclerView.Adapter<RouteViewHolder>() {

    var routeClickListener: RouteClickListener? = null
    var deleteRouteClickListener: RouteDeleteClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        return RouteViewHolder(
            ItemRoutesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {

        val item = list[position]
        holder.bind(item)

        holder.binding.mainLayout.setOnClickListener {
            routeClickListener?.onRouteClick(item)
        }

        holder.binding.btnDelete.setOnClickListener {
            deleteRouteClickListener?.onDeleteRouteClick(item)
        }

        holder.binding.ivDelete.setOnClickListener {
            deleteRouteClickListener?.onDeleteRouteClick(item)
        }
    }

    override fun getItemCount() = list.size

    fun updateData(newList: List<RouteResponseModel.Route>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}

class RouteViewHolder(val binding: ItemRoutesBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(route: RouteResponseModel.Route) {
        binding.route = route
    }
}