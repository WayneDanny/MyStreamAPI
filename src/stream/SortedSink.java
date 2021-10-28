package stream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortedSink<T> implements ISink<T>{
    private final ISink<T> downstream;
    private List<T> list;
    private final Comparator<T> cpt;

    public SortedSink(ISink<T> downstream, Comparator<T> cpt) {
        this.downstream = downstream;
        this.cpt = cpt;
    }

    @Override
    public void begin(long size) {
        list = new ArrayList<>();
    }

    @Override
    public void end() {
        list.sort(cpt);
        downstream.begin(-1);
        list.forEach(t -> downstream.accept(t));
        downstream.end();
    }

    @Override
    public void accept(T t) {
        list.add(t);
    }
}