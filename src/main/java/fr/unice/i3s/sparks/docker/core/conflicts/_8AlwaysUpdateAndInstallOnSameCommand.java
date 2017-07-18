package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _8AlwaysUpdateAndInstallOnSameCommand extends Check<Dockerfile, List<RunIssue1.Issue>>{
    public static List<RunIssue1.Issue> conflict(Dockerfile dockerfile) {
        RunIssue1 o = new RunIssue1();
        List<RunIssue1.Issue> apply = o.apply(dockerfile);
        return apply;
    }

    @Override
    public Map<Dockerfile, List<RunIssue1.Issue>> apply(List<Dockerfile> dockerfiles) {
        Map<Dockerfile, List<RunIssue1.Issue>> result = new HashMap<>();

        for (Dockerfile dockerfile : dockerfiles) {
            List<RunIssue1.Issue> conflict = conflict(dockerfile);
            if (!conflict.isEmpty()) {
                result.put(dockerfile, conflict);
            }
        }

        return result;
    }
}
