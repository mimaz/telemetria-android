package putmotorsport.poznan.pl.telemetria.android;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class FragmentAdapter extends FragmentStatePagerAdapter {
    FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return ChartFragment.newInstance(makeKmhSpeedDesc());

            case 1:
                return new ControlFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    private ChartDescription makeKmhSpeedDesc() {
        return new ChartDescription(
                "prędkość km/h",
                0, 150,
                new LineDescription(10, "lewe tył", Color.RED),
                new LineDescription(11, "prawe tył", Color.YELLOW)
        );
    }
}
