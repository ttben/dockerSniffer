package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.FROMCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class _10FromVersionPinning extends Check<Dockerfile, List<Command>>{

    public static List<Command> conflict(Dockerfile dockerfile) {
        List<Command> result = new ArrayList<>();

        ArrayList<FROMCommand> fromCommands = dockerfile.getActions()
                .stream()
                .filter(c -> c instanceof FROMCommand)
                .map(FROMCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));

        for (FROMCommand fromCommand : fromCommands) {
            String body = fromCommand.getParent().getDigest();
            String bodyLowerCase = body.trim().toLowerCase();
            if (bodyLowerCase.contains(":") && bodyLowerCase.split(":").length > 1) {   // cover ubuntu:'
                if (bodyLowerCase.split(":")[1].toLowerCase().equals("latest")) {
                    result.add(fromCommand);
                }
            } else {
                result.add(fromCommand);
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
