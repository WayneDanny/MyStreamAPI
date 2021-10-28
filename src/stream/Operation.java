package stream;

import stream.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

public class Operation<IN, OUT> extends AbstractPipeline<IN, OUT> {


    public Operation(Collection<OUT> source) {
        super(source);
    }

    public Operation(AbstractPipeline<?, IN> upstream) {
        super(upstream);
    }

    @Override
    public <R> IStream<R> map(Function<OUT, R> mapper) {
        return new Operation<OUT, R>(this) {
            @Override
            ISink<OUT> onWrapSink(ISink<R> downstreamSink) {
//                return val -> {
//                    R r = mapper.apply(val);
//                    downstreamSink.accept(r);
//                };
                return new ISink.ChainedSink<>(downstreamSink) {
                    @Override
                    public void accept(OUT val) {
                        R r = mapper.apply(val);
                        downstreamSink.accept(r);
                    }
                };
            }
        };
    }
    //  x -> x < 5 pass / x >= fail
    @Override
    public IStream<OUT> filter(Predicate<OUT> predicate) {
        return new Operation<OUT, OUT>(this) {
            @Override
            ISink<OUT> onWrapSink(ISink<OUT> downstreamSink) {
                return new ISink.ChainedSink<>(downstreamSink) {
                    @Override
                    public void accept(OUT val) {
                        if(predicate.test(val)) {
                            downstreamSink.accept(val);
                        }
                    }
                };
            }
        };
    }

    @Override
    ISink<IN> onWrapSink(ISink<OUT> sink) {
        throw new UnsupportedOperationException("need to be implemented");
    }


    @Override
    public IStream<OUT> sorted(Comparator<OUT> cpt) {
        return new Operation<OUT, OUT>(this) {
            @Override
            ISink<OUT> onWrapSink(ISink<OUT> downstreamSink) {
                return new SortedSink<OUT>(downstreamSink, cpt);
            }
        };
    }

    // distinct
    @Override
    public IStream<OUT> distinct() {
        return new Operation<OUT, OUT>(this) {
            @Override
            ISink<OUT> onWrapSink(ISink<OUT> downstreamSink) {
                return new DistinctSink<OUT>(downstreamSink);
            }
        };
    }
}