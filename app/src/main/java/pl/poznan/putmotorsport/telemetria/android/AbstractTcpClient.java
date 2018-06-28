package pl.poznan.putmotorsport.telemetria.android;

import java.io.Closeable;
import java.util.List;

public interface AbstractTcpClient extends Closeable {
    List<Integer> getNewData(int id);
}
