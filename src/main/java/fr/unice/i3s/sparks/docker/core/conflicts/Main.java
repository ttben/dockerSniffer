package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.conflicts.run.RUNConflict;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.analyser.DockerFileParser;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws MalFormedImageException, IOException, InterruptedException {
        analyseDockerfiles();
    }

    private static void analyseDockerfiles() throws IOException {

        List<Dockerfile> dockerfiles = new ArrayList<>();
        List<RUNConflict> conflicts = new ArrayList<>();


        FilenameFilter textFilter = (dir, name) -> {
            String lowercaseName = name.toLowerCase();
            return lowercaseName.endsWith("-dockerfile");
        };


        String folderThatContainsDockerfiles = "/Users/benjaminbenni/Work/PhD/src/main/resources/dockerfiles/";
        File folder = new File(folderThatContainsDockerfiles);

        File[] files = folder.listFiles(textFilter);


        for (File f : files) {
            //System.out.println("Handling file:" + f.getAbsolutePath());

            Dockerfile dockerfile = DockerFileParser.parse(f);
            Dockerfile enrichedDockerfile = Enricher.enrich(dockerfile);
            dockerfiles.add(enrichedDockerfile);
        }

        int nbDkF = dockerfiles.size();
        List<Dockerfile> trivialDockerfiles = filterTrivialDockerfiles(dockerfiles);

        System.out.println(nbDkF + "  files.");
        percentageOf(trivialDockerfiles.size(), nbDkF, "of files are trivial");
        System.out.println(dockerfiles.size() + " non-trivial files remain.");

        computeStatistics(dockerfiles);

        fixAndOptimise(dockerfiles, conflicts);

        computeStatistics(dockerfiles);

        /*
        System.out.println(conflicts.size() + " run conflicts found spread on " + nbOfDockerfilesInConflict + " different dockerfiles.");

        System.out.println("--------------------------------------------------------------------------");


        int datasetWihoutTrivial = dockerfiles.size() - trivialDockerfiles.size();

        percentageOf(dockerfilesWithRUN.size(), datasetWihoutTrivial, "of files contained a RUN command");
        percentageOf(dockerfilesWithUpdateInstall.size(), datasetWihoutTrivial, "of files contained a RUN command that update or install");
        System.out.println();
        percentageOf(nbOfDockerfilesInConflict, datasetWihoutTrivial, "of files contained a RUN issue ");
        percentageOf(nbOfDockerfilesInConflict, dockerfilesWithRUN.size(), "of files contained a RUN command and have a RUN issue ");
        percentageOf(nbOfDockerfilesInConflict, dockerfilesWithUpdateInstall.size(), "of files that contains a RUN command (that update or install) and have a RUN issue");



        int total = 0;
        for(List<RunIssue1.Issue> issues1 : issues) {
            total += issues1.size();
        }

        repartitionsOfCommands = new HashMap<>();

        for (Dockerfile dockerfile : dockerfiles) {

            computeRepartitionsOfCommands(AptInstall.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(AptUpdate.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(PipInstall.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(YumInstall.class, repartitionsOfCommands, dockerfile);

        }

        printRepartition(sortByValue(repartitionsOfCommands));

        System.out.println("\nTotal install without update command on same layer:" + total + " on " + issues.size() + " different dockerfiles");

        total = 0;
        for(List<List<OptimMultipleRun.Issue>> issues1 : optim1) {
            for (List<OptimMultipleRun.Issue> issues2 : issues1) {
                total += issues2.size();
            }
        }

        Set<Dockerfile> dockerfileSet = new HashSet<>();
        for(List<List<OptimMultipleRun.Issue>> issues1 : optim1) {
            for (List<OptimMultipleRun.Issue> issues2 : issues1) {
                dockerfileSet.add(issues2.get(0).getDockerfile());
            }
        }

        System.out.println("Total mergeable RUN:" + total + " on " + optim1.size() + " different clusters, on " + dockerfileSet.size() + " different dockerfiles");

        */
    }

    private static void fixAndOptimise(List<Dockerfile> dockerfiles, List<RUNConflict> conflicts) {
        fixSemanticGapIssue(dockerfiles);
        mergeContiguousRun(dockerfiles);
    }

    private static void mergeContiguousRun(List<Dockerfile> dockerfiles) {
        List<List<List<OptimMultipleRun.Issue>>> optim1 = new ArrayList<>();

        for (Dockerfile dockerfile : dockerfiles) {
            OptimMultipleRun optim1Issue = new OptimMultipleRun();
            List<List<OptimMultipleRun.Issue>> optim1IssueApplied = optim1Issue.apply(dockerfile);
            if (!optim1IssueApplied.isEmpty()) {
                optim1.add(optim1IssueApplied);
            }
        }

        int gain = 0;
        for (List<List<OptimMultipleRun.Issue>> dockerfileClusters : optim1) {
            for (List<OptimMultipleRun.Issue> localMerge : dockerfileClusters) {
                gain += localMerge.size() ;
            }
        }

        System.out.println("Rule: merge contiguous run.\n\t-> Number of run commands that can be deleted (by merge operation): " + gain + " commands.");
    }

    private static void fixSemanticGapIssue(List<Dockerfile> dockerfiles) {
        List<List<RunIssue1.Issue>> issues = new ArrayList<>();

        for (Dockerfile dockerfile : dockerfiles) {
            RunIssue1 runIssue1 = new RunIssue1();
            List<RunIssue1.Issue> apply = runIssue1.apply(dockerfile);
            if (!apply.isEmpty()) {
                issues.add(apply);
            }
        }

        int nbOfIssues = 0;
        for (List<RunIssue1.Issue> dockerfileClusters : issues) {
            nbOfIssues += dockerfileClusters.size();

        }
        System.out.println("Rule: run semantic gap.\n\t-> Number of run commands that have r.s.g. issue : " + nbOfIssues + " commands over " + issues.size() + " dockerfiles.");
    }

    private static void computeStatistics(List<Dockerfile> dockerfiles) {
        List<Dockerfile> dockerfilesWithRUN = new ArrayList<>();
        List<Dockerfile> dockerfilesWithUpdateInstall = new ArrayList<>();

        for (Dockerfile dockerfile : dockerfiles) {
            //System.out.println(dockerfile.getSourcefIle());

            if (dockerfile.contains(RUNCommand.class)) {
                dockerfilesWithRUN.add(dockerfile);
            }

            if (dockerfile.deepContains(AptInstall.class) && dockerfile.deepContains(AptUpdate.class)) {
                dockerfilesWithUpdateInstall.add(dockerfile);
            }
        }


        System.out.println("-------------------------------------");
        System.out.println(dockerfiles.size() + " dockerfiles parsed into model.");


        Map<Class, Integer> repartitionsOfCommands = new HashMap<>();

        for (Dockerfile dockerfile : dockerfiles) {
            computeRepartitionsOfCommands(FROMCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(RUNCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(ADDCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(COPYCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(ENTRYPointCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(WORKDIRCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(VOLUMECommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(CMDCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(EXPOSECommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(ENVCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(MAINTAINERCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(USERCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(NonParsedCommand.class, repartitionsOfCommands, dockerfile);
        }

        printRepartition(sortByValue(repartitionsOfCommands));


        int totalNbOfCommands = 0;
        for (Integer i : repartitionsOfCommands.values()) {
            totalNbOfCommands += i;
        }
        System.out.println("Total => " + totalNbOfCommands);


        int expectedTotalNbOfCommands = 0;
        for (Dockerfile dockerfile : dockerfiles) {
            expectedTotalNbOfCommands += dockerfile.getListOfCommand().size();
        }
        System.out.println("Expected? total => " + expectedTotalNbOfCommands);

        repartitionsOfCommands = new HashMap<>();

        for (Dockerfile dockerfile : dockerfiles) {

            computeRepartitionsOfCommands(AptInstall.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(AptUpdate.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(PipInstall.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(YumInstall.class, repartitionsOfCommands, dockerfile);

        }

        printRepartition(sortByValue(repartitionsOfCommands));


        System.out.println("--------------------------------------------------------------------------");
    }

    private static List<Dockerfile> filterTrivialDockerfiles(List<Dockerfile> dockerfiles) {
        int trivialThreshold = 2;

        List<Dockerfile> trivialDockerfiles = new ArrayList<>();

        ListIterator<Dockerfile> dockerfileListIterator = dockerfiles.listIterator();
        while (dockerfileListIterator.hasNext()) {
            Dockerfile dockerfile = dockerfileListIterator.next();

            if (dockerfile.getListOfCommand().size() < trivialThreshold) {
                trivialDockerfiles.add(dockerfile);
                dockerfileListIterator.remove();
            }
        }

        /*
        for (Dockerfile dockerfile : dockerfiles) {
            System.out.println(dockerfile.getSourcefIle());
            if (dockerfile.getListOfCommand().size() < trivialThreshold) {
                trivialDockerfiles.add(dockerfile);
            }
        }
        */

        return trivialDockerfiles;
    }

    private static void printRepartition(Map<Class, Integer> repartitionsOfCommands) {
        for (Map.Entry<Class, Integer> entry : repartitionsOfCommands.entrySet()) {
            System.out.println("-" + entry.getKey().getSimpleName() + ":" + entry.getValue());
        }
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private static void computeRepartitionsOfCommands(Class clazz, Map<Class, Integer> repartitionsOfCommands, Dockerfile dockerfile) {
        int command = dockerfile.howMuch(clazz);
        if (repartitionsOfCommands.containsKey(clazz)) {
            repartitionsOfCommands.put(clazz, command + repartitionsOfCommands.get(clazz));
        } else {
            repartitionsOfCommands.put(clazz, command);
        }
    }


    private static void percentageOf(int thiz, int overThis, String msg) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        double percentage = ((double) thiz * 100) / ((double) overThis);
        System.out.println(df.format(percentage) + "% " + msg + " (" + thiz + "/" + overThis + ")");
    }
}
