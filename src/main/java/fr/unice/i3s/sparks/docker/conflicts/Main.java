package fr.unice.i3s.sparks.docker.conflicts;

import fr.unice.i3s.sparks.docker.DockerFile;
import fr.unice.i3s.sparks.docker.DockerfileLexer;
import fr.unice.i3s.sparks.docker.DockerfileParser;
import fr.unice.i3s.sparks.docker.conflicts.commands.Command;
import fr.unice.i3s.sparks.docker.conflicts.run.RUNComflictSniffer;
import fr.unice.i3s.sparks.docker.conflicts.run.RUNConcflict;
import fr.unice.i3s.sparks.docker.grammar.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws MalFormedImageException, IOException {

        List<DockerFile> dockerfiles = new ArrayList<>();


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

        File folder = new File("/Users/benjaminbenni/Work/githug.dk.crawler/dockerfiles");

        File[] files = folder.listFiles(textFilter);
        for(File f : files) {
            System.err.println("Handling file:"+ f.getAbsolutePath());
            List<String> strings = FileUtils.readLines(f, "utf-8");

            String sentence = "";

            for(String s : strings) {
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

            DockerFile dockerFile = listener.getDockerfile();
            dockerfiles.add(dockerFile);
        }


        System.out.println(files.length + " dockerfiles handled.");
        System.out.println(dockerfiles.size() + " dockerfiles parsed into model.");

        dockerfiles.forEach(dockerFile -> {
            List<RUNConcflict> conflict = new RUNComflictSniffer().conflict(dockerFile);
            System.out.println(conflict);
        });

        /*
        Image root1 = new Image(
                new ImageID("root1"),
                Arrays.asList(
                        new ENVCommand("JAVA_HOME", "/toto/tata"),
                        new ENVCommand("GOPATH", "/toto/tutu"),
                        new ENVCommand("CATALINA_HOME", "/toto/titi")
                )
        );

        Image image11 = new Image(
                new ImageID("image11"),
                Arrays.asList(
                        new FROMCommand(new ImageID("root1")),
                        new ENVCommand("JAVA_HOME", "/plop/plop"),
                        new RUNCommand(),
                        new ENVCommand("POUET", "a value")
                )
        );

        Image image12 = new Image(
                new ImageID("image12"),
                Arrays.asList(
                        new FROMCommand(new ImageID("root1")),
                        new RUNCommand(),
                        new RUNCommand(),
                        new ENVCommand("TATA", "a value")
                )
        );

        Image image111 = new Image(
                new ImageID("image111"),
                Arrays.asList(
                        new FROMCommand(new ImageID("image11")),
                        new RUNCommand(),
                        new RUNCommand(),
                        new ENVCommand("GOPATH", "/azerty/qwerty")
                )
        );

        Image image112 = new Image(
                new ImageID("image112"),
                Arrays.asList(
                        new FROMCommand(new ImageID("image11")),
                        new RUNCommand(),
                        new RUNCommand(),
                        new ENVCommand("JAVA_HOME", "/arg/arg/"),
                        new ENVCommand("VARFOO", "/foo/bar")

                )
        );

        Image root2 = new Image(
                new ImageID("root2"),
                Arrays.asList(
                        new ENVCommand("JAVA_HOME", "/foo/bar"),
                        new ENVCommand("GOPATH", "/a/b")
                )
        );
        Image image21 = new Image(
                new ImageID("image21"),
                Arrays.asList(
                        new FROMCommand(new ImageID("root2")),
                        new ENVCommand("CATALINA_HOME", "/aser/aser")
                )
        );

        Image image211 = new Image(
                new ImageID("image211"),
                Arrays.asList(
                        new FROMCommand(new ImageID("image21")),
                        new ENVCommand("CATALINA_HOME", "/override/override")
                )
        );

        Image image212 = new Image(
                new ImageID("image212"),
                Arrays.asList(
                        new FROMCommand(new ImageID("image21")),
                        new ENVCommand("JAVA_HOME", "/override/override")
                )
        );



        Sniffer.analyze(root1, image11, image12, image111, image112, image21, image211, image212, root2);
        */
    }
}
