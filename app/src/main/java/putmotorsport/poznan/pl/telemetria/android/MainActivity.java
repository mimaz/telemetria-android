package putmotorsport.poznan.pl.telemetria.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

class MainActivity extends AppCompatActivity {
    private ViewPager pager;
    private TcpClient client;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        FragmentAdapter adapter = new FragmentAdapter(fm);

        pager = findViewById(R.id.pager);
        pager.setAdapter(adapter);

        client = new TcpClient();
        timer = new Timer();
    }

    @Override
    protected void onStart() {
        super.onStart();

        final FragmentManager fm = getSupportFragmentManager();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Fragment frag : fm.getFragments())
                    if (frag instanceof ChartFragment)
                        ((ChartFragment) frag).update();
            }
        }, 1000, 100);
    }

    @Override
    protected void onStop() {
        timer.cancel();

        super.onStop();
    }

    TcpClient getTcpClient() {
        return client;
    }
}
