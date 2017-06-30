package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.FROMCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.LABELCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class _6MergeableLabel {
    public static List<List<Command>> conflict(Dockerfile dockerfile) {
        List<List<Command>> result = new ArrayList<>();

        List<Command> listOfCommand = dockerfile.getActions();

        ListIterator<Command> commandListIterator = listOfCommand.listIterator();

        while (commandListIterator.hasNext()) {
            Command currentCommand = commandListIterator.next();
            List<Command> currentCluster = new ArrayList<>();
            while (currentCommand instanceof LABELCommand && commandListIterator.hasNext()) {
                currentCluster.add(currentCommand);
                currentCommand = commandListIterator.next();
            }

            if(currentCommand instanceof LABELCommand) {
                currentCluster.add(currentCommand);
            }

            if (currentCluster.size() > 1) {
                result.add(currentCluster);
            }
        }


        return result;
    }

    public static void main(String[] args) {
        Dockerfile dockerfile = new Dockerfile(new LABELCommand("a"), new FROMCommand(new ImageID("a")), new LABELCommand("b"));
        List<List<Command>> conflict = _6MergeableLabel.conflict(dockerfile);
        System.out.println(conflict);
    }
}
