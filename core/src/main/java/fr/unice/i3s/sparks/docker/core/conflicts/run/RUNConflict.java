package fr.unice.i3s.sparks.docker.core.conflicts.run;

import fr.unice.i3s.sparks.docker.core.commands.RUNCommand;

import java.util.LinkedList;

public class RUNConflict {
    private LinkedList<RUNCommand> conflictingRUNCommand;

    public RUNConflict(LinkedList<RUNCommand> conflictingRUNCommand) {

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
        final StringBuffer sb = new StringBuffer("RUNConflict{");
        sb.append("conflictingRUNCommand=").append(conflictingRUNCommand);
        sb.append('}');
        return sb.toString();
    }
}
