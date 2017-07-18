package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.conflicts.run.MultiLinesRun1Test;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.DockerFileParser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class _8AlwaysUpdateAndInstallOnSameCommandTest {
    @Test
    public void complexMultiLineRun2() throws IOException {
        File f = new File(MultiLinesRun1Test.class.getClassLoader().getResource("ComplexMultiLineRun2").getPath());
        Dockerfile result = DockerFileParser.parse(f);
        result = Enricher.enrich(result);
        assertEquals(15, result.getActions().size());
        assertEquals(1, result.howMuch(FROMCommand.class));
        assertEquals(1, result.howMuch(MAINTAINERCommand.class));
        assertEquals(6, result.howMuch(RUNCommand.class));
        assertEquals(1, result.howMuch(ENTRYPointCommand.class));
        assertEquals(2, result.howMuch(ENVCommand.class));
        assertEquals(0, result.howMuch(ARGCommand.class));
        assertEquals(2, result.howMuch(COPYCommand.class));
        assertEquals(0, result.howMuch(WORKDIRCommand.class));
        assertEquals(1, result.howMuch(CMDCommand.class));
        assertEquals(0, result.howMuch(ONBUILDCommand.class));
        assertEquals(1, result.howMuch(VOLUMECommand.class));
        assertEquals(15, result.howMuch(ShellCommand.class));

        _8AlwaysUpdateAndInstallOnSameCommand a = new _8AlwaysUpdateAndInstallOnSameCommand();
        Map<Dockerfile, List<RunIssue1.Issue>> apply = a.apply(Arrays.asList(result));

        assertEquals(15, result.getActions().size());
        assertEquals(1, result.howMuch(FROMCommand.class));
        assertEquals(1, result.howMuch(MAINTAINERCommand.class));
        assertEquals(6, result.howMuch(RUNCommand.class));
        assertEquals(1, result.howMuch(ENTRYPointCommand.class));
        assertEquals(2, result.howMuch(ENVCommand.class));
        assertEquals(0, result.howMuch(ARGCommand.class));
        assertEquals(2, result.howMuch(COPYCommand.class));
        assertEquals(0, result.howMuch(WORKDIRCommand.class));
        assertEquals(1, result.howMuch(CMDCommand.class));
        assertEquals(0, result.howMuch(ONBUILDCommand.class));
        assertEquals(1, result.howMuch(VOLUMECommand.class));
        assertEquals(15, result.howMuch(ShellCommand.class));

        assertFalse(apply.containsKey(result));
    }

}
