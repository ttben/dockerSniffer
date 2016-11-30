package fr.unice.i3s.sparks.docker.core.model;

import fr.unice.i3s.sparks.docker.core.commands.Command;
import fr.unice.i3s.sparks.docker.core.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.core.commands.ShellCommand;
import fr.unice.i3s.sparks.docker.core.commands.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dockerfile {
    private List<Command> listOfCommand;

    public Dockerfile(List<Command> listOfCommand) {

        this.listOfCommand = listOfCommand;
    }

    public Dockerfile(Command... commands) {
        this.listOfCommand = new ArrayList<>(Arrays.asList(commands));
    }

    public List<Command> getListOfCommand() {
        return listOfCommand;
    }

    public void addCommand(Command command) {
        this.listOfCommand.add(command);
    }

    public int howMuch(Class<? extends Command> commandClass) {
        int result = 0;
        for (Command command : listOfCommand) {
            if (command.getClass().equals(commandClass)) {
                result++;
            }
        }

        return result;
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

    public boolean deepContains(Class<? extends Command> commandClass) {
        for (Command command : listOfCommand) {
            if (command.getClass().equals(commandClass)) {
                return true;
            }

            if(command instanceof RUNCommand) {
                List<ShellCommand> body = ((RUNCommand) command).getBody();
                for(ShellCommand shellCommand : body) {
                    if(shellCommand.getClass().equals(commandClass)) {
                        return true;
                    }
                }
            }

        }

        return false;
    }
}
