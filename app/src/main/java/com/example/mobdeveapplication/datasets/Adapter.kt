package com.example.mobdeveapplication.datasets

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobdeveapplication.databinding.HistoryitemBinding
import com.google.android.material.snackbar.Snackbar


class Adapter(private val context: Context, private var historylist: ArrayList<Historyobject>) : RecyclerView.Adapter<Adapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.ViewHolder {
        val binding = HistoryitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binder(historylist[position],position)
    }
    override fun getItemCount() = historylist.size


    inner class ViewHolder(private val binding  : HistoryitemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun binder(item:Historyobject,index: Int){
            binding.titleHistory.text = item.location

            when (item.rating) {
                1 -> binding.ratingbar.rating = 1F
                2 -> binding.ratingbar.rating = 2F
                3 -> binding.ratingbar.rating = 3F
                4 -> binding.ratingbar.rating = 4F
                5 -> binding.ratingbar.rating = 5F
            }
            binding.viewhistorybtn.setOnClickListener {
                val information="username is " + item.location + "fatigue is " + item.rating
                Snackbar.make(itemView, information,Snackbar.LENGTH_LONG).show()
            }
        }
    }
}