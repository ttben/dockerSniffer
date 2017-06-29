package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class _16WorkdirAbsolutePathTest {
    @Test
    public void shouldYieldWarning() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new WORKDIRCommand("./r")
        );

        List<Command> conflict = _16WorkdirAbsolutePath.conflict(dockerfile);
        assertFalse(conflict.isEmpty());
    }

    @Test
    public void shouldNotYieldWarning() {

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

        List<Command> conflict = _16WorkdirAbsolutePath.conflict(dockerfile);
        assertTrue(conflict.isEmpty());
    }


}