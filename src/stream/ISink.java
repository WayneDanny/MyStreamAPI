package stream;

@FunctionalInterface
public interface ISink<T> {
    default void begin(long size) { }
    default void end(){ };
    default boolean cancellation() { return false; };

    void accept(T t);

    abstract static class ChainedSink<T, R> implements ISink<T> {
        private final ISink<R> downstream;

        public ChainedSink(ISink<R> downstream) {
            this.downstream = downstream;
        }

        @Override
        public void begin(long size) {
            downstream.begin(size);
        }

        @Override
        public void end() {
            downstream.end();
        }
    }


    abstract static class Box<T, E> implements ISink<T> {
        E state;
        public E get() {
            return state;
        }
    }
}