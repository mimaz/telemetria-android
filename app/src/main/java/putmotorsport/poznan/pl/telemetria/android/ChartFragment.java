package putmotorsport.poznan.pl.telemetria.android;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.GraphViewXML;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ChartFragment extends Fragment {
    private final Map<Integer, LineData> dataMap;
    private final Handler handler;
    private final List<Runnable> runnables;
    private GraphView chart;

    public static ChartFragment newInstance(ChartDescription desc) {
        ChartFragment fragment = new ChartFragment();

        fragment.setArguments(desc.toBundle());

        return fragment;
    }

    public ChartFragment() {
        dataMap = new TreeMap<>();
        handler = new Handler();
        runnables = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chart_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chart = view.findViewById(R.id.chart);

        Bundle args = getArguments();

        assert args != null;

        ChartDescription desc = new ChartDescription(args);

        chart.setTitle(desc.title);

        chart.getViewport().setXAxisBoundsManual(true);
        chart.getViewport().setMinX(0);
        chart.getViewport().setMaxX(40);

        chart.getViewport().setYAxisBoundsManual(true);
        chart.getViewport().setMinY(desc.min);
        chart.getViewport().setMaxY(desc.max);

        for (LineDescription line : desc.lines) {
            LineData data = new LineData(line);

            dataMap.put(line.id, data);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        runnables.add(new Runnable() {
            @Override
            public void run() {
                AbstractTcpClient client = getChartApplication().getTcpClient();

                for (int id : dataMap.keySet()) {
                    List<Integer> newData = client.getNewData(id);

                    for (int value : newData)
                        dataMap.get(id).addValue(value);
                }

                handler.postDelayed(this, 100);
            }
        });


        for (Runnable runnable : runnables)
            handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onStop() {
        for (Runnable runnable : runnables)
            handler.removeCallbacks(runnable);

        super.onStop();
    }

    private ChartApplication getChartApplication() {
        return (ChartApplication) getContext().getApplicationContext();
    }

    private class LineData {
        private String name;
        private LineGraphSeries<DataPoint> series;
        private int count;

        LineData(LineDescription desc) {
            name = desc.name;
            series = new LineGraphSeries<>();
            count = 0;

            chart.addSeries(series);

            series.setAnimated(true);
            series.setDrawBackground(true);
            series.setDrawAsPath(true);
            series.setThickness(5);

            int red = Color.red(desc.color);
            int green = Color.green(desc.color);
            int blue = Color.blue(desc.color);

            int primary = Color.argb(255, red, green, blue);
            int secondary = Color.argb(50, red, green, blue);

            series.setColor(primary);
            series.setBackgroundColor(secondary);
        }

        void addValue(int value) {
            DataPoint point = new DataPoint(count++, value);

            series.appendData(point, true, 100);
        }
    }
}
