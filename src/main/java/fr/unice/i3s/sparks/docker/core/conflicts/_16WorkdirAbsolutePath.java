package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class _16WorkdirAbsolutePath {
    public static List<Command> conflict(Dockerfile dockerfile) {


        ArrayList<WORKDIRCommand> workdirCommands = dockerfile.getListOfCommand()
                .stream()
                .filter(c -> c instanceof WORKDIRCommand)
                .map(WORKDIRCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));

        List<Command> result = new ArrayList<>();

        for (WORKDIRCommand workdirCommand : workdirCommands) {
            String body = workdirCommand.getBody();
            if (!body.trim().toLowerCase().startsWith("/")) {
                result.add(workdirCommand);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new WORKDIRCommand("./r")
        );

        List<Command> conflict = _16WorkdirAbsolutePath.conflict(dockerfile);
        System.out.println(conflict);

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new ENVCommand("e", "e"),
                new ADDCommand("htp", "/lol"),
                new ADDCommand("https://www.lol.com"),
                new USERCommand("root"),
                new USERCommand("root"),
                new USERCommand("root"),
                new WORKDIRCommand("/usr/toto"),
                new USERCommand("root"),
                new USERCommand("user")
        );

        conflict = _16WorkdirAbsolutePath.conflict(dockerfile);
        System.out.println(conflict);


    }
}
