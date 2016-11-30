package fr.unice.i3s.sparks.docker.core.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EXPOSECommand extends Command {
    private List body;

    public EXPOSECommand(String... body) {
        this.body = new ArrayList<>(Arrays.asList(body));
    }
}
