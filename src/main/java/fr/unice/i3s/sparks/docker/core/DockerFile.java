package fr.unice.i3s.sparks.docker.core;

import edu.emory.mathcs.backport.java.util.Arrays;
import fr.unice.i3s.sparks.docker.conflicts.commands.Command;

import java.util.List;

public class DockerFile {
    private List<Command> listOfCommand;

    public DockerFile(List<Command> listOfCommand) {

        this.listOfCommand = listOfCommand;
    }

    public DockerFile(Command... commands) {
        this.listOfCommand = Arrays.asList(commands);
    }

    public List<Command> getListOfCommand() {
        return listOfCommand;
    }

    public boolean contains(Class<?extends Command> commandClass) {
        for(Command command : listOfCommand) {
            if(command.getClass().isAssignableFrom(commandClass)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DockerFile{");
        sb.append("listOfCommand=").append(listOfCommand);
        sb.append('}');
        return sb.toString();
    }
}
