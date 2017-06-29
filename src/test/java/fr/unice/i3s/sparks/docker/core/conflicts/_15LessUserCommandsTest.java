package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class _15LessUserCommandsTest {


    @Test
    public void belowThresholdShouldNotThrow() {
        int threshold = _15LessUserCommands.THRESHOLD;

        List<Command> commands = new ArrayList<>();

        commands.add(new FROMCommand(new ImageID("a")));
        commands.add(new ENVCommand("e", "e"));

        for (int i = 0; i < threshold; i++) {
            commands.add(new USERCommand("test" + i));
        }

        Dockerfile dockerfile = new Dockerfile(commands);

        List<Command> conflict = _15LessUserCommands.conflict(dockerfile);
        assertTrue(conflict.isEmpty());
    }

    @Test
    public void belowThresholdShouldThrow() {
        int threshold = _15LessUserCommands.THRESHOLD;

        List<Command> commands = new ArrayList<>();

        commands.add(new FROMCommand(new ImageID("a")));
        commands.add(new ENVCommand("e", "e"));

        for (int i = 0; i < threshold + 10; i++) {
            commands.add(new USERCommand("test" + i));
        }

        Dockerfile dockerfile = new Dockerfile(commands);

        List<Command> conflict = _15LessUserCommands.conflict(dockerfile);
        assertFalse(conflict.isEmpty());
    }
}