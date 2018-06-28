package pl.poznan.putmotorsport.telemetria.android;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

class TcpClient implements AbstractTcpClient {
    private static final int[] ids;

    private final Map<Integer, Data> dataMap;
    private final Random random;
    private String address;
    private int port;

    TcpClient(String address, int port) {
        this.address = address;
        this.port = port;

        dataMap = new TreeMap<>();
        random = new Random();

        for (int id : ids)
            dataMap.put(id, new Data());
    }

    @Override
    public List<Integer> getNewData(int id) {
        if (id == 0)
            return Collections.singletonList(random.nextInt(50));


        Data data;

        try {
            data = dataMap.get(id);
        } catch (NoSuchElementException e) {
            return Collections.emptyList();
        }


        List<Integer> list = new ArrayList<>();

        synchronized (data) {
            while (!data.queue.isEmpty())
                list.add(data.queue.remove());
        }

        return list;
    }

    void fetch() throws IOException {
        Socket socket = new Socket(address, port);

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        Set<Integer> ids = dataMap.keySet();

        Log.d("TAG", "id cnt: " + ids.size());

        for (int id : ids) {
            Data data;

            synchronized (dataMap) {
                try {
                    data = dataMap.get(id);
                } catch (NoSuchElementException e) {
                    continue;
                }
            }

            dos.writeInt(1); // get data command
            dos.writeInt(data.since);
            dos.writeInt(100);
            dos.writeInt(id);

            int stat = dis.readInt();

            if (stat != 10)
                throw new IOException("request failed!: " + stat);

            data.since = dis.readInt();

            int count = dis.readInt();

            Log.d("TAG", "fetch count: " + count);

            for (int i = 0; i < count; i++) {
                int tim = dis.readInt();
                int val = dis.readInt();

                synchronized (data) {
                    data.queue.add(val);
                }

                Log.d("TAG", "fetch: " + val);

                while (data.queue.size() > 1000)
                    data.queue.poll();
            }
        }

        dos.writeInt(0);

        socket.close();
    }

    private class Data {
        Deque<Integer> queue;
        int since;

        Data() {
            queue = new ArrayDeque<>();
            since = 0;
        }
    }

    static {
        List<Integer> list = new ArrayList<>();

        for (ChartDescription cd : Charts.descriptions)
            for (LineDescription ld : cd.lines)
                list.add(ld.id);

        ids = new int[list.size()];

        for (int i = 0; i < ids.length; i++)
            ids[i] = list.get(i);
    }
}
