package fr.unice.i3s.sparks.docker.core.conflicts.run;


import fr.unice.i3s.sparks.docker.core.conflicts.Enricher;
import fr.unice.i3s.sparks.docker.core.conflicts.RunIssue1;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.analyser.DockerFileParser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertSame;

public class AptUpdateAptInstallIssueTest {

    @Test
    public void test() throws IOException {
        Dockerfile parse = DockerFileParser.parse(new File("src/test/resources/run_issue.dockerfile"));
        parse = Enricher.enrich(parse);

        RunIssue1 r = new RunIssue1();
        List<RunIssue1.Issue> apply = r.apply(parse);

        assertEquals(2, apply.size());
        assertSame(apply.get(0).getRunCommand(), parse.getListOfCommand().get(2));
        assertSame(apply.get(1).getRunCommand(), parse.getListOfCommand().get(3));

        System.out.println(parse.getListOfCommand().get(2));
        System.out.println(parse.getListOfCommand().get(3));
    }
}
