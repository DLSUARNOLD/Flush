package com.example.mobdeveapplication.datasets

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobdeveapplication.databinding.HistoryitemBinding
import com.example.mobdeveapplication.databinding.SearchitemBinding


class SearchAdapter(private val context: Context, private var searchlist: ArrayList<searchobject>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val binding = SearchitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binder(searchlist[position],position)
    }
    override fun getItemCount() = searchlist.size


    inner class ViewHolder(private val binding  : SearchitemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun binder(item: searchobject, index: Int){
            binding.titleHistory.text = item.name
        }
    }
}