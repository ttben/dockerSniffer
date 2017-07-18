package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ADDCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ENVCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.FROMCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class _12AddDiscouraged extends Check<Dockerfile, List<Command>>{
    public static final int THRESHOLD = 2;

    public static List<Command> conflict(Dockerfile dockerfile) {
        int nbOfAddCommand = dockerfile.howMuch(ADDCommand.class);
        if (nbOfAddCommand < THRESHOLD) {
            return new ArrayList<>();
        }

        return  dockerfile.getActions()
                .stream()
                .filter(c -> c instanceof ADDCommand)
                .map(ADDCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Map<Dockerfile, List<Command>> apply(List<Dockerfile> dockerfiles) {
        Map<Dockerfile, List<Command>> result = new HashMap<>();

        for (Dockerfile dockerfile : dockerfiles) {
            List<Command> conflict = conflict(dockerfile);
            if (!conflict.isEmpty()) {
                result.put(dockerfile, conflict);
            }
        }

        return result;
    }
}
