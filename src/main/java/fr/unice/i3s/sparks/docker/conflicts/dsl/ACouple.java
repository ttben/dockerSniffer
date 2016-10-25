package fr.unice.i3s.sparks.docker.conflicts.dsl;

import one.util.streamex.StreamEx;

import java.util.function.Function;
import java.util.stream.Stream;

public class ACouple extends Filter {

    public ACouple() {
        Function<Stream, Stream> function = stream -> {
            Stream<Pair> pairs = StreamEx.of(stream).pairMap(Pair::new);
            return pairs;
        };

        functions.add(function);
    }

    public That that() {
        That that = new That(this);
        return that;
    }
}
