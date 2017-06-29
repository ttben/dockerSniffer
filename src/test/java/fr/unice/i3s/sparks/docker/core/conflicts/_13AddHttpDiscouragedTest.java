package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ADDCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ENVCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.FROMCommand;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class _13AddHttpDiscouragedTest {
    @Test
    public void shouldYieldWarning() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new ENVCommand("e", "e"),
                new ADDCommand("https://www.lol.com")
        );

        List<Command> conflict = _13AddHttpDiscouraged.conflict(dockerfile);
        assertFalse(conflict.isEmpty());
    }

    @Test
    public void shouldNotYieldWarning() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new ENVCommand("e", "e"),
                new ADDCommand("htp", "/lol")
        );

        List<Command> conflict = _13AddHttpDiscouraged.conflict(dockerfile);
        assertTrue(conflict.isEmpty());
    }
}