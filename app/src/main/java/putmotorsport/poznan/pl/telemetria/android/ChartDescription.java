package putmotorsport.poznan.pl.telemetria.android;

import android.os.Bundle;

import java.util.ArrayList;

class ChartDescription {
    private static final String TITLE_KEY = "title";
    private static final String MIN_KEY = "min";
    private static final String MAX_KEY = "max";
    private static final String COUNT_KEY = "count";
    private static final String IDS_KEY = "ids";
    private static final String NAMES_KEY = "names";
    private static final String COLORS_KEY = "colors";

    final String title;
    final int min;
    final int max;
    final LineDescription[] lines;

    ChartDescription(String title,
                     int min,
                     int max,
                     LineDescription... lines) {
        this.title = title;
        this.min = min;
        this.max = max;
        this.lines = lines;
    }

    ChartDescription(Bundle bundle) {
        title = bundle.getString(TITLE_KEY);
        min = bundle.getInt(MIN_KEY);
        max = bundle.getInt(MAX_KEY);

        int count = bundle.getInt(COUNT_KEY);

        lines = new LineDescription[count];

        ArrayList<Integer> ids = bundle.getIntegerArrayList(IDS_KEY);
        ArrayList<String> names = bundle.getStringArrayList(NAMES_KEY);
        ArrayList<Integer> colors = bundle.getIntegerArrayList(COLORS_KEY);

        for (int i = 0; i < count; i++) {
            lines[i] = new LineDescription(
                    ids.get(i), names.get(i), colors.get(i)
            );
        }
    }

    Bundle toBundle() {
        Bundle bundle = new Bundle();

        bundle.putString(TITLE_KEY, title);
        bundle.putInt(MIN_KEY, min);
        bundle.putInt(MAX_KEY, max);
        bundle.putInt(COUNT_KEY, lines.length);

        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        for (LineDescription line : lines) {
            ids.add(line.id);
            names.add(line.name);
            colors.add(line.color);
        }

        bundle.putIntegerArrayList(IDS_KEY, ids);
        bundle.putStringArrayList(NAMES_KEY, names);
        bundle.putIntegerArrayList(COLORS_KEY, colors);

        return bundle;
    }
}
