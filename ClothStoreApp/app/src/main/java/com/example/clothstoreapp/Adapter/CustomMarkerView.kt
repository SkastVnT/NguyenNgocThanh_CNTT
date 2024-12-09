package com.example.clothstoreapp.Adapter

import android.content.Context
import android.widget.TextView
import com.example.clothstoreapp.Model.RevenueModel
import com.example.clothstoreapp.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class CustomMarkerView(
    context: Context,
    layoutResource: Int,
    private val revenueList: List<RevenueModel>
) : MarkerView(context, layoutResource) {

    private val tvDate: TextView = findViewById(R.id.tvDate)
    private val tvRevenue: TextView = findViewById(R.id.tvRevenue)
    private val tvOrders: TextView = findViewById(R.id.tvOrders)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.let {
            val index = it.x.toInt()
            val revenueModel = revenueList.getOrNull(index)
            val value = it.y

            tvDate.text = revenueModel?.date ?: ""
            tvRevenue.text = when {
                value >= 1000 -> String.format("%.1fK$", value/1000)
                else -> String.format("%.0f$", value)
            }
            tvOrders.text = "${revenueModel?.orderCount ?: 0} order "
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}