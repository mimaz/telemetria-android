package pl.poznan.putmotorsport.telemetria.android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Map;
import java.util.TreeMap;

class ChartAdapter extends FragmentStatePagerAdapter {
    private final Map<Integer, ChartFragment> fragmentMap;

    ChartAdapter(FragmentManager fm) {
        super(fm);

        fragmentMap = new TreeMap<>();
    }

    @Override
    public Fragment getItem(int i) {
        if (!fragmentMap.containsKey(i)) {
            ChartDescription desc = Charts.descriptions[i];
            fragmentMap.put(i, ChartFragment.newInstance(desc));
        }

        return fragmentMap.get(i);
    }

    @Override
    public int getCount() {
        return Charts.descriptions.length;
    }
}
