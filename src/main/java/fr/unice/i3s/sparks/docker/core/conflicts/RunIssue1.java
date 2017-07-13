package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.conflicts.tags.AptInstallTag;
import fr.unice.i3s.sparks.docker.core.conflicts.tags.AptUpdateTag;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.AptInstall;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ShellCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.AptUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RunIssue1 {
    public List<Issue> apply(Dockerfile dockerfile) {
        List<Issue> issues = new ArrayList<>();

        ArrayList<RUNCommand> runCommands = dockerfile.getActions()
                .stream()
                .filter(c -> c instanceof RUNCommand)
                .map(RUNCommand.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));

        for (RUNCommand runCommand : runCommands) {
            boolean flawed = false;

            boolean install = false, update = false;

            List<ShellCommand> body = runCommand.getBody();
            for (ShellCommand shellCommand : body) {
                if (shellCommand.containsTag(AptInstallTag.class)) {
                    install=true;
                }
                if (shellCommand.containsTag(AptUpdateTag.class)) {
                    update = true;
                }
            }

            flawed = !update && install;

            if(flawed) {
                issues.add(new Issue(dockerfile, runCommand));
                //runCommand.getBody().add(0, new AptUpdate("apt-get", "update"));
            }
        }

        return issues;
    }

    public class Issue {

        private Dockerfile dockerfile;
        private RUNCommand runCommand;

        public Issue(Dockerfile dockerfile, RUNCommand runCommand) {
            this.dockerfile = dockerfile;
            this.runCommand = runCommand;
        }

        public RUNCommand getRunCommand() {
            return runCommand;
        }

        public Dockerfile getDockerfile() {
            return dockerfile;
        }
    }
}
