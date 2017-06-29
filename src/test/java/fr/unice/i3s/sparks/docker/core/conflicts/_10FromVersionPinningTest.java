package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.FROMCommand;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class _10FromVersionPinningTest {

    @Test
    public void shouldYieldNotPinnedVersion() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("ubuntu"))
        );

        List<Command> conflict = _10FromVersionPinning.conflict(dockerfile);
        assertFalse(conflict.isEmpty());
    }
    @Test
    public void shouldNotYieldNotPinnedVersion() {



        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("ubuntu:1.0-light"))
        );

        List<Command> conflict = _10FromVersionPinning.conflict(dockerfile);
        assertTrue(conflict.isEmpty());
    }

    @Test
    public void handleLatestTag() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("ubuntu:latest"))
        );

        List<Command> conflict = _10FromVersionPinning.conflict(dockerfile);
        assertFalse(conflict.isEmpty());
    }
}