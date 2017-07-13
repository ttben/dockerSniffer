package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.conflicts.run.RUNConflict;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.AptInstall;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.AptUpdate;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ShellCommand;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

// FIXME this is broken, this is not the proper way to do this
// FIXME see RunIssue1
public class _8AlwaysUpdateAndInstallOnSameCommand {
    public static RUNConflict conflict(Dockerfile dockerFiles) {

        ArrayList<RUNCommand> runCommands = dockerFiles.getActions()
                .stream()
                .filter(c -> c instanceof RUNCommand)
                .map(RUNCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));

        //System.err.println("Remaining Run commands:" + runCommands);

        int index = findFirstUpdate(runCommands);

        //System.err.println(index);

        if (index == -1) {
            return null;
        }

        List<RUNCommand> remainingList = runCommands.subList(index + 1, runCommands.size());

        LinkedList<RUNCommand> conflictingRUNCommand = new LinkedList<>();

        for (RUNCommand runCommand : remainingList) {
            List<ShellCommand> body = runCommand.getBody();
            for (ShellCommand shellCommand : body) {
                if (shellCommand instanceof AptInstall) {
                    conflictingRUNCommand.add(runCommand);
                }
            }
        }

        if (!conflictingRUNCommand.isEmpty()) {
            conflictingRUNCommand.addFirst(runCommands.get(index));
        }
        RUNConflict runConflict = new RUNConflict(conflictingRUNCommand);

        //System.err.println("CONFLICT:" + conflictingRUNCommand);

        return runConflict;
    }

    private static int findFirstUpdate(ArrayList<RUNCommand> runCommands) {
        //System.err.println("Find first update...");
        int index = -1;
        for (int i = 0; i < runCommands.size(); i++) {
            RUNCommand runCommand = runCommands.get(i);

            //System.err.println("FFU: analysing..." + runCommand);

            List<ShellCommand> body = runCommand.getBody();

            //System.err.println("Body contains>" + body);

            for (int j = 0; j < body.size(); j++) {
                ShellCommand shellCommand = body.get(j);
                //System.err.println(shellCommand);
                if (shellCommand instanceof AptUpdate) {
                    index = i;
                    return index;
                }
            }
        }
        return index;
    }
}
