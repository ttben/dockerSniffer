package fr.unice.i3s.sparks.docker.core.model;

import fr.unice.i3s.sparks.docker.core.commands.Command;

import java.util.Arrays;
import java.util.List;

public class Dockerfile {
    private List<Command> listOfCommand;

    public Dockerfile(List<Command> listOfCommand) {

        this.listOfCommand = listOfCommand;
    }

    public Dockerfile(Command... commands) {
        this.listOfCommand = Arrays.asList(commands);
    }

    public List<Command> getListOfCommand() {
        return listOfCommand;
    }

    public boolean contains(Class<? extends Command> commandClass) {
        for (Command command : listOfCommand) {
            if (command.getClass().equals(commandClass)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Dockerfile{");
        sb.append("listOfCommand=").append(listOfCommand);
        sb.append('}');
        return sb.toString();
    }
}
