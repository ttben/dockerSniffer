package fr.unice.i3s.sparks.docker;

import edu.emory.mathcs.backport.java.util.Arrays;
import fr.unice.i3s.sparks.docker.conflicts.commands.ShellCommand;

import java.util.List;

public class Update extends ShellCommand {

    public Update(List<String> body) {
        super(body);
    }

    public Update(String... body) {
        super(Arrays.asList(body));
    }
}
