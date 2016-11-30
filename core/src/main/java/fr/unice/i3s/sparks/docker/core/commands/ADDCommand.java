package fr.unice.i3s.sparks.docker.core.commands;

import fr.unice.i3s.sparks.docker.core.commands.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ADDCommand extends Command {
    private final List body;

    public ADDCommand(String... body) {
        this.body = new ArrayList<>(Arrays.asList(body));
    }
}
