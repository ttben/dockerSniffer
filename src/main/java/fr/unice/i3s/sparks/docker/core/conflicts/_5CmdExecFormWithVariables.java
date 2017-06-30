package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class _5CmdExecFormWithVariables {
    public static List<Command> conflict(Dockerfile dockerfile) {
        ArrayList<CMDCommand> runCommands = dockerfile.getActions()
                .stream()
                .filter(c -> c instanceof CMDCommand)
                .map(CMDCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));

        List<Command> result = new ArrayList<>();

        List<CMDCommand> execFormRun = new ArrayList<>();
        for (CMDCommand cmdCommand : runCommands) {
            if (isExecForm(cmdCommand)) {
                execFormRun.add(cmdCommand);
            }
        }

        for (CMDCommand cmdCommand : execFormRun) {
            if (useVariable(cmdCommand)) {
                result.add(cmdCommand);
            }
        }

        return result;
    }

    private static boolean useVariable(CMDCommand cmdCommand) {
        for (String instruction : cmdCommand.getBody()) {
            String s = instruction.trim().toLowerCase();
            Matcher matcher = Pattern.compile("\\$[a-zA-Z0-9_\\-]+").matcher(s);
            if (matcher.matches()) {
                return true;
            }
        }

        return false;
    }

    private static boolean isExecForm(CMDCommand runCommand) {
        return runCommand.getBody().size() > 0 && !runCommand.getBody().get(0).trim().toLowerCase().startsWith("/bin");
    }

    public static void main(String[] args) {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new ShellCommand("echo", "$HOME")),
                new CMDCommand("echo", "$HOME")
        );

        List<Command> conflict = _5CmdExecFormWithVariables.conflict(dockerfile);
        System.out.println(conflict);

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new ShellCommand("/bin/sh", "$HOME")),
                new CMDCommand("/bin/sh", "$HOME")
        );

        conflict = _5CmdExecFormWithVariables.conflict(dockerfile);
        System.out.println(conflict);
    }
}
