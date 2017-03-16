package fr.unice.i3s.sparks.docker.core.conflicts.cmd;

import fr.unice.i3s.sparks.docker.core.commands.CMDCommand;
import fr.unice.i3s.sparks.docker.core.model.Image;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CMDConflictSniffer {

    public static CMDConflict conflict(List<Image> images) {
        Stream<CMDCommand> cmdCommandStream = images.stream()
                .map(Image::getCommandList).filter(c -> c instanceof CMDCommand)
                .map(CMDCommand.class::cast);


        Optional<Pair> collect = cmdCommandStream
                .flatMap(cmdCommand -> cmdCommandStream
                        .filter(cmdCommand2 -> cmdCommand != cmdCommand2)
                        .filter(cmdCommand2 -> cmdCommand.equals(cmdCommand2))
                        .map(cmdCommand2 -> new Pair(cmdCommand, cmdCommand2))
                )
                .findFirst();

        if (!collect.isPresent()) {
            return null;
        }


        return new CMDConflict(collect.get());
    }
}
