package com.uddernetworks.bcam.output;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class LightQueue {

    private final List<Boolean> buffer = new LinkedList<>();
    private final int byteSize;
    private final long time;

    private Consumer<LinkedList<Boolean>> callback;
    private Consumer<Boolean> singleCallback;

    public LightQueue(int byteSize, long time) {
        this.byteSize = byteSize;
        this.time = time;
    }

    public void listenSingle(Consumer<Boolean> singleCallback) {
        this.singleCallback = singleCallback;
    }

    public void listenMax(Consumer<LinkedList<Boolean>> callback) {
        this.callback = callback;
    }

    public void add(ToggleState element) {
        var val = element.getTimeOn() > time;
        buffer.add(val);
        singleCallback.accept(val);

        if (buffer.size() == byteSize) {
            callback.accept(new LinkedList<>(buffer));
            buffer.clear();
        }
    }

    public void reset() {
        buffer.clear();
    }
}