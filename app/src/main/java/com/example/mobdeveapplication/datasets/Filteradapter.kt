package com.example.mobdeveapplication.datasets

import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobdeveapplication.Establishment
import com.example.mobdeveapplication.databinding.FilteritemBinding
import com.squareup.picasso.Picasso

class Filteradapter(private val context: Context, private var listing: ArrayList<listingobject>) : RecyclerView.Adapter<Filteradapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Filteradapter.ViewHolder {
        val binding = FilteritemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binder(listing[position], position)
    }

    override fun getItemCount() = listing.size


    inner class ViewHolder(private val binding: FilteritemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binder(item: listingobject, index: Int) {
            binding.descriptiontxt.text = item.name
            Picasso.get().load(item.picture).fit().into(binding.displaypicture)
            binding.displaypicture.setOnClickListener {
                val selectedlisting = Intent(context, Establishment::class.java)
                selectedlisting.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                selectedlisting.putExtra("name", item.name)
                selectedlisting.putExtra("rating", item.rating)
                selectedlisting.putExtra("picture", item.picture)
                context.startActivity(selectedlisting)
            }

        }
    }
}  