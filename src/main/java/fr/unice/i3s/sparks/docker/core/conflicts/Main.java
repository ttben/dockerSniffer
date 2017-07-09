package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.conflicts.run.RUNConflict;
import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.DockerFileParser;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

public class Main {

    public static final String PATH_TO_DKF = "/Users/benjaminbenni/Work/PhD/src/main/resources/dockerfiles/";

    public static void main(String[] args) throws MalFormedImageException, IOException, InterruptedException {
        new Main().analyseDockerfiles();
    }

    private void analyseDockerfiles() throws IOException {

        List<Dockerfile> dockerfiles = new ArrayList<>();
        List<RUNConflict> conflicts = new ArrayList<>();


        FilenameFilter textFilter = (dir, name) -> {
            String lowercaseName = name.toLowerCase();
            return lowercaseName.endsWith("-dockerfile");
        };


        String folderThatContainsDockerfiles = PATH_TO_DKF;
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

        //System.out.println(nbDkF + "  files.");
        percentageOf(trivialDockerfiles.size(), nbDkF, "of files are trivial");
        //System.out.println(dockerfiles.size() + " non-trivial files remain.");

        computeStatistics(dockerfiles);

        fixAndOptimise(dockerfiles, conflicts);

        computeStatistics(dockerfiles);


        int nbDkf_1 = 0;
        for (Dockerfile dockerfile : dockerfiles) {
            boolean conflict = _1FromFirst.conflict(dockerfile);
            if (conflict) {
                nbDkf_1++;
            }
        }

        //System.out.printf("G_1FromFirst:\n\tDKF:%s\n", nbDkf_1);

        displayApplicationOnDataSet(_2RunExecFormWithVariables.class, dockerfiles);

        int nbDkf_3 = 0;
        for (Dockerfile dockerfile : dockerfiles) {
            boolean conflict = _3MultipleCMD.conflict(dockerfile);
            if (conflict) {
                nbDkf_3++;
            }
        }

        //System.out.printf("G_3MultipleCMD\n\t:DKF:%s\n", nbDkf_3);


        displayApplicationOnDataSet(_5CmdExecFormWithVariables.class, dockerfiles);

        int nbDkf_6 = 0;
        int nbDkC_6 = 0;
        for (Dockerfile dockerfile : dockerfiles) {
            List<List<Command>> conflict = _6MergeableLabel.conflict(dockerfile);
            if (!conflict.isEmpty()) {
                nbDkf_6++;

                for (List<Command> cluster : conflict) {
                    nbDkC_6 += cluster.size() - 1;
                }

            }
        }

        //System.out.printf("G_6MergeableLabel:\n\tDKF:%s, \tDKC:%s\n", nbDkf_6, nbDkC_6);


        displayApplicationOnDataSet(_7AptGetUpgrade.class, dockerfiles);



        int nbDkf_8 = 0;
        int nbDkC_8 = 0;
        for (Dockerfile dockerfile : dockerfiles) {
            RUNConflict conflict = _8AlwaysUpdateAndInstallOnSameCommand.conflict(dockerfile);
            if (conflict != null && !conflict.isEmpty()) {
                nbDkf_8++;
                nbDkC_8 += conflict.getConflictingRUNCommand().size();
            }
        }

        //System.out.printf("G_8AlwaysUpdateAndInstallOnSameCommand:\n\tDKF:%s, \tDKC:%s\n", nbDkf_8, nbDkC_8);



        displayApplicationOnDataSet(_9PackageInstallationVersionPinning.class, dockerfiles);
        displayApplicationOnDataSet(_10FromVersionPinning.class, dockerfiles);
        displayApplicationOnDataSet(_12AddDiscouraged.class, dockerfiles);
        displayApplicationOnDataSet(_13AddHttpDiscouraged.class, dockerfiles);
        displayApplicationOnDataSet(_14UserRoot.class, dockerfiles);
        displayApplicationOnDataSet(_15LessUserCommands.class, dockerfiles);
        displayApplicationOnDataSet(_16WorkdirAbsolutePath.class, dockerfiles);
        displayApplicationOnDataSet(_17CdInRunCommand.class, dockerfiles);
        displayApplicationOnDataSet(_18OrderPackageInstallation.class, dockerfiles);
        displayApplicationOnDataSet(_19SpecifyNoInstallRecommends.class, dockerfiles);



        Map<String, List<FROMCommand>> index = new HashMap();

        buildIndex("node", index, dockerfiles);
        buildIndex("ubuntu", index, dockerfiles);
        buildIndex("debian", index, dockerfiles);
        buildIndex("busybox", index, dockerfiles);
        buildIndex("redis", index, dockerfiles);
        buildIndex("alpine", index, dockerfiles);
        buildIndex("mysql", index, dockerfiles);
        buildIndex("mongo", index, dockerfiles);
        buildIndex("elasticsarch", index, dockerfiles);
        buildIndex("logstash", index, dockerfiles);
        buildIndex("postgres", index, dockerfiles);
        buildIndex("httpd", index, dockerfiles);
        buildIndex("wordpress", index, dockerfiles);
        buildIndex("centos", index, dockerfiles);
        buildIndex("ruby", index, dockerfiles);
        buildIndex("memcached", index, dockerfiles);
        buildIndex("python", index, dockerfiles);
        buildIndex("php", index, dockerfiles);
        buildIndex("jenkins", index, dockerfiles);
        buildIndex("golang", index, dockerfiles);
        buildIndex("java", index, dockerfiles);
        buildIndex("rabbitmq", index, dockerfiles);
        buildIndex("mariadb", index, dockerfiles);
        buildIndex("kibana", index, dockerfiles);

        for (Map.Entry<String, List<FROMCommand>> stringListEntry : index.entrySet()) {
            //System.out.println(stringListEntry.getKey() + " " + stringListEntry.getValue().size());
        }



    }

    private void displayApplicationOnDataSet(Class clazz, List<Dockerfile> dockerfiles) {

        int nbDkf = 0;
        int nbDkC = 0;

        for (Dockerfile dockerfile : dockerfiles) {
            List<Command> conflict = invokeConflictMethod(clazz, dockerfile);
            if (!conflict.isEmpty()) {
                nbDkf++;
                nbDkC += conflict.size();
            }
        }

        // System.out.printf("G"+ clazz.getSimpleName() + ":\n\tDKF:%s,\tDKC:%s\n", nbDkf, nbDkC);

    }

    private List<Command> invokeConflictMethod(Class clazz, Dockerfile dockerfile) {
        Object invoke = null;
        try {
            Method conflict = clazz.getMethod("conflict", Dockerfile.class);
            invoke = conflict.invoke(null, dockerfile);
            return (List<Command>) invoke;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    private static void buildIndex(String from, Map<String, List<FROMCommand>> index, List<Dockerfile> dockerfiles) {
        index.put(from, new ArrayList<>());

        for (Dockerfile dockerfile : dockerfiles) {
            Command command = dockerfile.getActions().get(0);
            if (command instanceof FROMCommand) {
                ImageID parent = ((FROMCommand) command).getParent();
                if (parent.toString().startsWith(from)) {
                    index.get(from).add((FROMCommand) command);
                }
            }
        }
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
                gain += localMerge.size();
            }
        }

        // System.out.println("Rule: merge contiguous run.\n\t-> Number of run commands that can be deleted (by merge operation): " + gain + " commands.");
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
        // System.out.println("Rule: run semantic gap.\n\t-> Number of run commands that have r.s.g. issue : " + nbOfIssues + " commands over " + issues.size() + " dockerfiles.");
    }

    private static void computeStatistics(List<Dockerfile> dockerfiles) {
        List<Dockerfile> dockerfilesWithRUN = new ArrayList<>();
        List<Dockerfile> dockerfilesWithUpdateInstall = new ArrayList<>();

        for (Dockerfile dockerfile : dockerfiles) {
            //System.out.println(dockerfile.getSourceFile());

            if (dockerfile.contains(RUNCommand.class)) {
                dockerfilesWithRUN.add(dockerfile);
            }

            if (dockerfile.contains(AptInstall.class) && dockerfile.contains(AptUpdate.class)) {
                dockerfilesWithUpdateInstall.add(dockerfile);
            }
        }


        // System.out.println("-------------------------------------");
        // System.out.println(dockerfiles.size() + " dockerfiles parsed into model.");


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
            computeRepartitionsOfCommands(LABELCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(ARGCommand.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(NonParsedCommand.class, repartitionsOfCommands, dockerfile);
        }

        printRepartition(sortByValue(repartitionsOfCommands));


        int totalNbOfCommands = 0;
        for (Integer i : repartitionsOfCommands.values()) {
            totalNbOfCommands += i;
        }
        //System.out.println("Total => " + totalNbOfCommands);


        int expectedTotalNbOfCommands = 0;
        for (Dockerfile dockerfile : dockerfiles) {
            expectedTotalNbOfCommands += dockerfile.getActions().size();
        }
        //System.out.println("Expected? total => " + expectedTotalNbOfCommands);

        repartitionsOfCommands = new HashMap<>();

        for (Dockerfile dockerfile : dockerfiles) {

            computeRepartitionsOfCommands(AptInstall.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(AptUpdate.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(PipInstall.class, repartitionsOfCommands, dockerfile);
            computeRepartitionsOfCommands(YumInstall.class, repartitionsOfCommands, dockerfile);

        }

        printRepartition(sortByValue(repartitionsOfCommands));


       // System.out.println("--------------------------------------------------------------------------");

        List<Dockerfile> extract = extract(NonParsedCommand.class, dockerfiles);
        //System.out.println();
    }

    private static List<Dockerfile> filterTrivialDockerfiles(List<Dockerfile> dockerfiles) {
        int trivialThreshold = 2;

        List<Dockerfile> trivialDockerfiles = new ArrayList<>();

        ListIterator<Dockerfile> dockerfileListIterator = dockerfiles.listIterator();
        while (dockerfileListIterator.hasNext()) {
            Dockerfile dockerfile = dockerfileListIterator.next();

            if (dockerfile.getActions().size() < trivialThreshold) {
                trivialDockerfiles.add(dockerfile);
                dockerfileListIterator.remove();
            }
        }

        /*
        for (Dockerfile dockerfile : dockerfiles) {
            System.out.println(dockerfile.getSourceFile());
            if (dockerfile.getActions().size() < trivialThreshold) {
                trivialDockerfiles.add(dockerfile);
            }
        }
        */

        return trivialDockerfiles;
    }

    private static void printRepartition(Map<Class, Integer> repartitionsOfCommands) {
        for (Map.Entry<Class, Integer> entry : repartitionsOfCommands.entrySet()) {
            //System.out.println("-" + entry.getKey().getSimpleName() + ":" + entry.getValue());
        }
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(comparingByValue(Collections.reverseOrder()))
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

    private static List<Dockerfile> extract(Class clazz, List<Dockerfile> dockerfiles) {
        List<Dockerfile> result = new ArrayList<>();

        for (Dockerfile dockerfile : dockerfiles) {
            int command = dockerfile.howMuch(clazz);
            if (command > 0) {
                result.add(dockerfile);
            }
        }

        return result;
    }


    private static void percentageOf(int thiz, int overThis, String msg) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        double percentage = ((double) thiz * 100) / ((double) overThis);
        //System.out.println(df.format(percentage) + "% " + msg + " (" + thiz + "/" + overThis + ")");
    }
}
