package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.CMDCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _3MultipleCMD extends Check<Dockerfile, Boolean> {

    public static boolean conflict(Dockerfile dockerfile) {
        return dockerfile.howMuch(CMDCommand.class) > 1;
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
