package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.FROMCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _1FromFirst extends Check<Dockerfile, Boolean> {

    public static boolean conflict(Dockerfile dockerfile) {
        return !(dockerfile.getActions().size() > 0 && dockerfile.getActions().get(0) instanceof FROMCommand);
    }

    @Override
    public Map<Dockerfile, Boolean> apply(List<Dockerfile> dockerfiles) {
        Map<Dockerfile, Boolean> result = new HashMap<>();

        for (Dockerfile dockerfile : dockerfiles) {
            Boolean conflict = conflict(dockerfile);
            if (conflict) {
                result.put(dockerfile, conflict);
            }
        }

        return result;
    }
}