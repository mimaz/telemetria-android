package putmotorsport.poznan.pl.telemetria.android;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

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
        ChartFragmentAdapter adapter = new ChartFragmentAdapter(fm);

        pager = findViewById(R.id.pager);
        pager.setAdapter(adapter);

        client = new TcpClient("192.168.137.239", 8080);
        client.addId(90);
        client.addId(91);
        client.addId(41);
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
                        Thread.sleep(100);
                        client.fetch();
                    } catch (IOException e) {
                        Log.e("TAG", "fetching failed!!!");
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
