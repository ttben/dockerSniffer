package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.CMDCommand;

public class _3MultipleCMD {

    public static boolean conflict(Dockerfile dockerfile) {
        return dockerfile.howMuch(CMDCommand.class) > 1;
    }
}
