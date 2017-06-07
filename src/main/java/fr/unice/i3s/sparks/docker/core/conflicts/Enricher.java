package fr.unice.i3s.sparks.docker.core.conflicts;


import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;

import java.util.ArrayList;
import java.util.List;

public class Enricher {
    public static List<Dockerfile> enrich(List<Dockerfile> dockerfiles) {
        List<Dockerfile> result = new ArrayList<>();

        List<Command> newListOfCommand = new ArrayList<Command>();

        for (Dockerfile dockerfile : dockerfiles) {

            newListOfCommand = analyseDockerFile(dockerfile);

            Dockerfile newDockerfile = new Dockerfile(newListOfCommand);
            result.add(newDockerfile);
        }

        return result;
    }

    private static List<Command> analyseDockerFile(Dockerfile dockerfile) {
        List<Command> newListOfCommand;
        newListOfCommand = new ArrayList<>();
        List<Command> listOfRunCommand = dockerfile.getListOfCommand();

        for (Command command : listOfRunCommand) {
            if (!(command instanceof RUNCommand)) {
                newListOfCommand.add(command);
                continue;
            }

            RUNCommand runCommand = (RUNCommand) command;
            List<ShellCommand> body = runCommand.getBody();

            RUNCommand newRunCommand = new RUNCommand();
            for (ShellCommand shellCommand : body) {
                if (shellCommand.getBody().contains("install")) {
                    Install install = new Install(shellCommand.getBody());
                    newRunCommand.add(install);
                } else if (shellCommand.getBody().contains("update")) {
                    Update update = new Update(shellCommand.getBody());
                    newRunCommand.add(update);
                } else {
                    newRunCommand.add(shellCommand);
                }
            }
            newListOfCommand.add(newRunCommand);
        }
        return newListOfCommand;
    }

    public static Dockerfile enrich(Dockerfile dockerfile) {
        List<Command> commands = analyseDockerFile(dockerfile);
        return new Dockerfile(commands);
    }
}
