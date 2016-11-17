package fr.unice.i3s.sparks.docker.conflicts.run;

import fr.unice.i3s.sparks.docker.Install;
import fr.unice.i3s.sparks.docker.Update;
import fr.unice.i3s.sparks.docker.core.DockerFile;
import fr.unice.i3s.sparks.docker.conflicts.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.conflicts.commands.ShellCommand;

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

        System.out.println(runCommands);

        int index = findFirstUpdate(runCommands);

        System.out.println(index);

        if(index == -1) {
            return null;
        }

        List<RUNCommand> remainingList = runCommands.subList(index, runCommands.size());

        List<RUNCommand> conflictingRUNCommand = new ArrayList<>();

        for(RUNCommand runCommand : remainingList) {
            List<ShellCommand> body = runCommand.getBody();
            for(ShellCommand shellCommand : body) {
                if(shellCommand instanceof Install) {
                    conflictingRUNCommand.add(runCommand);
                }
            }
        }

        System.out.println(conflictingRUNCommand);

        return null;
    }

    private int findFirstUpdate(ArrayList<RUNCommand> runCommands) {
        int index = -1;
        for(int i = 0; i < runCommands.size() ; i++) {
            RUNCommand runCommand = runCommands.get(i);

            System.out.println(runCommand);

            List<ShellCommand> body = runCommand.getBody();

            for(int j = 0 ; j < body.size() ; j++) {
                ShellCommand shellCommand = body.get(j);
                System.out.println(shellCommand);
                if(shellCommand instanceof Update) {
                    index = i;
                    return index;
                }
            }
        }
        return index;
    }
}
