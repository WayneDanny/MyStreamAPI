package stream;

import java.util.HashSet;

public class DistinctSink <T> implements ISink<T>{
    private final ISink<T> downstream;
    private HashSet<T> hashSet;

    public DistinctSink(ISink<T> downstream){
        this.downstream = downstream;
    }

    @Override
    public void begin(long size) {
        hashSet = new HashSet<>();
    }

    @Override
    public void end() {
        downstream.end();
    }

    @Override
    public void accept(T t) {
        if(!hashSet.contains(t)){ // if not seen, add to set and pass to downstream
            downstream.accept(t);
            hashSet.add(t);
        }
    }
}