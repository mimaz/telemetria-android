package pl.poznan.putmotorsport.telemetria.android;

import java.util.List;

public interface AbstractTcpClient {
    List<Integer> getNewData(int id);
}
