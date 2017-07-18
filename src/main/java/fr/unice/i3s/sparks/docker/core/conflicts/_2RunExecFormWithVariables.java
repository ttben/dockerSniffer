package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class _2RunExecFormWithVariables extends Check<Dockerfile, List<Command>> {
    public static List<Command> conflict(Dockerfile dockerfile) {
        ArrayList<RUNCommand> runCommands = dockerfile.getActions()
                .stream()
                .filter(c -> c instanceof RUNCommand)
                .map(RUNCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));

        List<Command> result = new ArrayList<>();

        List<RUNCommand> execFormRun = new ArrayList<>();
        for (RUNCommand runCommand : runCommands) {
            if (isExecForm(runCommand)) {
                execFormRun.add(runCommand);
            }
        }

        for (RUNCommand runCommand : execFormRun) {
            if (useVariable(runCommand)) {
                result.add(runCommand);
            }
        }

        return result;
    }

    private static boolean useVariable(RUNCommand runCommand) {

        for (ShellCommand shellCommand : runCommand.getBody()) {
            for (String instruction : shellCommand.getBody()) {
                String s = instruction.trim().toLowerCase();
                Matcher matcher = Pattern.compile("\\$[a-zA-Z0-9_\\-]+").matcher(s);
                if (matcher.matches()) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isExecForm(RUNCommand runCommand) {
        return runCommand.getBody().size() > 0 && !runCommand.getBody().get(0).getBody().get(0).trim().toLowerCase().startsWith("/bin");
    }

    public static void main(String[] args) {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new ShellCommand("echo", "$HOME"))
        );

        List<Command> conflict = _2RunExecFormWithVariables.conflict(dockerfile);
        System.out.println(conflict);

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new ShellCommand("/bin/sh", "$HOME"))
        );

        conflict = _2RunExecFormWithVariables.conflict(dockerfile);
        System.out.println(conflict);
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
