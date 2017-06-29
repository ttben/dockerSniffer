package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ADDCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ENVCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.FROMCommand;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class _12AddDiscouragedTest {

    @Test
    public void belowThresholdShouldNotThrow() {
        int threshold = _12AddDiscouraged.THRESHOLD;

        List<Command> commands = new ArrayList<>();

        commands.add(new FROMCommand(new ImageID("a")));
        commands.add(new ENVCommand("e", "e"));

        for (int i = 0; i < threshold -1 ; i++) {
            commands.add(new ADDCommand("test" + i));
        }

        Dockerfile dockerfile = new Dockerfile(commands);

        List<Command> conflict = _12AddDiscouraged.conflict(dockerfile);
        assertTrue(conflict.isEmpty());
    }

    @Test
    public void belowThresholdShouldThrow() {
        int threshold = _12AddDiscouraged.THRESHOLD;

        List<Command> commands = new ArrayList<>();

        commands.add(new FROMCommand(new ImageID("a")));
        commands.add(new ENVCommand("e", "e"));

        for (int i = 0; i < threshold + 10; i++) {
            commands.add(new ADDCommand("test" + i));
        }

        Dockerfile dockerfile = new Dockerfile(commands);

        List<Command> conflict = _12AddDiscouraged.conflict(dockerfile);
        assertFalse(conflict.isEmpty());
    }
}