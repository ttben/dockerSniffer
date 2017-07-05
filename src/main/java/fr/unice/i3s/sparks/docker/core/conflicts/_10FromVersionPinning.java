package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.FROMCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class _10FromVersionPinning {

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

    public static void main(String[] args) {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("ubuntu"))
        );

        List<Command> conflict = _10FromVersionPinning.conflict(dockerfile);
        System.out.println(conflict);

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("ubuntu:latest"))
        );

        conflict = _10FromVersionPinning.conflict(dockerfile);
        System.out.println(conflict);


        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("ubuntu:1.0-light"))
        );

        conflict = _10FromVersionPinning.conflict(dockerfile);
        System.out.println(conflict);
    }

}
