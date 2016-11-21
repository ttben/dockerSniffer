package fr.unice.i3s.sparks.docker.conflicts.run;

import fr.unice.i3s.sparks.docker.Enricher;
import fr.unice.i3s.sparks.docker.Install;
import fr.unice.i3s.sparks.docker.Update;
import fr.unice.i3s.sparks.docker.conflicts.commands.*;
import fr.unice.i3s.sparks.docker.core.DockerFile;
import fr.unice.i3s.sparks.docker.core.ImageID;
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

    public DockerFile buildDockerfile1() {
        DockerFile dockerFile = new DockerFile(
                new FROMCommand(new ImageID("anImageID")),
                new ENVCommand("aKey", "aValue")
        );

        return Enricher.enrich(dockerFile);
    }

    public DockerFile buildDockerfile2() {
        DockerFile dockerFile = new DockerFile(
                new FROMCommand(new ImageID("anImageID")),
                new ENVCommand("aKey", "aValue"),
                new RUNCommand(new Update("apt-get", "-y", "update"))
        );

        return dockerFile;
    }

    public DockerFile buildDockerfile3() {
        DockerFile dockerFile = new DockerFile(
                new FROMCommand(new ImageID("anImageID")),
                new ENVCommand("aKey", "aValue"),
                new RUNCommand(new Update("apt-get", "-y", "update")),
                new RUNCommand(new ShellCommand("python", "--f", "pouet"))
        );

        return dockerFile;
    }

    public DockerFile buildDockerfile4() {
        DockerFile dockerFile = new DockerFile(
                new FROMCommand(new ImageID("anImageID")),
                new ENVCommand("aKey", "aValue"),
                new RUNCommand(new Update("apt-get", "-y", "update")),
                new RUNCommand(new Install("apt-get", "install", "java"))
        );

        return dockerFile;
    }

    public DockerFile buildDockerfile5() {
        DockerFile dockerFile = new DockerFile(
                new FROMCommand(new ImageID("anImageID")),
                new ENVCommand("aKey", "aValue"),
                new RUNCommand(new Update("apt-get", "-y", "update"), new Install("apt-get", "install", "java")),
                new ENVCommand("anotherKey", "anotherVAlue")
        );

        return dockerFile;
    }
}