package fr.unice.i3s.sparks.docker.conflicts.dsl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Filter {
    protected LinkedList<Function> functions = new LinkedList<>();

    public List<Function> getFunctions() {
        return functions;
    }

    public Object applyOn(Collection collection) {
        for (int i = 0; i < functions.size() - 1; i++) {
            functions.get(i).andThen(functions.get(i + 1));
        }

        System.out.println(functions);

        Object apply = functions.get(0).apply(collection.stream());
        Stream stream = (Stream) apply;
        Object collect = stream.collect(Collectors.toSet());
        System.out.println(collect);
        return collect;
    }


}