package fr.unice.i3s.sparks.docker.core.conflicts.run;

import fr.unice.i3s.sparks.docker.core.conflicts.Enricher;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.parser.DockerFileParser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

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
        assertEquals(1, result.howMuch(AptInstall.class));
        assertEquals(1, result.howMuch(AptUpdate.class));
    }
}
