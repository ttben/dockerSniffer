package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class _7AptGetUpgradeTest {

    @Test
    public void shouldYieldAptUpgradeIssue1() {
        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("lol")),
                new ENVCommand("e", "e"),
                new RUNCommand(new ShellCommand("apt-get", "upgrade"), new ShellCommand("apt-get", "install", "X"))
        );

        List<Command> conflict = _7AptGetUpgrade.conflict(dockerfile);
        assertFalse(conflict.isEmpty());
    }

    @Test
    public void shouldYieldAptUpgraIssue2() {

        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("lol")),
                new ENVCommand("e", "e"),
                new RUNCommand(new ShellCommand("apt-get", "install", "X"), new ShellCommand("apt-get", "upgrade"))
        );

        List<Command> conflict = _7AptGetUpgrade.conflict(dockerfile);
        assertFalse(conflict.isEmpty());
    }

    @Test
    public void shouldNotYieldAptUpgradeIssue() {

        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("lol")),
                new ENVCommand("e", "e"),
                new RUNCommand(new ShellCommand("apt-get", "install", "X"), new ShellCommand("apt-get", "upgrad"))
        );

        List<Command> conflict = _7AptGetUpgrade.conflict(dockerfile);
        assertTrue(conflict.isEmpty());
    }
}