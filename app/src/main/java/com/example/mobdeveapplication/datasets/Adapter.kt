package com.example.mobdeveapplication.datasets

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobdeveapplication.databinding.HistoryitemBinding
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog

class Adapter(private val context: Context, private var historylist: ArrayList<Historyobject>,private val activity : Activity) : RecyclerView.Adapter<Adapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.ViewHolder {
        val binding = HistoryitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binder(historylist[position], position)
    }

    override fun getItemCount() = historylist.size


    inner class ViewHolder( val binding: HistoryitemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun binder(item: Historyobject, index: Int) {
            binding.titleHistory.text = item.location
            val shareBtn = binding.shareBtn

            binding.ratingbar.rating = item.rating
            shareBtn.setOnClickListener {

                val db = Globals().db
                db.collection("Establishments").whereEqualTo("Name", item.location).get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            //arrayrecycler.add(searchobject(document.data["Name"].toString()))
                            //Log.i("panalo",document.data["Name"].toString())
                        }
                        var hashTag = ShareHashtag.Builder().setHashtag("#FlushApp").build()
                        var sharecontent =
                            ShareLinkContent.Builder().setQuote("Try one of Flush's Washrooms")
                                .setShareHashtag(hashTag)
                                .setContentUrl(Uri.parse("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.barbourproductsearch.info%2Fthe-importance-of-washrooms-in-building-design-news078970.html&psig=AOvVaw1VqmTtgB7RPEmshKZz1lDj&ust=1642069491306000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCPijxdj_q_UCFQAAAAAdAAAAABAD%22"))
                                .build()
                        ShareDialog.show(activity, sharecontent)
                    }
            }
        }
    }
}