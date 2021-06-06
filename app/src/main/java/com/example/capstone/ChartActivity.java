package com.example.capstone;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.net.wifi.p2p.nsd.WifiP2pServiceRequest.newInstance;


public class ChartActivity extends AppCompatActivity {
    //private ActivityChartBinding binding;
    FragmentPagerAdapter adapterViewPager;
    Intent intent;
    static ArrayList<Float> l1;
    static String m1, m2, m3;
    float avg0,avg1,avg2,avg3,avg4,avg5,avg6,avg7,avg8,avg9,avg10
            ,avg11,avg12,avg13,avg14,avg15,avg16,avg17;
    float max;
    Button button[] = new Button[18];
    ArrayList<BarEntry> visitors;
    String[] lab;
    Integer[] Rid_button = {
            R.id.button1,R.id.button2,R.id.button3,R.id.button4,R.id.button5,R.id.button6,R.id.button7,R.id.button8,R.id.button9,R.id.button10,
            R.id.button11,R.id.button12,R.id.button13,R.id.button14,R.id.button15,R.id.button16,R.id.button17,R.id.button18
    };
    float[] avg = {
            avg0,avg1,avg2,avg3,avg4,avg5,avg6,avg7,avg8,avg9,avg10
            ,avg11,avg12,avg13,avg14,avg15,avg16,avg17
    };
    LimitLine ll;
    private BarChart barchart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        barchart = findViewById(R.id.barchart);
        intent = getIntent();
        l1 = (ArrayList<Float>) intent.getSerializableExtra("l1");
        m1 = intent.getStringExtra("m1");
        m2 = intent.getStringExtra("m2");
        m3 = intent.getStringExtra("m3");

        ArrayList<String> sel = (ArrayList<String>) intent.getSerializableExtra("sel");
        ArrayList<String> chart = (ArrayList<String>) intent.getSerializableExtra("chart"); // 0부터 짝수는 구이름 홀수는 sel된 값


        for(int i=0;i<18;i++){
            button[i] = (Button)findViewById(Rid_button[i]);
        }
        for(int i=0; i<sel.size();i++){
            final int INDEX;
            INDEX = i;
            button[INDEX].setVisibility(View.VISIBLE);
            button[INDEX].setText(sel.get(INDEX));
            button[INDEX].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lab = new String[]{chart.get((INDEX*50)),chart.get((INDEX*50)+2),chart.get((INDEX*50)+4),chart.get((INDEX*50)+6),chart.get((INDEX*50)+8),chart.get((INDEX*50)+10),chart.get((INDEX*50)+12)};
                    visitors.set(0,new BarEntry(0,Integer.parseInt(chart.get((INDEX*50)+1))));
                    visitors.set(1,new BarEntry(1,Integer.parseInt(chart.get((INDEX*50)+3))));
                    visitors.set(2,new BarEntry(2,Integer.parseInt(chart.get((INDEX*50)+5))));
                    visitors.set(3,new BarEntry(3,Integer.parseInt(chart.get((INDEX*50)+7))));
                    visitors.set(4,new BarEntry(4,Integer.parseInt(chart.get((INDEX*50)+9))));
                    visitors.set(5,new BarEntry(5,Integer.parseInt(chart.get((INDEX*50)+11))));
                    visitors.set(6,new BarEntry(6,Integer.parseInt(chart.get((INDEX*50)+13))));
                    ll = new LimitLine(avg[INDEX], "평균 선");
                    max = avg[INDEX];
                    setData();

                }
            });
        }
        lab = new String[]{chart.get(0),chart.get(2),chart.get(4),chart.get(6),chart.get(8),chart.get(10),chart.get(12)};

        visitors = new ArrayList<>();
        visitors.add(new BarEntry(0,Integer.parseInt(chart.get(1))));
        visitors.add(new BarEntry(1,Integer.parseInt(chart.get(3))));
        visitors.add(new BarEntry(2,Integer.parseInt(chart.get(5))));
        visitors.add(new BarEntry(3,Integer.parseInt(chart.get(7))));
        visitors.add(new BarEntry(4,Integer.parseInt(chart.get(9))));
        visitors.add(new BarEntry(5,Integer.parseInt(chart.get(11))));
        visitors.add(new BarEntry(6,Integer.parseInt(chart.get(13))));

        for(int j=0; j<sel.size(); j++) {
            for (int i = (50*j) + 1; i < (50*j) + 50; i = i + 2) {
                avg[j] = avg[j] + Integer.parseInt(chart.get(i));
            }
        }
        for(int j=0; j<sel.size(); j++) {
            avg[j] = avg[j] / 25;
        }
        max = avg[0];
        ll = new LimitLine(avg[0], "평균 선");
        setData();

        /*
        radarChart.getDescription().setEnabled(false);
        radarChart.setWebLineWidth(1f);
        radarChart.setWebColor(Color.BLACK);
        radarChart.setWebLineWidth(1f);
        radarChart.setWebColorInner(Color.BLACK);
        radarChart.setWebAlpha(200);

        //setData();

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

         */
    }
    private void setData(){
        // BarChart
        XAxis xAxis = barchart.getXAxis();  // barChart의 X축
        YAxis yLAxis = barchart.getAxisLeft();  // barChart의 Left_Y축
        YAxis yRAxis = barchart.getAxisRight();  // barChart의 Right_Y축
        barchart.invalidate();
        yLAxis.removeAllLimitLines();
        BarDataSet barDataSet = new BarDataSet(visitors,"Visiters");
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(11);


        BarData barData = new BarData(barDataSet);

        ll.setLineWidth(10f);
        barchart = findViewById(R.id.barchart);
        barchart.setFitBars(true);



// Y축 오른쪽 비활성화
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

// Y축 왼쪽 설정
        yLAxis.setAxisMaximum(max*3);  // Y축의 최댓값을 정해준다.
        yLAxis.setAxisMinimum(0f);  // Y축의 최솟값을 정해준다.
        yLAxis.addLimitLine(ll);
// X축 설정
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE); // x값 표시 위치
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false); // x축 GridLinexAxis.setDrawAxisLine(false);
        xAxis.setTextSize(11f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(lab)); // 그래프 Y축 포맷 변경
        barchart.getDescription().setEnabled(false); // 그래프 제목 삭제
        barchart.getLegend().setDrawInside(false);  // 범례 삭제
        barchart.getLegend().setEnabled(false); // 그래프 범례 삭제
// 그래프 zoom 애니메이션
        barchart.setPinchZoom(false);
        barchart.setScaleEnabled(false);
        barchart.setDoubleTapToZoomEnabled(false);
        barchart.animateY(500); // 그래프 애니메이션
        barchart.setData(barData);





    }
    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 :
                    return FirstFragment.newInstance(m1,l1.get(0),l1.get(1),l1.get(2),l1.get(3),l1.get(4));
                case 1:
                    return SecondFragment.newInstance(m2,l1.get(5),l1.get(6),l1.get(7),l1.get(8),l1.get(9));
                case 2:
                    return ThirdFragment.newInstance(m3,l1.get(10),l1.get(11),l1.get(12),l1.get(13),l1.get(14));
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }



}
