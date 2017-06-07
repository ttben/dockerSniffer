package fr.unice.i3s.sparks.docker.core.conflicts.copy;




import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.COPYCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class COPYConflict {
    public void conflict(Dockerfile dockerFiles) {
        ArrayList<Command> collect = dockerFiles.getListOfCommand()
                .stream().filter(c -> c instanceof COPYCommand).collect(Collectors.toCollection(ArrayList::new));
        System.out.println(collect.size() + " copy command found!");

    }
}
