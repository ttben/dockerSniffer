package fr.unice.i3s.sparks.docker.conflicts.env;

import fr.unice.i3s.sparks.docker.conflicts.Image;
import fr.unice.i3s.sparks.docker.conflicts.ImageTree;
import fr.unice.i3s.sparks.docker.conflicts.commands.Command;
import fr.unice.i3s.sparks.docker.conflicts.commands.ENVCommand;

import java.util.*;
import java.util.stream.Collectors;

public class ENVConflictSniffer {

    public static ENVConflictMap conflict(List<Image> images) {
        Set<Command> completeImageSet = images.parallelStream().map(Image::getCommandList).flatMap(List::stream).collect(Collectors.toSet());

        //System.out.printf("Perform union of all fr.unice.i3s.sparks.docker.conflicts.commands of all images input...\nComplete set of fr.unice.i3s.sparks.docker.conflicts.commands:%s\n", completeImageSet);

        Set<Command> remainingCommandSet = completeImageSet.stream().filter(c -> c instanceof ENVCommand).collect(Collectors.toSet());
        Set<ENVCommand> envCommandSet = ((Set) remainingCommandSet);

        //System.out.printf("Complete set of 'ENV' command:%s\n", envCommandSet);

        if (envCommandSet.size() < 2) {
            return null;
        }

        Map<String, List<ENVCommand>> conflictMap = envCommandSet.stream().collect(Collectors.groupingBy(ENVCommand::getKey));

        conflictMap = conflictMap.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if(conflictMap.keySet().size() == 0) {
            return null;
        }
        //System.out.println(conflictMap);

        ENVConflictMap envConflictMap = new ENVConflictMap(conflictMap);

        return envConflictMap;
    }
}
