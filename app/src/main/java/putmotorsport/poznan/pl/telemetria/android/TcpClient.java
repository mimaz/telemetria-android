package putmotorsport.poznan.pl.telemetria.android;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TcpClient {
    private static final int COMMAND_DATA = 1;
    private static final int RESULT_SUCCESS = 10;

    private String address;
    private int port;

    public TcpClient() {
        address = "192.168.1.21";
        port = 8080;
    }

    public int getData(int since, int count, int id,
                       Consumer<Integer> consumer) {
        try (Socket socket = new Socket(address, port)) {

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            dos.writeInt(COMMAND_DATA);
            dos.writeInt(since);
            dos.writeInt(count);
            dos.writeInt(id);

            int stat = dis.readInt();

            if (stat != RESULT_SUCCESS) {
                Log.e("TAG", "request failed!!!");
                return since;
            }

            Log.d("TAG", "result succed");

            since = dis.readInt();
            count = dis.readInt();

            while (count-- > 0) {
                dis.readInt();

                consumer.accept(dis.readInt());
            }

            dos.writeInt(0);

            Log.d("TAG", "data read");

            return since;
        } catch (IOException e) {
            Log.e("TAG", "opening socket failed: " + e);
            return since;
        }
    }
}
