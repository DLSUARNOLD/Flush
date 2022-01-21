package com.example.mobdeveapplication.datasets

import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobdeveapplication.databinding.HomepageitemBinding
import com.squareup.picasso.Picasso

class featuredadapter(private val context: Context, private var listing: ArrayList<listingobject>) : RecyclerView.Adapter<featuredadapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): featuredadapter.ViewHolder {
        val binding = HomepageitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binder(listing[position], position)
    }

    override fun getItemCount() = listing.size


    inner class ViewHolder(private val binding: HomepageitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binder(item: listingobject, index: Int) {
            binding.descriptiontxt.text = item.name
            Picasso.get().load(item.picture).fit().into(binding.imageView)
        }
    }
}  