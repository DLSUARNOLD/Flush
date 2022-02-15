package com.example.mobdeveapplication.datasets

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobdeveapplication.Establishment
import com.example.mobdeveapplication.databinding.ActivityFilterItemBinding
import com.squareup.picasso.Picasso

class CategoriesAdapter(private val context: Context, private var listing: ArrayList<Listingobject>) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesAdapter.ViewHolder {
        val binding = ActivityFilterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binder(listing[position], position)
    }

    override fun getItemCount() = listing.size


    inner class ViewHolder(private val binding: ActivityFilterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binder(item: Listingobject, index: Int) {
            binding.descriptiontext.text = item.name
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