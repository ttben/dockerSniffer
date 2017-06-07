package fr.unice.i3s.sparks.docker.core.conflicts.run;


import fr.unice.i3s.sparks.docker.core.conflicts.Enricher;
import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class RUNConflictSnifferTest {

    @Test
    public void withoutRUNCommands_SnifferShouldReturnNull() {
        RUNConflictSniffer runConflictSniffer = new RUNConflictSniffer();
        RUNConflict conflict = runConflictSniffer.conflict(buildDockerfile1());
        assertNull(conflict);
    }

    @Test
    public void withASingleRunCommand_SnifferShouldReturnEmpty() {
        RUNConflictSniffer runConflictSniffer = new RUNConflictSniffer();
        RUNConflict conflict = runConflictSniffer.conflict(buildDockerfile2());
        assertTrue(conflict.isEmpty());
    }

    @Test
    public void withMultipleRunWithoutConflict_SnifferShouldReturnEmpty() {
        RUNConflictSniffer runConflictSniffer = new RUNConflictSniffer();
        RUNConflict conflict = runConflictSniffer.conflict(buildDockerfile3());
        assertTrue(conflict.isEmpty());
    }

    @Test
    public void withConflict_SnifferShouldNotReturnEmpty() {
        RUNConflictSniffer runConflictSniffer = new RUNConflictSniffer();

        RUNConflict conflict = runConflictSniffer.conflict(buildDockerfile4());
        LinkedList<RUNCommand> conflictingRUNCommand = conflict.getConflictingRUNCommand();

        assertFalse(conflict.isEmpty());
        assertEquals(2, conflictingRUNCommand.size());
    }

    @Test
    public void withOneRunOnSingleLine_SnifferShouldNotRaisedConflict() {
        RUNConflictSniffer runConflictSniffer = new RUNConflictSniffer();
        RUNConflict conflict = runConflictSniffer.conflict(buildDockerfile5());
        assertTrue(conflict.isEmpty());

    }

    public Dockerfile buildDockerfile1() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("anImageID")),
                new ENVCommand("aKey", "aValue")
        );

        return Enricher.enrich(dockerfile);
    }

    public Dockerfile buildDockerfile2() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("anImageID")),
                new ENVCommand("aKey", "aValue"),
                new RUNCommand(new Update("apt-get", "-y", "update"))
        );

        return dockerfile;
    }

    public Dockerfile buildDockerfile3() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("anImageID")),
                new ENVCommand("aKey", "aValue"),
                new RUNCommand(new Update("apt-get", "-y", "update")),
                new RUNCommand(new ShellCommand("python", "--f", "pouet"))
        );

        return dockerfile;
    }

    public Dockerfile buildDockerfile4() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("anImageID")),
                new ENVCommand("aKey", "aValue"),
                new RUNCommand(new Update("apt-get", "-y", "update")),
                new RUNCommand(new Install("apt-get", "install", "java"))
        );

        return dockerfile;
    }

    public Dockerfile buildDockerfile5() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("anImageID")),
                new ENVCommand("aKey", "aValue"),
                new RUNCommand(new Update("apt-get", "-y", "update"), new Install("apt-get", "install", "java")),
                new ENVCommand("anotherKey", "anotherVAlue")
        );

        return dockerfile;
    }
}