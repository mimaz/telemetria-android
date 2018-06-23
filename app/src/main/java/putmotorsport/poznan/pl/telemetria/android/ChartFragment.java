package putmotorsport.poznan.pl.telemetria.android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class ChartFragment extends Fragment {
    private static final String ID_ARRAY_KEY = "id_array";

    private GraphView graph;
    private int[] idlist;
    private Map<Integer, ChartData> datamap;

    public static ChartFragment newInstance(int[] ids) {
        ChartFragment fragment = new ChartFragment();

        Bundle bundle = new Bundle();
        bundle.putIntArray(ID_ARRAY_KEY, ids);

        fragment.setArguments(bundle);

        return fragment;
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

        graph = view.findViewById(R.id.graph);
        idlist = getArguments().getIntArray(ID_ARRAY_KEY);
        datamap = new TreeMap<>();
    }

    public void update() {
        MainActivity activity = (MainActivity) getActivity();
        TcpClient client = activity.getTcpClient();

        for (int id : idlist) {
            if (!datamap.containsKey(id))
                datamap.put(id, new ChartData());

            datamap.get(id).update(client, id);
        }
    }

    private class ChartData {
        LineGraphSeries<DataPoint> series;
        int since;
        int count;

        ChartData() {
            series = new LineGraphSeries<>();
            since = 0;
            count = 0;

            graph.addSeries(series);
        }

        void update(TcpClient client, int id) {
            since = client.getData(since, 20, id, new Consumer<Integer>() {
                @Override
                public void accept(Integer v) {
                    Log.d("TAG", "point: " + count + ":" + v);

                    DataPoint point = new DataPoint(count++, v);

                    series.appendData(point, true, 20);
                }
            });

            Log.d("TAG", "update: " + since);
        }
    }
}
