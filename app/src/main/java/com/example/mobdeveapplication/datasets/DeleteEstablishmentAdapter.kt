package com.example.mobdeveapplication.datasets

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobdeveapplication.DeleteEstablishment
import com.example.mobdeveapplication.databinding.ActivityDeleteEstablishmentItemBinding

//Adapter class for Recycler View
class DeleteEstablishmentAdapter (private val context: Context, private val establishmentList: ArrayList<Establishmentobject>) : RecyclerView.Adapter<DeleteEstablishmentAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeleteEstablishmentAdapter.ViewHolder {
        val binding = ActivityDeleteEstablishmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeleteEstablishmentAdapter.ViewHolder, position: Int) {
        holder.binder(establishmentList[position], position)
    }

    override fun getItemCount(): Int {
        return establishmentList.size
    }

    inner class ViewHolder(private val binding: ActivityDeleteEstablishmentItemBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun binder(item: Establishmentobject, index: Int)
        {
            binding.tvEstablishmentname.text = item.Name
            binding.tvOwner.text = item.Owner
            binding.btnDeleteEstablishment.setOnClickListener {
                val db = Globals().db
                db.collection("Establishments").whereEqualTo("Name", item.Name).get()
                    .addOnSuccessListener {
                        for (document in it)
                            db.collection("Establishments").document(document.id).delete()
                    }
                val selectedestablishment = Intent (context, DeleteEstablishment::class.java)
                context.startActivity(selectedestablishment)
            }
        }
    }
}