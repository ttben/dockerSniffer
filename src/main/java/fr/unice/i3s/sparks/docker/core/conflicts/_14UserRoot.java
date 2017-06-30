package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class _14UserRoot {
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

    public static void main(String[] args) {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new ENVCommand("e", "e"),
                new ADDCommand("htp", "/lol"),
                new ADDCommand("https://www.lol.com"),
                new USERCommand("root"),
                new USERCommand("user")
        );

        List<Command> conflict = _14UserRoot.conflict(dockerfile);
        System.out.println(conflict);
    }
}
