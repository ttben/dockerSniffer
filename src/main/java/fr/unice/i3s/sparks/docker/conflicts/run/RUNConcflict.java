package fr.unice.i3s.sparks.docker.conflicts.run;

import fr.unice.i3s.sparks.docker.conflicts.commands.RUNCommand;

import java.util.LinkedList;

public class RUNConcflict {
    private LinkedList<RUNCommand> conflictingRUNCommand;

    public RUNConcflict(LinkedList<RUNCommand> conflictingRUNCommand) {

        this.conflictingRUNCommand = conflictingRUNCommand;
    }

    public LinkedList<RUNCommand> getConflictingRUNCommand() {
        return conflictingRUNCommand;
    }

    public boolean isEmpty() {
        return conflictingRUNCommand.isEmpty();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RUNConcflict{");
        sb.append("conflictingRUNCommand=").append(conflictingRUNCommand);
        sb.append('}');
        return sb.toString();
    }
}
