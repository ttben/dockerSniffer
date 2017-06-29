package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class _9PackageInstallationVersionPinning {
    public static List<Command> conflict(Dockerfile dockerfile) {
        ArrayList<RUNCommand> runCommands = dockerfile.getListOfCommand()
                .stream()
                .filter(c -> c instanceof RUNCommand)
                .map(RUNCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));

        List<Command> result = new ArrayList<>();

        for (RUNCommand runCommand : runCommands) {
            for (ShellCommand shellCommand : runCommand.getBody()) {

                if (shellCommand instanceof AptInstall) {
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

                        if (current.split(":").length == 1) {
                            result.add(runCommand);
                        }
                    }
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new AptInstall("apt-get", "install", "-y", "ruby:203"))
        );

        List<Command> conflict = _9PackageInstallationVersionPinning.conflict(dockerfile);
        System.out.println(conflict);

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new AptInstall("apt-get", "install", "-y", "ruby"))
        );

        conflict = _9PackageInstallationVersionPinning.conflict(dockerfile);
        System.out.println(conflict);

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new AptInstall("apt-get", "install", "ruby"))
        );

        conflict = _9PackageInstallationVersionPinning.conflict(dockerfile);
        System.out.println(conflict);

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new AptInstall("apt-get", "install", "ruby:4"))
        );

        conflict = _9PackageInstallationVersionPinning.conflict(dockerfile);
        System.out.println(conflict);


    }
}
