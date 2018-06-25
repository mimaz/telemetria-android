package putmotorsport.poznan.pl.telemetria.android;

import android.app.Application;

public class ChartApplication extends Application {
    AbstractTcpClient getTcpClient() {
        return new FakeTcpClient();
    }
}
