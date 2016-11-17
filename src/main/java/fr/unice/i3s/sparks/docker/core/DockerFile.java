package fr.unice.i3s.sparks.docker.core;

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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DockerFile{");
        sb.append("listOfCommand=").append(listOfCommand);
        sb.append('}');
        return sb.toString();
    }
}
