package fr.unice.i3s.sparks.docker.conflicts.dsl;

public class That extends Filter {

    public That() {
    }

    public That(ACouple aCouple) {
        functions.addAll(aCouple.getFunctions());
    }

    public HaveSame haveTheSame(String attributeName) {
        HaveSame haveSame = new HaveSame(attributeName);
        functions.addAll(haveSame.getFunctions());
        return haveSame;
    }

    public HaveTypeOf haveTypeOf(Class clazz) {
        HaveTypeOf haveTypeOf = new HaveTypeOf(clazz);
        functions.addAll(haveTypeOf.getFunctions());
        return haveTypeOf;
    }

    public HaveDifferent haveDifferent(String attributeName) {
        HaveDifferent haveDifferent = new HaveDifferent(attributeName);
        functions.addAll(haveDifferent.getFunctions());
        return haveDifferent;
    }
}
