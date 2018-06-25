package putmotorsport.poznan.pl.telemetria.android;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Iterator;

class LineData {
    private static final String COUNT_KEY = "count";
    private static final String VALUES_KEY = "values";
    private static final int MAX_COUNT = 500;

    private String name;
    private LineGraphSeries<DataPoint> series;
    private int count;

    LineData(LineDescription desc) {
        name = desc.name;
        series = new LineGraphSeries<>();
        count = 0;

        series.setAnimated(false);
        series.setDrawBackground(true);
        series.setDrawAsPath(true);
        series.setThickness(5);
        series.setTitle(name);

        int red = Color.red(desc.color);
        int green = Color.green(desc.color);
        int blue = Color.blue(desc.color);

        int primary = Color.argb(255, red, green, blue);
        int secondary = Color.argb(50, red, green, blue);

        series.setColor(primary);
        series.setBackgroundColor(secondary);
    }

    void saveSeries(Bundle bundle) {
        double min = series.getLowestValueX() - 1;
        double max = series.getHighestValueX() + 1;

        Iterator<DataPoint> iter = series.getValues(min, max);
        ArrayList<Integer> values = new ArrayList<>();

        while (iter.hasNext())
            values.add((int) Math.round(iter.next().getY()));

        bundle.putInt(COUNT_KEY, count);
        bundle.putIntegerArrayList(VALUES_KEY, values);
    }

    void restoreSeries(Bundle bundle) {
        ArrayList<Integer> values = bundle.getIntegerArrayList(VALUES_KEY);
        count = bundle.getInt(COUNT_KEY) - values.size();

        for (int val : values)
            addValue(val);
    }

    void addValue(int value) {
        DataPoint point = new DataPoint(count++, value);

        series.appendData(point, true, MAX_COUNT);
    }

    LineGraphSeries<DataPoint> getSeries() {
        return series;
    }
}
