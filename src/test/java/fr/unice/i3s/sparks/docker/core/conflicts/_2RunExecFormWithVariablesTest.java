package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.DockerFileParser;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.FROMCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ShellCommand;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.*;

public class _2RunExecFormWithVariablesTest {
    @Test
    public void shouldBeConflicting() {
        FROMCommand a = new FROMCommand(new ImageID("a"));
        RUNCommand runCommand = new RUNCommand(new ShellCommand("echo", "$HOME"));
        Dockerfile dockerfile = new Dockerfile(
                a,
                runCommand
        );

        List<Command> conflict = _2RunExecFormWithVariables.conflict(dockerfile);
        assertFalse(conflict.isEmpty());
        assertEquals(runCommand, conflict.get(0));
    }

    @Test
    public void shouldNotBeConflicting() {
        FROMCommand a = new FROMCommand(new ImageID("a"));
        RUNCommand runCommand = new RUNCommand(new ShellCommand("/bin/sh", "$HOME"));
        Dockerfile dockerfile = new Dockerfile(
                a,
                runCommand
        );

        List<Command> conflict = _2RunExecFormWithVariables.conflict(dockerfile);
        assertTrue(conflict.isEmpty());
    }

    @Test
    public void fileTestNoConflict() throws IOException, URISyntaxException {
        Dockerfile dockerfile = DockerFileParser.parse(new File(_2RunExecFormWithVariables.class.getClassLoader().getResource("_2RunExecFormWithVariables_NoConflict").toURI()));
        assertTrue(_2RunExecFormWithVariables.conflict(dockerfile).isEmpty());
    }

    @Test
    public void fileTestConflict() throws IOException, URISyntaxException {
        Dockerfile dockerfile = DockerFileParser.parse(new File(_2RunExecFormWithVariables.class.getClassLoader().getResource("_2RunExecFormWithVariables_Conflict").toURI()));
        assertFalse(_2RunExecFormWithVariables.conflict(dockerfile).isEmpty());
        assertEquals(2, _2RunExecFormWithVariables.conflict(dockerfile).size());
    }
}