package com.sudansh.appointments.ui

import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sudansh.appointments.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), OnItemClickListener {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }
    private val adapter by lazy {
        ScheduleAdapter(this)
    }
    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(bottom_sheet)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.adapter = adapter
        viewModel.liveSchedules.observe(this, Observer {
            if (it != null) {
                adapter.setItems(it.orEmpty())
                progressBar.visibility = View.GONE
            }
        })
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onItemClick(date: Date) {
        tvDate.text = DateFormat.format("dd-mm hh:mm a", date)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}
