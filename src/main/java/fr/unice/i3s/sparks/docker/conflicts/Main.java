package fr.unice.i3s.sparks.docker.conflicts;

import fr.unice.i3s.sparks.docker.conflicts.commands.ENVCommand;
import fr.unice.i3s.sparks.docker.conflicts.commands.FROMCommand;
import fr.unice.i3s.sparks.docker.conflicts.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.conflicts.env.ENVConflictSniffer;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws MalFormedImageException {

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
    }
}
