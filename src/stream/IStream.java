package stream;

import stream.ISink;
import stream.Operation;

import java.util.*;
import java.util.function.*;

/**
 *  IStream.of(col) => op1 = new Operation()
 *          .map(x -> x * 2) => op1.map(x -> x * 2) => op2 = new Operation()
 *          .map(x -> new String(x)) => op2.map(..) => op3 = new Operation()
 *          .collect() => this = op3
 *
 *   op1(level = 0) <=> op2(level = 1) <=> op3(level = 2)
 */
public interface IStream<T> {

    static <T> IStream<T> of(Collection<T> col) {
        //Head
        return new Operation<T, T>(col) {
            @Override
            ISink<T> onWrapSink(ISink<T> downstreamSink) {
                return val -> downstreamSink.accept(val);
            }
        };
    }

    <R> IStream<R> map(Function<T, R> mapper);

    IStream<T> filter(Predicate<T> predicate);

    IStream<T> sorted(Comparator<T> cpt);

    <R> R collect(Supplier<R> supplier, BiConsumer<R, T> accumulator);

    <E> E reduce(E e, BiFunction<T, E, E> operation);

    IStream<T> distinct();
}

class TestIStream {
    public static void main(String[] args) {
//        List<Integer> l = new ArrayList<>();
        List<Integer> l = new ArrayList<>(Arrays.asList(4,3,3,3,2,1,5));
        IStream<Integer> stream = IStream.of(l);

        // test map and collect
//        List<String> ans = stream.map(x -> x * 2).sorted((v1, v2) -> v1.compareTo(v2)).collect(() -> new ArrayList(), (res, val) -> res.add(val));
//        System.out.println(ans);

        // test distinct
        List<String> ans = stream.distinct().collect(() -> new ArrayList(), (res, val) -> res.add(val));
        System.out.println(ans);

//         test sorted and reduce
//        System.out.println(stream.sorted((v1, v2) -> v1.compareTo(v2)).reduce(0, (res, val) -> res + val));

    }
}
