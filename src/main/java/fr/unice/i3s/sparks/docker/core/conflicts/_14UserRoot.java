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

public class _14UserRoot extends Check<Dockerfile, List<Command>> {
    public static List<Command> conflict(Dockerfile dockerfile) {
        List<Command> result = new ArrayList<>();

        ArrayList<USERCommand> addCommands = dockerfile.getActions()
                .stream()
                .filter(c -> c instanceof USERCommand)
                .map(USERCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));

        for (USERCommand userCommand : addCommands) {
            String body = userCommand.getBody();
            if (body.trim().toLowerCase().contains("root")) {
                result.add(userCommand);
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
