package fr.unice.i3s.sparks.docker.conflicts.dsl;

import java.util.function.Function;
import java.util.stream.Stream;

public class HaveTypeOf extends Have {
    public HaveTypeOf(Class clazz) {
        Function<Stream, Stream> function =
                stream -> stream.filter(c -> c.getClass().equals(clazz));

        functions.add(function);
    }
}
