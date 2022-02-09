package com.example.mobdeveapplication.datasets

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobdeveapplication.databinding.HistoryitemBinding
import com.example.mobdeveapplication.databinding.SearchitemBinding


class SearchAdapter(private val context: Context, private var searchlist: ArrayList<searchobject>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private  lateinit var  mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener : onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val binding = SearchitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(mListener,binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binder(searchlist[position],position)
    }
    override fun getItemCount() = searchlist.size



    inner class ViewHolder(listener: onItemClickListener,private val binding  : SearchitemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
        fun binder(item: searchobject, index: Int){
            binding.titleHistory.text = item.name
        }
    }
}