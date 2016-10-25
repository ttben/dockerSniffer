package fr.unice.i3s.sparks.docker.conflicts.dsl;

import java.util.ArrayList;
import java.util.List;

public class That {
    private List<Have> haveList = new ArrayList<>();

    public static HaveSame haveTheSame(String attributeName) {
        return new HaveSame(attributeName);
    }

    public static HaveTypeOf haveTypeOf(Class clazz) {
        return new HaveTypeOf(clazz);
    }

    public static HaveDifferent haveDifferent(String attributeName) {
        return new HaveDifferent(attributeName);
    }
}
