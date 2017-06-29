package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class _5CmdExecFormWithVariablesTest {

    @Test
    public void testShouldConflict() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new ShellCommand("echo", "$HOME")),
                new CMDCommand("echo", "$HOME")
        );

        List<Command> conflict = _5CmdExecFormWithVariables.conflict(dockerfile);
        assertFalse(conflict.isEmpty());
    }


    @Test
    public void testShouldNotConflict() {

        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new ShellCommand("/bin/sh", "$HOME")),
                new CMDCommand("/bin/sh", "$HOME")
        );

        List<Command> conflict = _5CmdExecFormWithVariables.conflict(dockerfile);
        assertTrue(conflict.isEmpty());
    }


}