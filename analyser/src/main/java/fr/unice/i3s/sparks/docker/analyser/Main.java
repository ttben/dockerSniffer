package fr.unice.i3s.sparks.docker.analyser;

import fr.unice.i3s.sparks.docker.DockerfileLexer;
import fr.unice.i3s.sparks.docker.DockerfileParser;
import fr.unice.i3s.sparks.docker.core.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.core.conflicts.Enricher;
import fr.unice.i3s.sparks.docker.core.conflicts.MalFormedImageException;
import fr.unice.i3s.sparks.docker.core.conflicts.run.RUNConflict;
import fr.unice.i3s.sparks.docker.core.conflicts.run.RUNConflictSniffer;
import fr.unice.i3s.sparks.docker.core.model.Dockerfile;
import fr.unice.i3s.sparks.docker.grammar.AntLRDockerListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws MalFormedImageException, IOException {

        List<Dockerfile> dockerfiles = new ArrayList<>();
        List<RUNConflict> conflicts = new ArrayList<>();


        FilenameFilter textFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith("-dockerfile")) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        String folderThatContainsDockerfiles = "./src/main/resources/dockerfiles";
        File folder = new File(folderThatContainsDockerfiles);

        File[] files = folder.listFiles(textFilter);
        int nbOfDockerFilesThatContainsRunInstallOrUpdate = 0;
        int nbOfDockerFilesThatContainsRun = 0;
        for (File f : files) {
            System.err.println("Handling file:" + f.getAbsolutePath());
            List<String> strings = FileUtils.readLines(f, "utf-8");

            String sentence = "";

            for (String s : strings) {
                sentence += s + "\n";
            }

            // Get our lexer
            DockerfileLexer lexer = new DockerfileLexer(new ANTLRInputStream(sentence));

            // Get a list of matched tokens
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            // Pass the tokens to the parser
            DockerfileParser parser = new DockerfileParser(tokens);

            // Specify our entry point
            DockerfileParser.DockerfileContext drinkSentenceContext = parser.dockerfile();

            // Walk it and attach our listener
            ParseTreeWalker walker = new ParseTreeWalker();
            AntLRDockerListener listener = new AntLRDockerListener();

            walker.walk(listener, drinkSentenceContext);

            Dockerfile dockerfile = listener.getDockerfile();

            if (dockerfile.contains(RUNCommand.class)) {
                nbOfDockerFilesThatContainsRun++;
            }

            Dockerfile enrichedDockerfile = Enricher.enrich(dockerfile);
            dockerfiles.add(enrichedDockerfile);

            //System.err.println(enrichedDockerfile);

            RUNConflictSniffer runConflictSniffer = new RUNConflictSniffer();
            RUNConflict conflict = runConflictSniffer.conflict(enrichedDockerfile);

            if (conflict != null) {
                nbOfDockerFilesThatContainsRunInstallOrUpdate++;
                if (!conflict.isEmpty()) {
                    conflicts.add(conflict);
                }
            }
        }
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);

        System.out.println(files.length + " dockerfiles handled.");
        System.out.println(dockerfiles.size() + " dockerfiles parsed into model.");
        System.out.println(conflicts.size() + " run conflicts found.");

        double percentageOfDockerfileQWithRun = (nbOfDockerFilesThatContainsRun * 100.0) / (double) dockerfiles.size();
        System.out.println(df.format(percentageOfDockerfileQWithRun) + "% of files contained any RUN command (" + nbOfDockerFilesThatContainsRun + ")");

        double percentageOfDockerfileQWithRunInstallOrUpdate = (nbOfDockerFilesThatContainsRunInstallOrUpdate * 100.0) / (double) dockerfiles.size();
        System.out.println(df.format(percentageOfDockerfileQWithRunInstallOrUpdate) + "% of files contained a RUN command that update or install (" + nbOfDockerFilesThatContainsRunInstallOrUpdate + ")");

        double percentageRunSniffedOnDockerfileThatContainsRunInstallOrUpdate = (conflicts.size() * 100.0) / (double) nbOfDockerFilesThatContainsRun;
        System.out.println(df.format(percentageRunSniffedOnDockerfileThatContainsRunInstallOrUpdate) + "% of files that contains a RUN command  and have a RUN issue (" + nbOfDockerFilesThatContainsRun + ")");

        double percentageRunSniffedOnDockerfileThatContainsRum = (conflicts.size() * 100.0) / (double) nbOfDockerFilesThatContainsRunInstallOrUpdate;
        System.out.println(df.format(percentageRunSniffedOnDockerfileThatContainsRum) + "% of files that contains a RUN command (that update or install) and have a RUN issue (" + nbOfDockerFilesThatContainsRunInstallOrUpdate + ")");

        double percentageRunSniffed = (conflicts.size() * 100.0) / (double) dockerfiles.size();
        System.out.println(df.format(percentageRunSniffed) + "% of files contained a RUN issue (" + conflicts.size() + ")");

    }
}
