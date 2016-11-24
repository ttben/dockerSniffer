package fr.unice.i3s.sparks.docker.core.conflicts.env;

import fr.unice.i3s.sparks.docker.core.commands.Command;
import fr.unice.i3s.sparks.docker.core.commands.ENVCommand;
import fr.unice.i3s.sparks.docker.core.model.Image;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ENVConflictSniffer {

    public static ENVConflictMap conflict(List<Image> images) {

        Stream<Image> imageStream = images.stream();

        imageStream
                .map(Image::getCommandList)
                .filter(c -> c instanceof ENVCommand)
                .map(ENVCommand.class::cast)
                .collect(Collectors.groupingBy(ENVCommand::getKey))
                .entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Set<Command> completeImageSet =
                images.parallelStream()
                        .map(Image::getCommandList)
                        .flatMap(List::stream)
                        .collect(Collectors.toSet());

        //System.out.printf("Perform union of all fr.unice.i3s.sparks.docker.conflicts.commands of all images input...\nComplete set of fr.unice.i3s.sparks.docker.conflicts.commands:%s\n", completeImageSet);

        Set<Command> remainingCommandSet =
                completeImageSet.stream()
                        .filter(c -> c instanceof ENVCommand).collect(Collectors.toSet());

        Set<ENVCommand> envCommandSet = ((Set) remainingCommandSet);

        //System.out.printf("Complete set of 'ENV' command:%s\n", envCommandSet);

        if (envCommandSet.size() < 2) {
            return null;
        }

        Map<String, List<ENVCommand>> conflictMap = envCommandSet.stream().collect(Collectors.groupingBy(ENVCommand::getKey));

        conflictMap = conflictMap.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (conflictMap.keySet().size() == 0) {
            return null;
        }
        //System.out.println(conflictMap);

        ENVConflictMap envConflictMap = new ENVConflictMap(conflictMap);

        return envConflictMap;
    }
}
