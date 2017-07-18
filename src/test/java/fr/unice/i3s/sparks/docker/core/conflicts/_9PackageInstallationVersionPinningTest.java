package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.conflicts.tags.AptInstallTag;
import fr.unice.i3s.sparks.docker.core.conflicts.tags.AptUpdateTag;
import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class _9PackageInstallationVersionPinningTest {

    @Test
    public void versionIsPinned1() {
        ShellCommand shellCommand = new ShellCommand("apt-get", "install", "-y", "ruby:203");
        RUNCommand runCommand = new RUNCommand(shellCommand);
        runCommand.addTag(new AptInstallTag());
        shellCommand.addTag(new AptInstallTag());
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                runCommand
        );

        List<Command> conflict = _9PackageInstallationVersionPinning.conflict(dockerfile);
        assertTrue(conflict.isEmpty());
    }

    @Test
    public void versionIsNotPinned1() {
        ShellCommand shellCommand = new ShellCommand("apt-get", "install", "-y", "ruby");
        shellCommand.addTag(new AptInstallTag());      // todo tags must be handled in a upstream tree (tag a child must tag the parent)

        RUNCommand runCommand = new RUNCommand(shellCommand);
        runCommand.addTag(new AptInstallTag());

        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                runCommand
        );

        List<Command> conflict = _9PackageInstallationVersionPinning.conflict(dockerfile);
        assertFalse(conflict.isEmpty());
    }

    @Test
    public void versionIsNotPinned2() {
        ShellCommand shellCommand = new ShellCommand("apt-get", "install", "ruby");
        shellCommand.addTag(new AptInstallTag());      // todo tags must be handled in a upstream tree (tag a child must tag the parent)
        RUNCommand runCommand = new RUNCommand(shellCommand);
        runCommand.addTag(new AptInstallTag());

        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                runCommand
        );

        List<Command> conflict = _9PackageInstallationVersionPinning.conflict(dockerfile);
        assertFalse(conflict.isEmpty());
    }

    @Test
    public void versionIsPinned2() {

        ShellCommand shellCommand = new ShellCommand("apt-get", "install", "ruby:4");
        RUNCommand runCommand = new RUNCommand(shellCommand);
        runCommand.addTag(new AptInstallTag());
        shellCommand.addTag(new AptInstallTag());

        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                runCommand
        );

        List<Command> conflict = _9PackageInstallationVersionPinning.conflict(dockerfile);
        assertTrue(conflict.isEmpty());
    }
}