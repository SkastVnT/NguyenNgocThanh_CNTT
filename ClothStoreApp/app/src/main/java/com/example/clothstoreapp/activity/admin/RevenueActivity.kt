package com.example.clothstoreapp.activity.admin

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.clothstoreapp.Adapter.CustomMarkerView
import com.example.clothstoreapp.Model.RevenueModel
import com.example.clothstoreapp.R
import com.example.clothstoreapp.ViewModel.RevenueViewModel
import com.example.clothstoreapp.databinding.ActivityRevenueBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RevenueActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRevenueBinding
    private val viewModel: RevenueViewModel by viewModels()
    private val revenueData = MutableLiveData<List<RevenueModel>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRevenueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupDatePickers()
        setupChart()
        observeData()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Dashboard"
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupDatePickers() {
        binding.etStartDate.setOnClickListener { showDatePicker(true) }
        binding.etEndDate.setOnClickListener { showDatePicker(false) }
    }

    private fun showDatePicker(isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                val date = Calendar.getInstance().apply {
                    set(year, month, day)
                }
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(date.time)

                if (isStartDate) {
                    binding.etStartDate.setText(formattedDate)
                    viewModel.setStartDate(date.time)
                } else {
                    binding.etEndDate.setText(formattedDate)
                    viewModel.setEndDate(date.time)
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun setupChart() {
        binding.lineChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            setDrawGridBackground(false)
            animateX(1500)
            setPinchZoom(false)
            isDoubleTapToZoomEnabled = false

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                setDrawGridLines(false)
                labelRotationAngle = -30f
                textSize = 10f
            }

            axisLeft.apply {
                axisMinimum = 0f
                setDrawGridLines(true)
                setDrawAxisLine(true)
                textSize = 12f

                // Định dạng giá trị trục Y
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return when {
                            value >= 1000 -> String.format("%.1fK$", value/1000)
                            else -> String.format("%.0f$", value)
                        }
                    }
                }

                // Tự động tính toán khoảng cách phù hợp
                setLabelCount(5, true)  // Số lượng nhãn trên trục Y
            }

            axisRight.isEnabled = false
            legend.isEnabled = true

            setScaleEnabled(false)
            isDoubleTapToZoomEnabled = false

            // Thêm padding để tránh bị cắt text
            extraBottomOffset = 10f
            extraLeftOffset = 15f
        }
    }


    private fun updateChart(revenueList: List<RevenueModel>) {
        val entries = revenueList.mapIndexed { index, revenue ->
            Entry(index.toFloat(), revenue.totalRevenue.toFloat())
        }

        val dataSet = LineDataSet(entries, "Revenue").apply {
            color = ContextCompat.getColor(this@RevenueActivity, R.color.purple_500)
            setCircleColor(ContextCompat.getColor(this@RevenueActivity, R.color.purple_500))
            lineWidth = 2f
            circleRadius = 4f
            valueTextSize = 10f

            // Định dạng giá trị trên điểm
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return when {
                        value >= 1000 -> String.format("%.1fK$", value/1000)
                        else -> String.format("%.0f$", value)
                    }
                }
            }

            mode = LineDataSet.Mode.LINEAR
            setDrawFilled(false)
        }

        binding.lineChart.data = LineData(dataSet)
        binding.lineChart.invalidate()
    }
    private fun updateSummary(revenueList: List<RevenueModel>) {
        val totalRevenue = revenueList.sumOf { it.totalRevenue }
        val totalOrders = revenueList.sumOf { it.orderCount }
        val averageOrderValue = if (totalOrders > 0) totalRevenue / totalOrders else 0.0

        binding.tvTotalRevenue.text = String.format("%,.0f $", totalRevenue)
        binding.tvTotalOrders.text = totalOrders.toString()

    }

    private fun observeData() {
        viewModel.revenueData.observe(this) { revenueList ->
            updateChart(revenueList)
            updateSummary(revenueList)
            // Cập nhật marker view với danh sách mới
            binding.lineChart.marker = CustomMarkerView(this, R.layout.marker_view, revenueList)
        }
    }
}