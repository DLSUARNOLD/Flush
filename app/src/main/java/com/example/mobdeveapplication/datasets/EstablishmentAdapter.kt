package com.example.mobdeveapplication.datasets

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobdeveapplication.AddEstablishment
import com.example.mobdeveapplication.EditEstablishment
import com.example.mobdeveapplication.databinding.EstablishmentitemBinding


class EstablishmentAdapter (private val context: Context, private val establishmentList: ArrayList<Establishmentobject>) : RecyclerView.Adapter<EstablishmentAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstablishmentAdapter.ViewHolder {
        val binding = EstablishmentitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EstablishmentAdapter.ViewHolder, position: Int) {
        holder.binder(establishmentList[position], position)
    }

    override fun getItemCount(): Int {
       return establishmentList.size
    }

    inner class ViewHolder(private val binding: EstablishmentitemBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun binder(item: Establishmentobject, index: Int)
            {
                binding.tvEstablishmentname.text = item.Name
                binding.tvOwner.text = item.Owner
                binding.editEstablishment.setOnClickListener {
                    val selectedestablishment = Intent (context, EditEstablishment::class.java)
                    selectedestablishment.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    selectedestablishment.putExtra("Name", item.Name)
                    selectedestablishment.putExtra("Owner", item.Owner)
                    context.startActivity(selectedestablishment)
                }
            }
        }
    }