package fr.unice.i3s.sparks.docker.core.commands;

import java.util.Arrays;
import java.util.List;

public class Install extends ShellCommand {

    public Install(List<String> body) {
        super(body);
    }

    public Install(String... body) {
        super(Arrays.asList(body));
    }
}
