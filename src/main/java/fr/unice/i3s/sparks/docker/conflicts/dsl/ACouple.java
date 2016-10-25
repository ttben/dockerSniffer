package fr.unice.i3s.sparks.docker.conflicts.dsl;

import one.util.streamex.StreamEx;

import java.util.function.Function;
import java.util.stream.Stream;

public class ACouple extends Filter {

    public ACouple() {
        Function<Stream, Stream> function = stream -> {
            System.out.println("Applying acouple");
            Stream<Pair> pairs = StreamEx.of(stream).pairMap(Pair::new);
            return pairs;
        };

        functions.addLast(function);
    }

    public That that() {
        That that = new That(this);
        return that;
    }
}
