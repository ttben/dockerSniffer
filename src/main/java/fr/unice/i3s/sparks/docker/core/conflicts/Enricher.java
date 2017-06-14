package fr.unice.i3s.sparks.docker.core.conflicts;


import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;

import java.util.ArrayList;
import java.util.List;

public class Enricher {
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
                if (shellCommand.getBody().contains("install") && shellCommand.getBody().contains("apt-get")) {
                    AptInstall aptInstall = new AptInstall(shellCommand.getBody());
                    newRunCommand.add(aptInstall);
                } else if (shellCommand.getBody().contains("update") && shellCommand.getBody().contains("apt-get")) {
                    AptUpdate aptUpdate = new AptUpdate(shellCommand.getBody());
                    newRunCommand.add(aptUpdate);
                } else if (shellCommand.getBody().contains("mkdir")) {
                    FolderCreation folderCreation = new FolderCreation(shellCommand.getBody());
                    newRunCommand.add(folderCreation);
                } else if (shellCommand.getBody().contains("install") && shellCommand.getBody().contains("pip")) {
                    PipInstall pipInstall = new PipInstall(shellCommand.getBody());
                    newRunCommand.add(pipInstall);
                } else if (shellCommand.getBody().contains("install") && shellCommand.getBody().contains("yum")) {
                    YumInstall yumInstall = new YumInstall(shellCommand.getBody());
                    newRunCommand.add(yumInstall);
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
        Dockerfile dockerfile1 = new Dockerfile(commands, dockerfile.getSourcefIle());
        return dockerfile1;
    }
}
