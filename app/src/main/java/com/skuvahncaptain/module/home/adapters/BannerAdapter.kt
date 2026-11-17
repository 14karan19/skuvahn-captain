package com.skuvahncaptain.module.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skuvahncaptain.R
import com.skuvahncaptain.databinding.ItemBannerBinding
import com.skuvahncaptain.module.home.models.HomeResponseModel

class BannerAdapter :
    ListAdapter<HomeResponseModel.Banner, BannerAdapter.ViewHolder>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<HomeResponseModel.Banner>() {
            override fun areItemsTheSame(old: HomeResponseModel.Banner, new: HomeResponseModel.Banner) = old.id == new.id
            override fun areContentsTheSame(old: HomeResponseModel.Banner, new: HomeResponseModel.Banner) = old == new
        }
    }

    inner class ViewHolder(val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position)

        Glide.with(holder.itemView.context)
            .load(item.bannerUrl)
            .placeholder(R.drawable.banner_placeholder)
            .into(holder.binding.imageView)
    }
}