package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.DockerFileParser;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class _1FromFirstTest {
    @Test
    public void basicTest1() {
        Dockerfile dockerfile = new Dockerfile(
                new WORKDIRCommand("./r"),
                new FROMCommand(new ImageID("a"))
        );

        assertTrue(_1FromFirst.conflict(dockerfile));
    }

    @Test
    public void basicTest2() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new ENVCommand("e", "e"),
                new ADDCommand("htp", "/lol"),
                new ADDCommand("https://www.lol.com"),
                new USERCommand("root"),
                new USERCommand("root"),
                new USERCommand("root"),
                new WORKDIRCommand("/usr/toto"),
                new USERCommand("root"),
                new USERCommand("user")
        );

        assertFalse(_1FromFirst.conflict(dockerfile));
    }

    @Test
    public void fileTestNoConflict() throws IOException, URISyntaxException {
        Dockerfile dockerfile = DockerFileParser.parse(new File(_1FromFirst.class.getClassLoader().getResource("_1FromFirst_NoConflict").toURI()));
        assertFalse(_1FromFirst.conflict(dockerfile));
    }

    @Test
    public void fileTestFail() throws IOException, URISyntaxException {
        Dockerfile dockerfile = DockerFileParser.parse(new File(_1FromFirst.class.getClassLoader().getResource("_1FromFirst_Conflict").toURI()));
        assertTrue(_1FromFirst.conflict(dockerfile));
    }
}