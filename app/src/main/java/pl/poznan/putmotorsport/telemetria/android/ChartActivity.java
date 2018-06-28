package pl.poznan.putmotorsport.telemetria.android;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import putmotorsport.poznan.pl.telemetria.android.R;

public class ChartActivity extends AppCompatActivity {
    private ViewPager pager;
    private Thread thread;
    private TcpClient client;
    private volatile boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        ChartAdapter adapter = new ChartAdapter(fm);

        pager = findViewById(R.id.pager);
        pager.setAdapter(adapter);

        String address = getResources().getString(R.string.address);
        int port = getResources().getInteger(R.integer.port);

        client = new TcpClient(address, port,
                new TcpClient.IdGetter() {
            @Override
            public int[] getIds() {
                int idx = pager.getCurrentItem();
                ChartDescription cd = Charts.descriptions[idx];

                int[] ids = new int[cd.lines.length];

                for (int i = 0; i < ids.length; i++)
                    ids[i] = cd.lines[i].id;

                return ids;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        running = true;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    try {
                        int delay = getResources()
                                .getInteger(R.integer.request_interval);

                        Thread.sleep(delay);

                        client.fetch();
                    } catch (IOException e) {
                        Log.e("TAG", "fetching failed: " + e);
                    } catch (InterruptedException e) {}
                }
            }
        });

        thread.start();
    }

    @Override
    protected void onStop() {
        running = false;

        super.onStop();
    }

    AbstractTcpClient getTcpClient() {
        return client;
    }
}
