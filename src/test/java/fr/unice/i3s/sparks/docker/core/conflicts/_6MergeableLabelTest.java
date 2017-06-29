package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.FROMCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.LABELCommand;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class _6MergeableLabelTest {
    @Test
    public void shouldNotMergeLabel() {
        Dockerfile dockerfile = new Dockerfile(new LABELCommand("a"), new FROMCommand(new ImageID("a")), new LABELCommand("b"));
        List<List<Command>> conflict = _6MergeableLabel.conflict(dockerfile);
        assertTrue(conflict.isEmpty());
    }

    @Test
    public void shouldMergeLabel() {
        Dockerfile dockerfile = new Dockerfile(new FROMCommand(new ImageID("a")), new LABELCommand("a"), new LABELCommand("b"));
        List<List<Command>> conflict = _6MergeableLabel.conflict(dockerfile);
        assertFalse(conflict.isEmpty());
    }
}