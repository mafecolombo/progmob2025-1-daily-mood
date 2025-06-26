package com.android.dailymood.ui.fragments.home

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android.dailymood.R
import com.android.dailymood.data.local.dao.AppDatabase
import com.android.dailymood.databinding.FragmentMoodChartBinding
import com.android.dailymood.utils.SessionManager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MoodChartFragment : Fragment() {

    private var _binding: FragmentMoodChartBinding? = null
    private val binding get() = _binding!!
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMoodChartBinding.inflate(inflater, container, false)

        val userId = SessionManager.getLoggedInUser(requireContext()) ?: return binding.root
        val db = AppDatabase.getDatabase(requireContext())

        lifecycleScope.launch(Dispatchers.IO) {
            val entries = db.moodDao().getMoodHistory(userId).sortedBy { it.date }

            withContext(Dispatchers.Main) {
                addChart("Histórico de Humor", entries.mapIndexed { i, e ->
                    Entry(i.toFloat(), e.moodScale.toFloat())
                }, entries.map { it.date.substring(5) })

                // Consistência: quantidade de dias seguidos com registro
                val consistencyData = calcularConsistencia(entries)
                addChart("Consistência dos Registros", consistencyData.first, consistencyData.second)
            }
        }

        return binding.root
    }

    private fun addChart(title: String, points: List<Entry>, labels: List<String>) {
        val chartCard = layoutInflater.inflate(R.layout.item_chart_card, null)
        val card = chartCard.findViewById<CardView>(R.id.cardChart)
        val titleView = chartCard.findViewById<TextView>(R.id.tvChartTitle)
        val chart = chartCard.findViewById<LineChart>(R.id.chartView)

        titleView.text = title

        val dataSet = LineDataSet(points, title).apply {
            color = Color.MAGENTA
            valueTextColor = Color.BLACK
            setCircleColor(Color.MAGENTA)
            circleRadius = 4f
            lineWidth = 2f
        }

        chart.data = LineData(dataSet)
        chart.description.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.axisLeft.textColor = Color.BLACK
        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = IndexAxisValueFormatter(labels)
            granularity = 1f
            textColor = Color.BLACK
        }

        chart.invalidate()

        // clique para expandir/recolher o gráfico
        var expanded = false
        card.setOnClickListener {
            expanded = !expanded
            val newHeight = if (expanded) 600 else 150
            resizeChart(chart, newHeight)
        }

        binding.chartContainer.addView(chartCard)
    }

    private fun resizeChart(chart: View, newHeightDp: Int) {
        val px = (newHeightDp * resources.displayMetrics.density).toInt()
        val initialHeight = chart.height
        val anim = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                val params = chart.layoutParams
                params.height = initialHeight + ((px - initialHeight) * interpolatedTime).toInt()
                chart.layoutParams = params
            }
        }
        anim.duration = 300
        chart.startAnimation(anim)
    }

    private fun calcularConsistencia(entries: List<com.android.dailymood.data.local.model.MoodEntry>): Pair<List<Entry>, List<String>> {
        if (entries.isEmpty()) return Pair(emptyList(), emptyList())

        val firstDate = dateFormat.parse(entries.first().date)!!
        val lastDate = dateFormat.parse(entries.last().date)!!
        val totalDays = ((lastDate.time - firstDate.time) / (1000 * 60 * 60 * 24)).toInt() + 1

        val dateSet = entries.map { it.date }.toSet()
        val points = mutableListOf<Entry>()
        val labels = mutableListOf<String>()

        for (i in 0 until totalDays) {
            val date = Calendar.getInstance().apply {
                time = firstDate
                add(Calendar.DAY_OF_YEAR, i)
            }
            val formatted = dateFormat.format(date.time)
            points.add(Entry(i.toFloat(), if (dateSet.contains(formatted)) 1f else 0f))
            labels.add(formatted.substring(5)) // MM-dd
        }

        return Pair(points, labels)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
