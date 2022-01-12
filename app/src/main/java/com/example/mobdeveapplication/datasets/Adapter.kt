package com.example.mobdeveapplication.datasets

import android.content.Context

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobdeveapplication.Facebook
import com.example.mobdeveapplication.databinding.HistoryitemBinding


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
            var shareBtn = binding.shareBtn

            when (item.rating) {
                1 -> binding.ratingbar.rating = 1F
                2 -> binding.ratingbar.rating = 2F
                3 -> binding.ratingbar.rating = 3F
                4 -> binding.ratingbar.rating = 4F
                5 -> binding.ratingbar.rating = 5F
            }
            shareBtn.setOnClickListener {
                val facebookintent = Intent(context, Facebook::class.java)
                facebookintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(facebookintent)
            }
        }
    }
}