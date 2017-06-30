package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.conflicts.cmd.CMDConflict;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.CMDCommand;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class _3MultipleCMDTest {

    @Test
    public void shouldYieldWarning() {
        Dockerfile aDockerfile = new Dockerfile();
        CMDCommand node = new CMDCommand(aDockerfile, "node");
        CMDCommand node1 = new CMDCommand(aDockerfile, "node");
        aDockerfile.add(node);
        aDockerfile.add(node1);

        assertTrue(_3MultipleCMD.conflict(aDockerfile));
    }

    @Test
    public void shouldNotYieldWarning() {
        Dockerfile aDockerfile = new Dockerfile();
        CMDCommand node = new CMDCommand(aDockerfile, "node");
        aDockerfile.add(node);

        assertFalse(_3MultipleCMD.conflict(aDockerfile));
    }
}