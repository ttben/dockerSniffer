package fr.unice.i3s.sparks.docker.core.conflicts.cmd;

import fr.unice.i3s.sparks.docker.core.commands.CMDCommand;
import fr.unice.i3s.sparks.docker.core.model.Dockerfile;
import javafx.util.Pair;
import one.util.streamex.StreamEx;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CMDConflictSniffer {

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
        /*
        Stream<CMDCommand> cmdCommandStream = images.stream()
                .map(Dockerfile::getListOfCommand)
                .filter(c -> c instanceof CMDCommand)
                .map(CMDCommand.class::cast);


        Optional<ArrayList> first = cmdCommandStream
                .flatMap(cmdCommand -> cmdCommandStream
                        .filter(cmdCommand2 -> cmdCommand != cmdCommand2)
                        .filter(cmdCommand2 -> cmdCommand.equals(cmdCommand2))
                        .map(cmdCommand2 -> {
                            ArrayList a = new ArrayList();
                            a.add(cmdCommand);
                            a.add(cmdCommand2);
                            return a;
                        })
                )
                .findFirst();

        if (!first.isPresent()) {
            return null;
        }



        System.out.println(first.get());
        */
        return cmdConflicts;
        //return new CMDConflict(first.get());
    }

    public static void main(String[] args) {
        Dockerfile aDockerfile = new Dockerfile();
        CMDCommand node = new CMDCommand(aDockerfile, "node");
        aDockerfile.addCommand(node);

        Dockerfile anotherDockerfile = new Dockerfile();
        CMDCommand node1 = new CMDCommand(anotherDockerfile, "node");
        anotherDockerfile.addCommand(node1);

        List<CMDConflict> conflict = CMDConflictSniffer.conflict(Arrays.asList(aDockerfile, anotherDockerfile));
        System.out.println(conflict);
    }
}
