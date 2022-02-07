package com.example.mobdeveapplication.datasets

import android.app.Activity
import android.content.Context

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobdeveapplication.databinding.HistoryitemBinding
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog


class Adapter(private val context: Context, private var historylist: ArrayList<Historyobject>, private val Activity : Activity) : RecyclerView.Adapter<Adapter.ViewHolder>() {


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

                val db = Globals().db
                db.collection("Establishments").whereEqualTo("Name",item.location).get().addOnSuccessListener{ documents ->
                    var link = ""
                    for (document in documents)
                    {
                        link = document.data["link"].toString()
                        Log.i("panalo",document.data["Name"].toString())
                    }

                    var hashTag = ShareHashtag.Builder().setHashtag("#FlushApp").build()


                    var sharecontent = ShareLinkContent.Builder().setQuote("Try one of Flush's Washrooms")
                        .setShareHashtag(hashTag)
                        .setContentUrl(Uri.parse(link))
                        .build()

                    ShareDialog.show(Activity, sharecontent)
                }
            }
        }
    }
}