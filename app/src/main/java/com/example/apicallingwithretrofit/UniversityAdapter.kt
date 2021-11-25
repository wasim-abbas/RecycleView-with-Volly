package com.example.apicallingwithretrofit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UniversityAdapter(private val listener: ItemClickListenerCallBack) :
    RecyclerView.Adapter<UniversityAdapter.UniversityViewHolder>() {

    private val item: ArrayList<UniversityModelClass> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversityViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_view, parent, false)
        val pos = UniversityViewHolder(v)
        v.setOnClickListener {
            listener.itemClicked(item[pos.adapterPosition])
        }

        return pos

    }

    override fun onBindViewHolder(holder: UniversityViewHolder, position: Int) {
        val currentItem = item[position]

        holder.uniName.text = currentItem.name
        if (currentItem.stateProvince == "null") {
            holder.province.text = "N/A"

        } else {
            holder.province.text = currentItem.stateProvince
        }

        holder.state.text = currentItem.country


        holder.code.text = currentItem.alphaTwoCode

    }

    fun updateData(updatedItem: ArrayList<UniversityModelClass>) {
        item.clear()
        item.addAll(updatedItem)
        notifyDataSetChanged()

    }

    override fun getItemCount() = item.size

    inner class UniversityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val uniName = itemView.findViewById<TextView>(R.id.uniName)
        val state = itemView.findViewById<TextView>(R.id.uniState)
        val province = itemView.findViewById<TextView>(R.id.uniprovince)
        val code = itemView.findViewById<TextView>(R.id.uniCode)


    }
}

interface ItemClickListenerCallBack {
    fun itemClicked(item: UniversityModelClass)
}