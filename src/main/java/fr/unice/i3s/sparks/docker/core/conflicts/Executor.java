package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.uca.i3s.sparks.composition.metamodel.Check;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Executor {
    public Map<Check, Map<Dockerfile, Object>> apply(List<Dockerfile> dockerfiles, List<Check> checks) {
        Map<Check, Map<Dockerfile, Object>> results = new HashMap<>();
        for (int i = 0; i < checks.size(); i++) {
            Check check = checks.get(i);
            Map<Dockerfile, Object> apply = (Map<Dockerfile, Object>) check.apply(dockerfiles);
            if (apply != null) {
                if (!apply.isEmpty()) {
                    results.put(check, apply);
                }
            }
        }
        return results;
    }
}
