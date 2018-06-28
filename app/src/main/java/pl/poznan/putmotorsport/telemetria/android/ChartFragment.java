package pl.poznan.putmotorsport.telemetria.android;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import putmotorsport.poznan.pl.telemetria.android.R;

public class ChartFragment extends Fragment {
    private static final int UPDATE_INTERVAL = 100;

    private final Map<Integer, LineData> dataMap;
    private final Handler handler;
    private final Runnable updater;

    public static ChartFragment newInstance(ChartDescription desc) {
        ChartFragment fragment = new ChartFragment();

        fragment.setArguments(desc.toBundle());

        return fragment;
    }

    public ChartFragment() {
        dataMap = new TreeMap<>();
        handler = new Handler();
        updater = new Runnable() {
            @Override
            public void run() {
                AbstractTcpClient client = getChartActivity().getTcpClient();

                for (int id : dataMap.keySet()) {
                    List<Integer> newData = client.getNewData(id);

                    for (int value : newData)
                        dataMap.get(id).addValue(value);
                }

                handler.postDelayed(this, UPDATE_INTERVAL);
            }
        };
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

        GraphView chart = view.findViewById(R.id.chart);

        Bundle args = getArguments();

        assert args != null;

        ChartDescription desc = new ChartDescription(args);

        chart.setTitle(desc.title);

        LegendRenderer lr = chart.getLegendRenderer();
        lr.setVisible(true);
        lr.setFixedPosition(0, 0);

        GridLabelRenderer glr = chart.getGridLabelRenderer();
        glr.setHorizontalLabelsVisible(false);
        glr.setVerticalLabelsVisible(true);

        chart.setCursorMode(true);

        chart.getViewport().setXAxisBoundsManual(true);
        chart.getViewport().setMinX(0);
        chart.getViewport().setMaxX(100);

        chart.getViewport().setYAxisBoundsManual(true);
        chart.getViewport().setMinY(desc.min);
        chart.getViewport().setMaxY(desc.max);

        for (LineDescription line : desc.lines) {
            LineData data = new LineData(line);

            dataMap.put(line.id, data);
            chart.addSeries(data.getSeries());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            for (int id : dataMap.keySet()) {
                LineData data = dataMap.get(id);

                String key = lineKey(id);

                if (savedInstanceState.containsKey(key)) {
                    Bundle bundle = savedInstanceState.getBundle(key);
                    data.restoreSeries(bundle);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        for (int id : dataMap.keySet()) {
            LineData data = dataMap.get(id);
            Bundle bundle = new Bundle();

            data.saveSeries(bundle);

            String key = lineKey(id);

            outState.putBundle(key, bundle);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();

        handler.post(updater);
    }

    @Override
    public void onStop() {
        handler.removeCallbacks(updater);

        super.onStop();
    }

    private ChartActivity getChartActivity() {
        return (ChartActivity) getActivity();
    }

    private String lineKey(int id) {
        return "line_" + id;
    }
}
