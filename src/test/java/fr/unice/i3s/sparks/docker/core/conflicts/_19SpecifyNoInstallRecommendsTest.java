package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.conflicts.tags.AptInstallTag;
import fr.unice.i3s.sparks.docker.core.model.ImageID;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class _19SpecifyNoInstallRecommendsTest {

    @Test
    public void shouldYieldWarning() {
        ShellCommand shellCommand = new ShellCommand("apt-get", "install", "-y", "ruby:203", "quby:203");
        shellCommand.addTag(new AptInstallTag());

        RUNCommand runCommand = new RUNCommand(shellCommand);
        runCommand.addTag(new AptInstallTag());

        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                runCommand
        );

        List<Command> conflict = _19SpecifyNoInstallRecommends.conflict(dockerfile);
        assertFalse(conflict.isEmpty());

        ShellCommand shellCommand1 = new ShellCommand("apt-get", "install", "-y", "java", "ruby");
        shellCommand1.addTag(new AptInstallTag());

        RUNCommand runCommand1 = new RUNCommand(shellCommand1);
        runCommand1.addTag(new AptInstallTag());

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                runCommand1
        );

        conflict = _19SpecifyNoInstallRecommends.conflict(dockerfile);
        assertFalse(conflict.isEmpty());

        ShellCommand shellCommand2 = new ShellCommand("apt-get", "install", "ruby");
        shellCommand2.addTag(new AptInstallTag());

        RUNCommand runCommand2 = new RUNCommand(shellCommand2);
        runCommand2.addTag(new AptInstallTag());

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                runCommand2
        );

        conflict = _19SpecifyNoInstallRecommends.conflict(dockerfile);
        assertFalse(conflict.isEmpty());

        ShellCommand shellCommand3 = new ShellCommand("apt-get", "install", "ruby:4", "wget");
        shellCommand3.addTag(new AptInstallTag());

        RUNCommand runCommand3 = new RUNCommand(shellCommand3);
        runCommand3.addTag(new AptInstallTag());

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                runCommand3
        );

        conflict = _19SpecifyNoInstallRecommends.conflict(dockerfile);
        assertFalse(conflict.isEmpty());
    }


    @Test
    public void shouldNotYieldWarning() {
        ShellCommand shellCommand = new ShellCommand("apt-get", "install", "-y", "--no-install-recommends", "ruby:203", "quby:203");
        shellCommand.addTag(new AptInstallTag());

        RUNCommand runCommand = new RUNCommand(shellCommand);
        runCommand.addTag(new AptInstallTag());

        Dockerfile dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                runCommand
        );

        List<Command> conflict = _19SpecifyNoInstallRecommends.conflict(dockerfile);
        assertTrue(conflict.isEmpty());

        ShellCommand shellCommand1 = new ShellCommand("apt-get", "install", "-y", "java", "ruby", "--no-install-recommends");
        shellCommand1.addTag(new AptInstallTag());

        RUNCommand runCommand1 = new RUNCommand(shellCommand1);
        runCommand1.addTag(new AptInstallTag());

        dockerfile = new Dockerfile(
                new FROMCommand(new ImageID("a")),
                runCommand1
        );

        conflict = _19SpecifyNoInstallRecommends.conflict(dockerfile);
        assertTrue(conflict.isEmpty());

    }
}