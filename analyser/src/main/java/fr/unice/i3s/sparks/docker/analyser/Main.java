package fr.unice.i3s.sparks.docker.analyser;

import fr.unice.i3s.sparks.docker.core.commands.Install;
import fr.unice.i3s.sparks.docker.core.commands.NonParsedCommand;
import fr.unice.i3s.sparks.docker.core.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.core.commands.Update;
import fr.unice.i3s.sparks.docker.core.conflicts.Enricher;
import fr.unice.i3s.sparks.docker.core.conflicts.MalFormedImageException;
import fr.unice.i3s.sparks.docker.core.conflicts.run.RUNConflict;
import fr.unice.i3s.sparks.docker.core.conflicts.run.RUNConflictSniffer;
import fr.unice.i3s.sparks.docker.core.model.Dockerfile;
import fr.unice.i3s.sparks.docker.grammar.DockerFileParser;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws MalFormedImageException, IOException {
        analyseDockerfiles();
    }

    private static void analyseDockerfiles() throws IOException {
        List<Dockerfile> dockerfiles = new ArrayList<>();
        List<RUNConflict> conflicts = new ArrayList<>();


        FilenameFilter textFilter = (dir, name) -> {
            String lowercaseName = name.toLowerCase();
            return lowercaseName.endsWith("-dockerfile");
        };

        String folderThatContainsDockerfiles = "./src/main/resources/dockerfiles";
        File folder = new File(folderThatContainsDockerfiles);

        File[] files = folder.listFiles(textFilter);


        for (File f : files) {
            System.out.println("Handling file:" + f.getAbsolutePath());

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
        System.out.println();

        System.out.println(conflicts.size() + " run conflicts found spread on "+ nbOfDockerfilesInConflict+ " different dockerfiles.");

        System.out.println("--------------------------------------------------------------------------");


        int datasetWihoutTrivial = dockerfiles.size() - trivialDockerfiles.size();

        percentageOf(dockerfilesWithRUN.size(), datasetWihoutTrivial, "of files contained a RUN command");
        percentageOf(dockerfilesWithUpdateInstall.size(), datasetWihoutTrivial, "of files contained a RUN command that update or install");
        System.out.println();
        percentageOf(nbOfDockerfilesInConflict, datasetWihoutTrivial, "of files contained a RUN issue ");
        percentageOf(nbOfDockerfilesInConflict, dockerfilesWithRUN.size(), "of files contained a RUN command and have a RUN issue ");
        percentageOf(nbOfDockerfilesInConflict, dockerfilesWithUpdateInstall.size(), "of files that contains a RUN command (that update or install) and have a RUN issue");
    }

    private static void percentageOf(int thiz, int overThis, String msg) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        double percentage = ((double) thiz * 100) / ((double) overThis);
        System.out.println(df.format(percentage) + "% " + msg + " (" + thiz + "/" + overThis + ")");
    }
}
