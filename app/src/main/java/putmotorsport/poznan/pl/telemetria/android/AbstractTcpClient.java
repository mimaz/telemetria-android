package putmotorsport.poznan.pl.telemetria.android;

import java.util.List;

public interface AbstractTcpClient {
    List<Integer> getNewData(int id);
}
