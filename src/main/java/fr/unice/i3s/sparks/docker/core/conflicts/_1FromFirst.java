package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.FROMCommand;

public class _1FromFirst {

    public static boolean conflict(Dockerfile dockerfile) {
        return !(dockerfile.getListOfCommand().size() > 0 && dockerfile.getListOfCommand().get(0) instanceof FROMCommand);
    }
}