package fr.unice.i3s.sparks.docker.core.conflicts.cmd;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.CMDCommand;
import javafx.util.Pair;
import one.util.streamex.StreamEx;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CMDShadowingConflictSniffer {

    public static List<CMDConflict> conflict(List<Dockerfile> images) {

        List<CMDCommand> collect = StreamEx.of(images)
                .map(Dockerfile::getListOfCommand)
                .flatMap(List::stream)
                .select(CMDCommand.class)
                .collect(Collectors.toList());

        List<CMDConflict> cmdConflicts = StreamEx.ofPairs(collect,
                (c1, c2) -> (c1 != c2
                        && !c1.equals(c2)
                        && c1.getParent() != c2.getParent()) ?
                        new CMDConflict(new Pair(c1, c2)) : null
        ).nonNull().collect(Collectors.toList());

        System.out.println(cmdConflicts);

        return cmdConflicts;
    }

    public static void main(String[] args) {
        Dockerfile aDockerfile = new Dockerfile();
        CMDCommand node = new CMDCommand(aDockerfile, "node");
        aDockerfile.addCommand(node);

        Dockerfile anotherDockerfile = new Dockerfile();
        CMDCommand node1 = new CMDCommand(anotherDockerfile, "node");
        anotherDockerfile.addCommand(node1);

        List<CMDConflict> conflict = CMDShadowingConflictSniffer.conflict(Arrays.asList(aDockerfile, anotherDockerfile));
        System.out.println(conflict);
    }
}
