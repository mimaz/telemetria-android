package pl.poznan.putmotorsport.telemetria.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

@Deprecated
class FakeTcpClient implements AbstractTcpClient {
    private final Random random;
    private final Map<Integer, State> stateMap;

    FakeTcpClient() {
        random = new Random();
        stateMap = new TreeMap<>();
    }

    @Override
    public List<Integer> getNewData(int id) {
        if (!stateMap.containsKey(id))
            stateMap.put(id, new State());

        return stateMap.get(id).newData();
    }

    private class State {
        private float value;
        private float speed;
        private float accel;

        State() {
            value = 100;
            speed = 0;
            accel = 0;
        }

        List<Integer> newData() {
            List<Integer> list = new ArrayList<>();

            accel += (random.nextFloat() - 0.5f) * 50f;
            speed += accel;
            value += speed;

            list.add(Math.round(value));

            return list;
        }
    }
}
