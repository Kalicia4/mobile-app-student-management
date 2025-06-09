package com.example.etudiant

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class ChartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        val barChart = findViewById<BarChart>(R.id.barChart)

        // Récupérer les données transmises depuis l'autre activité
        val moyenneClasse = intent.getFloatExtra("moyenneClasse", 0f)
        val min = intent.getFloatExtra("min", 0f)
        val max = intent.getFloatExtra("max", 0f)

        // Créer les entrées du bar chart
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, moyenneClasse))
        entries.add(BarEntry(1f, min))
        entries.add(BarEntry(2f, max))

        // Créer le dataset avec style moderne
        val barDataSet = BarDataSet(entries, "")

        // ✅ Couleurs modernes - barres blanches
        barDataSet.color = Color.parseColor("#FFA500")
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 14f
        barDataSet.valueTypeface = Typeface.DEFAULT_BOLD

        // ✅ Arrondir les coins des barres (si supporté)
        barDataSet.barBorderWidth = 0f

        // ✅ Format des valeurs avec 2 décimales
        barDataSet.valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return String.format("%.1f", value)
            }
        }

        val barData = BarData(barDataSet)
        barData.barWidth = 0.15f // ✅ Barres plus fines (réduit de 0.6f à 0.3f)

        // ✅ Configuration du graphique
        barChart.data = barData

        // ✅ Appliquer le renderer personnalisé pour les barres arrondies
        val roundedRenderer = com.example.etudiant.style.RoundedBarChartRenderer(
            barChart, barChart.animator, barChart.viewPortHandler
        )
        barChart.renderer = roundedRenderer

        // ✅ Style général du graphique
        barChart.setBackgroundColor(Color.TRANSPARENT)
        barChart.setDrawGridBackground(false)
        barChart.setDrawBorders(false)
        barChart.animateY(1000) // Animation d'entrée

        // ✅ Désactiver la description
        val description = Description()
        description.isEnabled = false
        barChart.description = description

        // ✅ Désactiver la légende
        barChart.legend.isEnabled = false

        // ✅ Configuration de l'axe X (horizontal)
        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(false)
        xAxis.gridColor = Color.parseColor("#FFA500")
        xAxis.textColor = Color.parseColor("#FFA500")
        xAxis.textSize = 12f
        xAxis.typeface = Typeface.DEFAULT_BOLD

        // ✅ Labels personnalisés pour l'axe X
        val labels = arrayOf("Moyenne", "Minimum", "Maximum")
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.granularity = 1f
        xAxis.labelCount = 3

        // ✅ Configuration de l'axe Y gauche
        val leftAxis = barChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.gridColor = Color.parseColor("#FFA500") // Grille semi-transparente
        leftAxis.setDrawAxisLine(false)
        leftAxis.textColor = Color.parseColor("#FFA500")
        leftAxis.textSize = 11f
        leftAxis.setDrawZeroLine(false)
        leftAxis.granularity = 1f

        // ✅ Désactiver l'axe Y droit
        barChart.axisRight.isEnabled = false

        // ✅ Désactiver le zoom et les interactions
        barChart.setTouchEnabled(false)
        barChart.setDragEnabled(false)
        barChart.setScaleEnabled(false)
        barChart.setPinchZoom(false)
        barChart.isDoubleTapToZoomEnabled = false

        // ✅ Marges internes
        barChart.setExtraOffsets(20f, 40f, 20f, 20f)

        // ✅ Rafraîchir le graphique
        barChart.invalidate()

        // ✅ Gérer le bouton retour
        val btnRetour = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnRetour)
        btnRetour.setOnClickListener {
            finish()
        }
    }
}