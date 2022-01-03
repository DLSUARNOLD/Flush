package com.example.mobdeveapplication.datasets

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobdeveapplication.databinding.HistoryitemBinding
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
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


            /*binding.buttonload.setOnClickListener {
                var loadprofile = Intent(context, Gameactivity::class.java)
                loadprofile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val backpack = Bundle()
                backpack.putString("profilename",item.profilename)
                backpack.putString("username",item.username)
                backpack.putString("fatigue",item.fatigue)
                backpack.putString("hunger",item.hunger)
                backpack.putString("money",item.money)
                loadprofile.putExtras(backpack)
                context.startActivity(loadprofile)
            }*/
            binding.buttonview.setOnClickListener {
                val information="username is " + item.location + "fatigue is " + item.rating
                Snackbar.make(itemView, information,LENGTH_LONG).show()
            }
        }
    }
}