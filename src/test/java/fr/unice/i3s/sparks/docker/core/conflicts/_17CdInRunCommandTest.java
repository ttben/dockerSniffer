package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.FROMCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ShellCommand;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class _17CdInRunCommandTest {
    @Test
    public void shouldYieldWarning() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new ShellCommand("cd", ".."), new ShellCommand("mkdir", "toto"))
        );

        List<Command> conflict = _17CdInRunCommand.conflict(dockerfile);
        assertFalse(conflict.isEmpty());
    }

    @Test
    public void shouldNotYieldWarning() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new ShellCommand("cdirecto", "lolilol"), new ShellCommand("mkdir", "toto"))
        );

        List<Command> conflict = _17CdInRunCommand.conflict(dockerfile);
        assertTrue(conflict.isEmpty());
    }
}