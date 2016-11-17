package fr.unice.i3s.sparks.docker;

import edu.emory.mathcs.backport.java.util.Arrays;
import fr.unice.i3s.sparks.docker.conflicts.commands.Command;
import fr.unice.i3s.sparks.docker.conflicts.commands.ENVCommand;
import fr.unice.i3s.sparks.docker.conflicts.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.conflicts.commands.ShellCommand;
import fr.unice.i3s.sparks.docker.core.DockerFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Enricher {
    public static List<DockerFile> enrich(List<DockerFile> dockerFiles) {
        List<DockerFile> result = new ArrayList<>();

        List<Command> newListOfCommand = new ArrayList<Command>();

        for(DockerFile dockerFile : dockerFiles) {

            newListOfCommand = analyseDockerFile(dockerFile);

            DockerFile newDockerFile = new DockerFile(newListOfCommand);
            result.add(newDockerFile);
        }

        return result;
    }

    private static List<Command> analyseDockerFile(DockerFile dockerFile) {
        List<Command> newListOfCommand;
        newListOfCommand = new ArrayList<>();
        List<Command> listOfRunCommand = dockerFile.getListOfCommand();

        for(Command command : listOfRunCommand) {
            if(! (command instanceof RUNCommand)) {
                newListOfCommand.add(command);
                continue;
            }

            RUNCommand runCommand = (RUNCommand) command;
            List<ShellCommand> body = runCommand.getBody();

            for(ShellCommand shellCommand : body) {
                if(shellCommand.getBody().contains("install")) {
                    Install install = new Install(shellCommand.getBody());
                    newListOfCommand.add(install);
                }
                else if(shellCommand.getBody().contains("update")) {
                    Update update = new Update(shellCommand.getBody());
                    newListOfCommand.add(update);
                } else {
                    newListOfCommand.add(shellCommand);
                }
            }
        }
        return newListOfCommand;
    }

    public static DockerFile enrich(DockerFile dockerFile) {
        List<Command> commands = analyseDockerFile(dockerFile);
        return new DockerFile(commands);
    }
}
