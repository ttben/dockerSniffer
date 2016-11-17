package fr.unice.i3s.sparks.docker;

import fr.unice.i3s.sparks.docker.conflicts.commands.Command;

import java.util.List;

public class DockerFile {
    private List<Command> listOfCommand;

    public DockerFile(List<Command> listOfCommand) {

        this.listOfCommand = listOfCommand;
    }

    public List<Command> getListOfCommand() {
        return listOfCommand;
    }
}
