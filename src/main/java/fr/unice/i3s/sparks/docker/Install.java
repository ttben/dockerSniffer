package fr.unice.i3s.sparks.docker;

import fr.unice.i3s.sparks.docker.conflicts.commands.ShellCommand;

import java.util.ArrayList;
import java.util.List;

public class Install extends ShellCommand {

    public Install(List<String> body) {
        super(body);
    }
}
