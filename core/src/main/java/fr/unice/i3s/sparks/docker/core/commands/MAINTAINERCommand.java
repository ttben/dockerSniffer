package fr.unice.i3s.sparks.docker.core.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MAINTAINERCommand extends Command {
    private List body;

    public MAINTAINERCommand(String... body) {
        this.body = new ArrayList<>(Arrays.asList(body));
    }
}
