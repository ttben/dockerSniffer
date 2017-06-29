package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.AptInstall;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.FROMCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.RUNCommand;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class _9PackageInstallationVersionPinningTest {

    @Test
    public void versionIsPinned1() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new AptInstall("apt-get", "install", "-y", "ruby:203"))
        );

        List<Command> conflict = _9PackageInstallationVersionPinning.conflict(dockerfile);
        assertTrue(conflict.isEmpty());
    }

    @Test
    public void versionIsNotPinned1() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new AptInstall("apt-get", "install", "-y", "ruby"))
        );

        List<Command> conflict = _9PackageInstallationVersionPinning.conflict(dockerfile);
        assertFalse(conflict.isEmpty());
    }

    @Test
    public void versionIsNotPinned2() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new AptInstall("apt-get", "install", "ruby"))
        );

        List<Command> conflict = _9PackageInstallationVersionPinning.conflict(dockerfile);
        assertFalse(conflict.isEmpty());
    }

    @Test
    public void versionIsPinned2() {

        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new AptInstall("apt-get", "install", "ruby:4"))
        );

        List<Command> conflict = _9PackageInstallationVersionPinning.conflict(dockerfile);
        assertTrue(conflict.isEmpty());
    }
}