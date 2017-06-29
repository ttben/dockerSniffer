package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ADDCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ENVCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.FROMCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class _12AddDiscouraged {
    public static final int THRESHOLD = 2;

    public static List<Command> conflict(Dockerfile dockerfile) {
        int nbOfAddCommand = dockerfile.howMuch(ADDCommand.class);
        if (nbOfAddCommand < THRESHOLD) {
            return new ArrayList<>();
        }

        return  dockerfile.getListOfCommand()
                .stream()
                .filter(c -> c instanceof ADDCommand)
                .map(ADDCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void main(String[] args) {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new ENVCommand("e", "e"),
                new ADDCommand("htp", "/lol"),
                new ADDCommand("https://www.lol.com")
        );

        List<Command> conflict = _12AddDiscouraged.conflict(dockerfile);
        System.out.println(conflict);
    }
}
