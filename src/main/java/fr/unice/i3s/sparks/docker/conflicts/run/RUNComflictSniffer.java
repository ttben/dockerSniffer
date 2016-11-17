package fr.unice.i3s.sparks.docker.conflicts.run;

import fr.unice.i3s.sparks.docker.DockerFile;
import fr.unice.i3s.sparks.docker.conflicts.Image;
import fr.unice.i3s.sparks.docker.conflicts.commands.ENVCommand;
import fr.unice.i3s.sparks.docker.conflicts.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.conflicts.commands.ShellCommand;
import org.jvnet.hk2.internal.Collector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RUNComflictSniffer {
    public List<RUNConcflict> conflict(DockerFile dockerFiles) {
        ArrayList<RUNCommand> runCommands = dockerFiles.getListOfCommand()
                .stream()
                .filter(c -> c instanceof RUNCommand)
                .map(RUNCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));

        Stream<RUNCommand> install1 = runCommands.stream().filter(runCommand -> {
            List<ShellCommand> body = runCommand.getBody();
            Stream<ShellCommand> install = body.stream().filter(shellCommand -> {
                return shellCommand.getBody().contains("apt-get") && shellCommand.getBody().contains("install");
            });

            return install.count() > 0;
        });

        install1.forEach(runCommand -> {
            System.out.println(runCommand);
        });

        return null;
    }
}
