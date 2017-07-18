package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class _16WorkdirAbsolutePath extends Check<Dockerfile, List<Command>> {
    public static List<Command> conflict(Dockerfile dockerfile) {


        ArrayList<WORKDIRCommand> workdirCommands = dockerfile.getActions()
                .stream()
                .filter(c -> c instanceof WORKDIRCommand)
                .map(WORKDIRCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));

        List<Command> result = new ArrayList<>();

        for (WORKDIRCommand workdirCommand : workdirCommands) {
            String body = workdirCommand.getBody();
            if (!body.trim().toLowerCase().startsWith("/")) {
                result.add(workdirCommand);
            }
        }

        return result;
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
