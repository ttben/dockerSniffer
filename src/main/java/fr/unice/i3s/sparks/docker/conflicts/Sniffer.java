package fr.unice.i3s.sparks.docker.conflicts;

import fr.unice.i3s.sparks.docker.conflicts.env.ENVConflictMap;
import fr.unice.i3s.sparks.docker.conflicts.env.ENVConflictSniffer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Sniffer {
    public static void analyze(Image... images) throws MalFormedImageException {
        //  Linearise the images set, i.e. transform the given set of images into tree
        ImageTree imageTree = ImageTreeBuilder.build(new HashSet<Image>(Arrays.asList(images)));

        Set<List<Image>> pathsToAnalyze = getListOfImagesToAnalyze(imageTree);

        Set<ENVConflictMap> envConflictMapSet = new HashSet<>();

        for (List<Image> imageList : pathsToAnalyze) {
            ENVConflictMap conflict = ENVConflictSniffer.conflict(imageList);
            envConflictMapSet.add(conflict);
        }

        System.out.println(envConflictMapSet);
    }

    private static Set<List<Image>> getListOfImagesToAnalyze(ImageTree imageTree) {
        Set<List<Image>> allPath = new HashSet<>();

        for (Image currentRoot : imageTree.getRoots()) {
            DepthFirstSearch depthFirstSearch = new DepthFirstSearch();
            Set<List<Image>> linearize = depthFirstSearch.linearize(currentRoot);
            allPath.addAll(linearize);
        }

        //  Filter roots (or not?)
        return allPath.stream().filter(x -> x.size() > 1).collect(Collectors.toSet());
    }
}