package fr.unice.i3s.sparks.docker.conflicts.dsl;

public abstract class Have extends Filter {
    public Have() {
    }
    public static That and() {
        return new That();
    }
}
