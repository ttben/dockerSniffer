package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class _17CdInRunCommand extends Check<Dockerfile, List<Command>> {
    public static List<Command> conflict(Dockerfile dockerfile) {
        ArrayList<RUNCommand> runCommands = dockerfile.getActions()
                .stream()
                .filter(c -> c instanceof RUNCommand)
                .map(RUNCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));

        List<Command> result = new ArrayList<>();

        for (RUNCommand runCommand : runCommands) {
            for (ShellCommand shellCommand : runCommand.getBody()) {
                List<String> body = shellCommand.getBody();
                Pattern compile = Pattern.compile("cd");
                for (String string : body) {
                    if (compile.matcher(string).matches()) {
                        result.add(runCommand);
                    }
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new ShellCommand("cdirecto", "lolilol"), new ShellCommand("mkdir", "toto"))
        );

        List<Command> conflict = _17CdInRunCommand.conflict(dockerfile);
        System.out.println(conflict);

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new ShellCommand("cd", ".."), new ShellCommand("mkdir", "toto"))
        );

        conflict = _17CdInRunCommand.conflict(dockerfile);
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
