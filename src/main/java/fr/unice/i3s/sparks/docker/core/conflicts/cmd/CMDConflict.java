package fr.unice.i3s.sparks.docker.core.conflicts.cmd;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.CMDCommand;
import javafx.util.Pair;

import java.util.LinkedList;

public class CMDConflict {
    private LinkedList<CMDCommand> conflictingCMDCommand = new LinkedList<>();

    public CMDConflict(LinkedList<CMDCommand> conflictingCMDCommand) {

        this.conflictingCMDCommand = conflictingCMDCommand;
    }

    public CMDConflict(Pair pair) {
        this.conflictingCMDCommand.add((CMDCommand) pair.getKey());
        this.conflictingCMDCommand.add((CMDCommand) pair.getValue());
    }

    public LinkedList<CMDCommand> getConflictingCMDCommand() {
        return conflictingCMDCommand;
    }

    public boolean isEmpty() {
        return conflictingCMDCommand.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CMDConflict that = (CMDConflict) o;

        return conflictingCMDCommand != null ? conflictingCMDCommand.equals(that.conflictingCMDCommand) : that.conflictingCMDCommand == null;
    }

    @Override
    public int hashCode() {
        return conflictingCMDCommand != null ? conflictingCMDCommand.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CMDConflict{");
        sb.append("conflictingCMDCommand=").append(conflictingCMDCommand);
        sb.append('}');
        return sb.toString();
    }
}
