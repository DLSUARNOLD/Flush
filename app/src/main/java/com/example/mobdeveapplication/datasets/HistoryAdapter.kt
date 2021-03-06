package com.example.mobdeveapplication.datasets

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobdeveapplication.databinding.ActivityHistoryItemBinding
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog

//Adapter class for Recycler View
class HistoryAdapter(private val context: Context, private var historylist: ArrayList<Historyobject>, private val activity : Activity) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        val binding = ActivityHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binder(historylist[position], position)
    }

    override fun getItemCount() = historylist.size

    inner class ViewHolder(val binding: ActivityHistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun binder(item: Historyobject, index: Int) {
            binding.tvHistoryItemTitle.text = item.location
            val shareBtn = binding.btnHistoryItemShare
            binding.rbHistoryItemRating.rating = item.rating
            shareBtn.setOnClickListener {
                shareBtn.setOnClickListener {
                    val db = Globals().db
                    db.collection("Establishments").whereEqualTo("Name", item.location).get()
                        .addOnSuccessListener { documents ->
                            var link = ""
                            for (document in documents) {
                                link = document.data["link"].toString()
                            }
                            val hashTag = ShareHashtag.Builder().setHashtag("#FlushApp").build()
                            val sharecontent =
                                ShareLinkContent.Builder().setQuote("Try one of Flush's Washrooms")
                                    .setShareHashtag(hashTag)
                                    .setContentUrl(Uri.parse(link))
                                    .build()
                            ShareDialog.show(activity, sharecontent)
                        }
                }
            }
        }
    }
}