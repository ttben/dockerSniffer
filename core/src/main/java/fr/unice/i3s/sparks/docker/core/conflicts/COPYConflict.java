package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.commands.COPYCommand;
import fr.unice.i3s.sparks.docker.core.commands.Command;
import fr.unice.i3s.sparks.docker.core.conflicts.run.RUNConflict;
import fr.unice.i3s.sparks.docker.core.model.Dockerfile;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class COPYConflict {
    public void conflict(Dockerfile dockerFiles) {
        ArrayList<Command> collect = dockerFiles.getListOfCommand()
                .stream().filter(c -> c instanceof COPYCommand).collect(Collectors.toCollection(ArrayList::new));
        System.out.println(collect.size() + " copy command found!");

    }
}
