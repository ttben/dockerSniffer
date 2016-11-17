package fr.unice.i3s.sparks.docker;

import fr.unice.i3s.sparks.docker.conflicts.commands.ShellCommand;

import java.util.List;

public class Update extends ShellCommand {

    public Update(List<String> body) {
        super(body);
    }
}
