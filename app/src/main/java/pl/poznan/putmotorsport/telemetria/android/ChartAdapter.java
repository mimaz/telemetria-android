package pl.poznan.putmotorsport.telemetria.android;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Map;
import java.util.TreeMap;

class ChartAdapter extends FragmentStatePagerAdapter {
    private static final ChartDescription[] descriptions;

    private final Map<Integer, ChartFragment> fragmentMap;

    ChartAdapter(FragmentManager fm) {
        super(fm);

        fragmentMap = new TreeMap<>();
    }

    @Override
    public Fragment getItem(int i) {
        if (!fragmentMap.containsKey(i)) {
            ChartDescription desc = descriptions[i];
            fragmentMap.put(i, ChartFragment.newInstance(desc));
        }

        return fragmentMap.get(i);
    }

    @Override
    public int getCount() {
        return descriptions.length;
    }

    static {
        descriptions = new ChartDescription[] {
                new ChartDescription(
                        "prędkość km/h",
                        0, 150,
                        new LineDescription(
                                90, "lewe tył", Color.RED
                        ),
                        new LineDescription(
                                91, "prawe tył", Color.YELLOW
                        ),
                        new LineDescription(
                                92, "lewe przód", Color.BLUE
                        ),
                        new LineDescription(
                                93, "prawe przód", Color.GREEN
                        )
                ),
                new ChartDescription(
                        "prędkość Hz",
                        0, 500,
                        new LineDescription(
                                20, "lewe tył", Color.RED
                        ),
                        new LineDescription(
                                21, "prawe tył", Color.YELLOW
                        ),
                        new LineDescription(
                                22, "lewe przód", Color.BLUE
                        ),
                        new LineDescription(
                                23, "prawe przód", Color.GREEN
                        )
                ),
                new ChartDescription(
                        "TPS",
                        0, 100,
                        new LineDescription(
                                41, "wartość %", Color.WHITE
                        )
                ),
                new ChartDescription(
                        "random",
                        0, 50,
                        new LineDescription(
                                0, "wartość", Color.WHITE
                        )
                ),
                new ChartDescription(
                        "ugięcie amortyzatorów",
                        0, 1023,
                        new LineDescription(
                                20, "lewy tył", Color.RED
                        ),
                        new LineDescription(
                                21, "prawy tył", Color.YELLOW
                        ),
                        new LineDescription(
                                22, "lewy przód", Color.BLUE
                        ),
                        new LineDescription(
                                23, "prawy przód", Color.GREEN
                        )
                ),
                new ChartDescription(
                        "położenie kierownicy",
                        -100, 100,
                        new LineDescription(
                                30, "kąt skrętu", Color.WHITE
                        )
                )
        };
    }
}
