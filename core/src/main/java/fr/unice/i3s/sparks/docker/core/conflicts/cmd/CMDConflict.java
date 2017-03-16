package fr.unice.i3s.sparks.docker.core.conflicts.cmd;

import fr.unice.i3s.sparks.docker.core.commands.CMDCommand;

import java.util.LinkedList;

public class CMDConflict {
    private LinkedList<CMDCommand> conflictingCMDCommand;

    public CMDConflict(LinkedList<CMDCommand> conflictingCMDCommand) {

        this.conflictingCMDCommand = conflictingCMDCommand;
    }

    public LinkedList<CMDCommand> getConflictingCMDCommand() {
        return conflictingCMDCommand;
    }

    public boolean isEmpty() {
        return conflictingCMDCommand.isEmpty();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CMDCommand{");
        sb.append("conflictingCMDCommand=").append(conflictingCMDCommand);
        sb.append('}');
        return sb.toString();
    }
}
