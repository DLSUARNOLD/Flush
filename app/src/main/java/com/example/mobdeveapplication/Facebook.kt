package com.example.mobdeveapplication

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobdeveapplication.databinding.FacebookBinding
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog


private lateinit var binding: FacebookBinding

class Facebook : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FacebookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.shareBtn.setOnClickListener {
            var hashTag = ShareHashtag.Builder().setHashtag("#FlushApp").build()


            var sharecontent = ShareLinkContent.Builder().setQuote("Try one of Flush's Washrooms")
                .setShareHashtag(hashTag)
                .setContentUrl(Uri.parse("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.barbourproductsearch.info%2Fthe-importance-of-washrooms-in-building-design-news078970.html&psig=AOvVaw1VqmTtgB7RPEmshKZz1lDj&ust=1642069491306000&source=images&cd=vfe&ved=0CAsQjRxqFwoTCPijxdj_q_UCFQAAAAAdAAAAABAD"))
                .build()

            ShareDialog.show(this@Facebook, sharecontent)
        }
    }
}