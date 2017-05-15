package fr.unice.i3s.sparks.docker.core.commands;

import fr.unice.i3s.sparks.docker.core.model.Dockerfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CMDCommand extends Command{

    private final Dockerfile parent;
    private final List<String> body;

    public CMDCommand(Dockerfile parent, String... body) {
        this.parent = parent;
        this.body = new ArrayList<>(Arrays.asList(body));
    }

    public List<String> getBody() {
        return body;
    }

    public Dockerfile getParent() {
        return parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CMDCommand that = (CMDCommand) o;

        return body != null ? body.equals(that.body) : that.body == null;
    }

    @Override
    public int hashCode() {
        return body != null ? body.hashCode() : 0;
    }
}
