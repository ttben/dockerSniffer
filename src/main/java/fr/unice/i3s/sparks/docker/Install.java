package fr.unice.i3s.sparks.docker;

import edu.emory.mathcs.backport.java.util.Arrays;
import fr.unice.i3s.sparks.docker.conflicts.commands.ShellCommand;

import java.util.ArrayList;
import java.util.List;

public class Install extends ShellCommand {

    public Install(List<String> body) {
        super(body);
    }

    public Install(String... body) {
        super(Arrays.asList(body));
    }
}
