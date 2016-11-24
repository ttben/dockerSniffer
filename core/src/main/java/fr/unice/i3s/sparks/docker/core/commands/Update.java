package fr.unice.i3s.sparks.docker.core.commands;

import java.util.Arrays;
import java.util.List;

public class Update extends ShellCommand {

    public Update(List<String> body) {
        super(body);
    }

    public Update(String... body) {
        super(Arrays.asList(body));
    }
}
