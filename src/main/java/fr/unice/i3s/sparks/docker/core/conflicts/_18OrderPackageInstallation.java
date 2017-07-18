package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.conflicts.tags.AptInstallTag;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ShellCommand;

import java.util.*;
import java.util.stream.Collectors;

public class _18OrderPackageInstallation extends Check<Dockerfile, List<Command>> {
    public static List<Command> conflict(Dockerfile dockerfile) {
        ArrayList<RUNCommand> runCommands = dockerfile.getActions()
                .stream()
                .filter(c -> c instanceof RUNCommand)
                .map(RUNCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));

        List<Command> result = new ArrayList<>();

        for (RUNCommand runCommand : runCommands) {
            for (ShellCommand shellCommand : runCommand.getBody()) {

                if (shellCommand.containsTag(AptInstallTag.class)) {
                    List<String> currentInstallation = new ArrayList<>();

                    List<String> body = shellCommand.getBody();
                    ListIterator<String> stringListIterator = body.listIterator();

                    String current = null;
                    while (stringListIterator.hasNext()) {
                        current = stringListIterator.next();
                        if (current.equals("apt-get")) {
                            continue;
                        }

                        if (current.equals("install")) {
                            continue;
                        }

                        if (current.equals("-y")) {
                            continue;
                        }

                        currentInstallation.add(current);
                    }
                    boolean sorted = isSorted(currentInstallation);
                    if (!sorted) {
                        result.add(runCommand);
                    }

                }
            }
        }

        return result;
    }


    public static boolean isSorted(List<String> list) {
        boolean sorted = true;
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i - 1).compareTo(list.get(i)) > 0) sorted = false;
        }

        return sorted;
    }


    @Override
    public Map<Dockerfile, List<Command>> apply(List<Dockerfile> dockerfiles) {
        Map<Dockerfile, List<Command>> result = new HashMap<>();

        for (Dockerfile dockerfile : dockerfiles) {
            List<Command> conflict = conflict(dockerfile);
            if (!conflict.isEmpty()) {
                result.put(dockerfile, conflict);
            }
        }

        return result;
    }
}
