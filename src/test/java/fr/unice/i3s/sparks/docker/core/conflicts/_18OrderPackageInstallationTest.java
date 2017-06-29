package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.AptInstall;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.FROMCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.RUNCommand;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class _18OrderPackageInstallationTest {

    @Test
    public void shouldYieldWarning() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new AptInstall("apt-get", "install", "-y", "ruby:203", "quby:203"))
        );

        List<Command> conflict = _18OrderPackageInstallation.conflict(dockerfile);
        assertFalse(conflict.isEmpty());

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new AptInstall("apt-get", "install", "-y", "ruby:203", "aeuby:203"))
        );

        conflict = _18OrderPackageInstallation.conflict(dockerfile);
        assertFalse(conflict.isEmpty());

    }
    @Test
    public void shouldNotYieldWarning() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new AptInstall("apt-get", "install", "-y", "java", "ruby"))
        );

        List<Command> conflict = _18OrderPackageInstallation.conflict(dockerfile);
        assertTrue(conflict.isEmpty());

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new AptInstall("apt-get", "install", "ruby"))
        );

        conflict = _18OrderPackageInstallation.conflict(dockerfile);
        assertTrue(conflict.isEmpty());

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new AptInstall("apt-get", "install", "ruby:4", "wget"))
        );

        conflict = _18OrderPackageInstallation.conflict(dockerfile);
        assertTrue(conflict.isEmpty());


        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                new RUNCommand(new AptInstall("apt-get", "install", "ruby:4", "zeubi"))
        );

        conflict = _18OrderPackageInstallation.conflict(dockerfile);
        assertTrue(conflict.isEmpty());
    }
}