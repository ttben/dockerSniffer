package fr.unice.i3s.sparks.docker.core.conflicts.run;

import fr.unice.i3s.sparks.docker.core.conflicts.Enricher;
import fr.unice.i3s.sparks.docker.core.conflicts.tags.AptInstallTag;
import fr.unice.i3s.sparks.docker.core.conflicts.tags.AptUpdateTag;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.DockerFileParser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MultiLinesRun1Test {
    @Test
    public void complexMultiLineRun1() throws IOException {
        File f = new File(MultiLinesRun1Test.class.getClassLoader().getResource("ComplexMultiLineRun1").getPath());
        Dockerfile result = DockerFileParser.parse(f);
        result = Enricher.enrich(result);
        assertEquals(1, result.getActions().size());

        assertEquals(0, result.howMuch(FROMCommand.class));
        assertEquals(0, result.howMuch(MAINTAINERCommand.class));
        assertEquals(1, result.howMuch(RUNCommand.class));
        assertEquals(0, result.howMuch(ENTRYPointCommand.class));
        assertEquals(0, result.howMuch(ENVCommand.class));
        assertEquals(0, result.howMuch(ARGCommand.class));
        assertEquals(0, result.howMuch(COPYCommand.class));
        assertEquals(0, result.howMuch(WORKDIRCommand.class));
        assertEquals(0, result.howMuch(CMDCommand.class));
        assertEquals(0, result.howMuch(ONBUILDCommand.class));
        assertEquals(4, result.howMuch(ShellCommand.class));

        List<RUNCommand> actionsOfType = result.getActionsOfType(RUNCommand.class);
        int nbOfUpdate = 0 , nbOfInstall = 0;

        RUNCommand runCommand = actionsOfType.get(0);
        assertTrue(runCommand.containsTag(AptInstallTag.class));
        assertTrue(runCommand.containsTag(AptUpdateTag.class));
    }

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


    }
}
