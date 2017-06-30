package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class _15LessUserCommands {
    public static final int THRESHOLD = 4;

    public static List<Command> conflict(Dockerfile dockerfile) {
        int nbOfUserCommands = dockerfile.howMuch(USERCommand.class);
        if (nbOfUserCommands > THRESHOLD) {
            return  dockerfile.getActions()
                    .stream()
                    .filter(c -> c instanceof USERCommand)
                    .map(USERCommand.class::cast)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        return new ArrayList<>();
    }

    public static void main(String[] args) {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new ENVCommand("e", "e"),
                new ADDCommand("htp", "/lol"),
                new ADDCommand("https://www.lol.com"),
                new USERCommand("root"),
                new USERCommand("user")
        );

        List<Command> conflict = _15LessUserCommands.conflict(dockerfile);
        System.out.println(conflict);

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new ENVCommand("e", "e"),
                new ADDCommand("htp", "/lol"),
                new ADDCommand("https://www.lol.com"),
                new USERCommand("root"),
                new USERCommand("root"),
                new USERCommand("root"),
                new USERCommand("root"),
                new USERCommand("user")
        );

        conflict = _15LessUserCommands.conflict(dockerfile);
        System.out.println(conflict);


    }
}
