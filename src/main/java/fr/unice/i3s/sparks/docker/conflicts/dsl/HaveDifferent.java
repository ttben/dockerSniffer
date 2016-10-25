package fr.unice.i3s.sparks.docker.conflicts.dsl;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.stream.Stream;

public class HaveDifferent extends Have {
    public HaveDifferent(String attributeName) {
        Function<Stream<Pair>, Stream<Pair>> function = stream -> {
            stream = stream.filter(pair -> {
                Object first = pair.getFirst();
                Object second = pair.getSecond();

                try {
                    Field fieldFirst = first.getClass().getDeclaredField(attributeName);
                    Object firstValue = fieldFirst.get(first);

                    Field fieldSecond = second.getClass().getDeclaredField(attributeName);
                    Object secondValue = fieldSecond.get(second);

                    return !firstValue.equals(secondValue);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return false;
            });

            return stream;
        };

        functions.add(function);
    }
}
