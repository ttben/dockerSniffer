package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.conflicts.run.RUNConflict;
import fr.unice.i3s.sparks.docker.core.conflicts.run.RUNConflictSniffer;
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


        String folderThatContainsDockerfiles = "/Users/benjaminbenni/Work/merger/src/main/resources/dockerfiles/";
        File folder = new File(folderThatContainsDockerfiles);

        File[] files = folder.listFiles(textFilter);


        for (File f : files) {
            //System.out.println("Handling file:" + f.getAbsolutePath());

            Dockerfile dockerfile = DockerFileParser.parse(f);
            Dockerfile enrichedDockerfile = Enricher.enrich(dockerfile);
            dockerfiles.add(enrichedDockerfile);
        }


        List<Dockerfile> trivialDockerfiles = new ArrayList<>();
        List<Dockerfile> dockerfilesWithRUN = new ArrayList<>();
        List<Dockerfile> dockerfilesWithUpdateInstall = new ArrayList<>();

        int trivialThreshold = 2;
        int nbOfDockerfilesInConflict = 0;
        int nbNonParsed = 0;

        for (Dockerfile dockerfile : dockerfiles) {
            if (dockerfile.getListOfCommand().size() < trivialThreshold) {
                trivialDockerfiles.add(dockerfile);
            }

            if (dockerfile.contains(RUNCommand.class)) {
                dockerfilesWithRUN.add(dockerfile);
            }

            if (dockerfile.deepContains(Install.class) || dockerfile.deepContains(Update.class)) {
                dockerfilesWithUpdateInstall.add(dockerfile);
            }

            if (dockerfile.contains(NonParsedCommand.class)) {
                nbNonParsed += dockerfile.howMuch(NonParsedCommand.class);
            }


            RUNConflictSniffer runConflictSniffer = new RUNConflictSniffer();
            RUNConflict conflict = runConflictSniffer.conflict(dockerfile);

            if (conflict != null) {
                //nbOfDockerFilesThatContainsRunInstallOrUpdate++;
                if (!conflict.isEmpty()) {
                    nbOfDockerfilesInConflict++;
                    conflicts.add(conflict);
                }
            }

        }


        System.out.println("-------------------------------------");
        System.out.println(files.length + " dockerfiles handled.");
        System.out.println(dockerfiles.size() + " dockerfiles parsed into model.");
        System.out.println(nbNonParsed + " commands non parsed");
        System.out.println("-------------------------------------\n");
        percentageOf(trivialDockerfiles.size(), dockerfiles.size(), "of files are trivial");
        System.out.println(dockerfiles.size() - trivialDockerfiles.size() + " non-trivial files remain.");
        System.out.println();

        System.out.println(conflicts.size() + " run conflicts found spread on " + nbOfDockerfilesInConflict + " different dockerfiles.");

        System.out.println("--------------------------------------------------------------------------");


        int datasetWihoutTrivial = dockerfiles.size() - trivialDockerfiles.size();

        percentageOf(dockerfilesWithRUN.size(), datasetWihoutTrivial, "of files contained a RUN command");
        percentageOf(dockerfilesWithUpdateInstall.size(), datasetWihoutTrivial, "of files contained a RUN command that update or install");
        System.out.println();
        percentageOf(nbOfDockerfilesInConflict, datasetWihoutTrivial, "of files contained a RUN issue ");
        percentageOf(nbOfDockerfilesInConflict, dockerfilesWithRUN.size(), "of files contained a RUN command and have a RUN issue ");
        percentageOf(nbOfDockerfilesInConflict, dockerfilesWithUpdateInstall.size(), "of files that contains a RUN command (that update or install) and have a RUN issue");

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
        }

        printRepartition(sortByValue(repartitionsOfCommands));
    }

    private static void printRepartition(Map<Class, Integer> repartitionsOfCommands) {
        for(Map.Entry<Class, Integer> entry : repartitionsOfCommands.entrySet()) {
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
