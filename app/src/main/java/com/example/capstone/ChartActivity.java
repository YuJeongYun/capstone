package com.example.capstone;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;



public class ChartActivity extends AppCompatActivity {
    //private ActivityChartBinding binding;
    public static final float MAX = 15, MIN =1;
    private RadarChart radarChart;
    Intent intent;
    static ArrayList<Float> l1;
    static ArrayList<Float> avg;
    static String m1, m2, m3;
    private BarChart barchart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        radarChart = findViewById(R.id.radarChart);
        barchart = findViewById(R.id.barchart);
        intent =getIntent();
        l1 = (ArrayList<Float>) intent.getSerializableExtra("l1");
        avg = (ArrayList<Float>) intent.getSerializableExtra("avg");
        m1 = intent.getStringExtra("m1");
        m2 = intent.getStringExtra("m2");
        m3 = intent.getStringExtra("m3");

        ArrayList<BarEntry> visitors = new ArrayList<>();
        visitors.add(new BarEntry(2014,429));
        visitors.add(new BarEntry(2015,475));
        visitors.add(new BarEntry(2016,508));
        visitors.add(new BarEntry(2017,660));
        visitors.add(new BarEntry(2018,550));
        visitors.add(new BarEntry(2019,630));
        visitors.add(new BarEntry(2020,470));

        BarDataSet barDataSet = new BarDataSet(visitors,"Visiters");
        barDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barchart = findViewById(R.id.barchart);
        barchart.setFitBars(true);
        barchart.setData(barData);
        //binding.barchart.getDescription().setText("Bar Chart Example");
        barchart.animateY(2000);

        radarChart.getDescription().setEnabled(false);
        radarChart.setWebLineWidth(1f);
        radarChart.setWebColor(Color.BLACK);
        radarChart.setWebLineWidth(1f);
        radarChart.setWebColorInner(Color.BLACK);
        radarChart.setWebAlpha(200);

        setData();

        radarChart.animateXY(1000,1000, Easing.EasingOption.EaseInOutQuad,Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setYOffset(0);
        xAxis.setXOffset(0);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] label = new String[] {"생활·편의·교통","교육","복지 문화","자연","안전"};
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return label[(int) value % label.length];
            }
        });
        xAxis.setTextColor(Color.BLACK);

        YAxis yAxis = radarChart.getYAxis();
        yAxis.setLabelCount(5,true);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(MIN);
        yAxis.setAxisMaximum(MAX);
        yAxis.setDrawLabels(true);

        Legend l = radarChart.getLegend();
        l.setTextSize(15f);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.BLACK);


    }


    private void setData(){
        ArrayList<RadarEntry> main = new ArrayList<>();
        ArrayList<RadarEntry> val = new ArrayList<>();
        for(int i=0; i<5; i++){
            main.add(new RadarEntry(5));
            val.add(new RadarEntry((l1.get(i)/avg.get(i))*5));
        }

        RadarDataSet set1 =new RadarDataSet(main, "평균");
        set1.setValueTextSize(2f);
        set1.setValueTextColor(Color.RED);
        set1.setColor(Color.RED);
        set1.setFillColor(Color.RED);
        set1.setDrawFilled(true);
        //set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightIndicators(false);
        set1.setDrawHighlightCircleEnabled(true);


        RadarDataSet set2 =new RadarDataSet(val, m1);
        set2.setColor(Color.GREEN);
        set2.setFillColor(Color.GREEN);
        set2.setDrawFilled(true);
        //set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightIndicators(false);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setValueTextSize(2f);
        set2.setValueTextColor(Color.GREEN);


        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(sets);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.BLACK);

        radarChart.setData(data);
        radarChart.invalidate();

    }
}
