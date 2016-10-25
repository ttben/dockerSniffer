package fr.unice.i3s.sparks.docker.conflicts.dsl;

public class Pair<T, E> {
    private T first;
    private E second;

    public Pair() {
    }

    public Pair(T first, E second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public E getSecond() {
        return second;
    }
}

