package com.sudansh.appointments.ui

import android.annotation.SuppressLint
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sudansh.appointments.R
import com.sudansh.appointments.databinding.ItemScheduleBinding
import java.util.*

class ScheduleAdapter(private var callback: OnItemClickListener) : RecyclerView.Adapter<ScheduleViewHolder>() {
    private val items: MutableList<Date> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val binding = DataBindingUtil.inflate<ItemScheduleBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_schedule,
                parent,
                false
        )
        return ScheduleViewHolder(binding)
    }


    override fun getItemCount() = items.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(vh: ScheduleViewHolder, position: Int) {
        val binding = vh.binding()
        val date = items[position]
        binding.day.text = DateFormat.format("EEE", date)
        binding.date.text = DateFormat.format("dd", date)
        binding.time.text = DateFormat.format("HH:mm a", date)
        binding.mainContainer.setOnClickListener {
            callback.onItemClick(items[position])
        }
        binding.executePendingBindings()
    }

    fun setItems(data: List<Date>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }
}

class ScheduleViewHolder(private val binding: ItemScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {

    fun binding() = binding
}

interface OnItemClickListener {
    fun onItemClick(date: Date)
}