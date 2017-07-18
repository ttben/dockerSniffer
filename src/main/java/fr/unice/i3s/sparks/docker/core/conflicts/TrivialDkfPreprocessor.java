package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static fr.unice.i3s.sparks.docker.core.conflicts.Main.SILENT;

public class TrivialDkfPreprocessor extends Preprocessor<Dockerfile> {
    @Override
    public List<Dockerfile> apply(List<Dockerfile> dockerfiles) {
        int trivialThreshold = 2;
        int nbDkF = dockerfiles.size();

        List<Dockerfile> trivialDockerfiles = new ArrayList<>();

        ListIterator<Dockerfile> dockerfileListIterator = dockerfiles.listIterator();
        while (dockerfileListIterator.hasNext()) {
            Dockerfile dockerfile = dockerfileListIterator.next();

            if (dockerfile.getActions().size() < trivialThreshold) {
                trivialDockerfiles.add(dockerfile);
                dockerfileListIterator.remove();
            }
        }


        if (!SILENT) {
            System.out.println(nbDkF + "  files.");
            Main.percentageOf(trivialDockerfiles.size(), nbDkF, "of files are trivial");
            System.out.println(dockerfiles.size() + " non-trivial files remain.");
        }


        return dockerfiles;
    }
}
