package fr.unice.i3s.sparks.docker.core.conflicts.cmd;

import fr.unice.i3s.sparks.docker.core.commands.CMDCommand;
import fr.unice.i3s.sparks.docker.core.model.Image;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CMDConflictSniffer {

    public static CMDConflict conflict(List<Image> images) {
        LinkedList<CMDCommand> collect = images.stream()
                .map(Image::getCommandList)
                .filter(c -> c instanceof CMDCommand)
                .map(CMDCommand.class::cast)
                .collect(Collectors.toCollection(LinkedList::new));

        if (collect.size() < 2) {
            return null;
        }

        return new CMDConflict(collect);
    }
}
