package com.nikhil.buyerapp.freelanceprofileview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nikhil.buyerapp.databinding.QualificationItemBinding
import com.nikhil.buyerapp.dataclasses.Qualification

class QualAdapter(val qlist:MutableList<Qualification>):RecyclerView.Adapter<com.nikhil.buyerapp.freelanceprofileview.QualAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: QualificationItemBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bindData(position: Int){
            val item=qlist[position]
            binding.tvRollNo.setText(qlist[position].rollNo)
            binding.tvInstName.setText(qlist[position].instName)
            binding.tvAggregate.setText("${qlist[position].aggregate}/${qlist[position].max}")
            binding.tvEndYear.setText(qlist[position].endYear)
            binding.tvDegree.setText(qlist[position].degree)

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): com.nikhil.buyerapp.freelanceprofileview.QualAdapter.ViewHolder {
        val binding=QualificationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return qlist.size
    }

    override fun onBindViewHolder(holder: com.nikhil.buyerapp.freelanceprofileview.QualAdapter.ViewHolder, position: Int) {
        holder.bindData(position)
    }
    fun updateData(newList: List<Qualification>)
    {
        qlist.clear()
        qlist.addAll(newList)
        notifyDataSetChanged()
    }

}