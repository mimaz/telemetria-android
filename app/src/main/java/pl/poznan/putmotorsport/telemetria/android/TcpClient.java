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
    private final IdGetter getter;
    private String address;
    private int port;

    public interface IdGetter {
        int[] getIds();
    }

    TcpClient(String address, int port, IdGetter getter) {
        this.address = address;
        this.port = port;
        this.getter = getter;

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

    @Override
    public void close() {}

    void fetch() throws IOException {
        int[] ids = getter.getIds();
        Map<Integer, Data> map = new TreeMap<>();

        synchronized (dataMap) {
            for (int id : ids) {
                Data data = dataMap.get(id);

                map.put(id, data);
            }
        }

        Socket socket = new Socket(address, port);

        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            dos.writeInt(1); // get data command
            dos.writeInt(100);
            dos.writeInt(map.size());

            for (Map.Entry<Integer, Data> entry : map.entrySet()) {
                dos.writeInt(entry.getKey());
                dos.writeInt(entry.getValue().since);
            }

            for (Map.Entry<Integer, Data> entry : map.entrySet()) {
                int id = entry.getKey();
                Data data = entry.getValue();

                int stat = dis.readInt();

                if (stat != 10)
                    throw new IOException("request failed!: " + stat);

                data.since = dis.readInt();

                int count = dis.readInt();
                int values[] = new int[count];

                for (int i = 0; i < count; i++) {
                    int val = dis.readShort();

                    values[i] = val;


                    Log.d("TAG", "fetch: " + val);

                    while (data.queue.size() > 1000)
                        data.poll();
                }

                for (int value : values)
                    data.push(value);

                dos.writeInt(0);
            }
        } finally {
            socket.close();
        }
    }

    private class Data {
        private Deque<Integer> queue;
        int since;

        Data() {
            queue = new ArrayDeque<>();
            since = 0;
        }

        synchronized void push(int value) {
            queue.add(value);
        }

        synchronized int poll() {
            return queue.remove();
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
