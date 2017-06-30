package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class _19SpecifyNoInstallRecommends {

    public static <T extends Command> List<T> lol(Dockerfile dockerfile) {
        Class<T> l = null;
        List<T> listOfCommand = (List<T>) dockerfile.getActions();
        List<T> collect = new ArrayList<>();
        for (T t : listOfCommand) {
            if (l.isInstance(t)) {
                collect.add(t);
            }
        }

        return collect;
    }

    public static List<Command> conflict(Dockerfile dockerfile) {
        ArrayList<RUNCommand> runCommands =
                dockerfile.getActions()
                        .stream()
                        .filter(c -> c instanceof RUNCommand)
                        .map(RUNCommand.class::cast)
                        .collect(Collectors.toCollection(ArrayList::new));

        List<Command> result = new ArrayList<>();

        for (RUNCommand runCommand : runCommands) {
            for (ShellCommand shellCommand : runCommand.getBody()) {

                if (shellCommand instanceof AptInstall) {

                    List<String> body = shellCommand.getBody();

                    if (!body.contains("--no-install-recommends")) {
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

    public static void main(String[] args) {

        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new AptInstall("apt-get", "install", "-y", "ruby:203", "quby:203"))
        );

        List<Command> conflict = _19SpecifyNoInstallRecommends.conflict(dockerfile);
        System.out.println(conflict);

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new AptInstall("apt-get", "install", "-y", "java", "ruby"))
        );

        conflict = _19SpecifyNoInstallRecommends.conflict(dockerfile);
        System.out.println(conflict);

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new AptInstall("apt-get", "install", "ruby"))
        );

        conflict = _19SpecifyNoInstallRecommends.conflict(dockerfile);
        System.out.println(conflict);

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new AptInstall("apt-get", "install", "ruby:4", "wget"))
        );

        conflict = _19SpecifyNoInstallRecommends.conflict(dockerfile);
        System.out.println(conflict);
    }
}
