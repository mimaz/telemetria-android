package pl.poznan.putmotorsport.telemetria.android;

import android.content.Context;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;

import putmotorsport.poznan.pl.telemetria.android.R;

class TcpClient implements AbstractTcpClient {
    private static final int[] ids;

    private final Map<Integer, Data> dataMap;
    private final Random random;
    private final IdGetter getter;
    private Context context;
    private String address;
    private Socket socket;
    private int port;

    public interface IdGetter {
        int[] getIds();
    }

    TcpClient(Context context, String address, int port, IdGetter getter) {
        this.context = context;
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
    public void close() {
        try {
            socket.close();
        } catch (NullPointerException | IOException e) {}
    }

    void fetch() throws IOException {
        int[] ids = getter.getIds();
        Map<Integer, Data> map = new TreeMap<>();

        synchronized (dataMap) {
            for (int id : ids) {
                Data data = dataMap.get(id);

                map.put(id, data);
            }
        }


        if (socket == null || socket.isClosed()) {
            Log.d("TAG", "opening new socket");
            socket = new Socket(address, port);
        }


        int maxCount = context.getResources().getInteger(R.integer.chart_width);


        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        dos.writeInt(1); // get data command
        dos.writeInt(maxCount);
        dos.writeInt(map.size());

        for (Map.Entry<Integer, Data> entry : map.entrySet()) {
            dos.writeInt(entry.getKey());
            dos.writeInt(entry.getValue().since);
        }

        for (Map.Entry<Integer, Data> entry : map.entrySet()) {
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


                while (data.queue.size() > maxCount)
                    data.poll();
            }

            for (int value : values)
                data.push(value);
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
