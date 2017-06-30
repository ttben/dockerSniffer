package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class _13AddHttpDiscouraged {
    public static List<Command> conflict(Dockerfile dockerfile) {
        List<Command> result = new ArrayList<>();

        ArrayList<ADDCommand> addCommands = dockerfile.getActions()
                .stream()
                .filter(c -> c instanceof ADDCommand)
                .map(ADDCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));

        for (ADDCommand addCommand : addCommands) {
            for (String string : addCommand.getBody()) {
                if (string.trim().startsWith("http")) {
                    result.add(addCommand);
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new ENVCommand("e", "e"),
                new ADDCommand("htp", "/lol"),
                new ADDCommand("https://www.lol.com")
        );

        List<Command> conflict = _13AddHttpDiscouraged.conflict(dockerfile);
        System.out.println(conflict);
    }
}
