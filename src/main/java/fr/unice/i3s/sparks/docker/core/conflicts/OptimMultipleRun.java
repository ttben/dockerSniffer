package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.Command;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ShellCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class OptimMultipleRun {
    public List<List<Issue>> apply(Dockerfile dockerfile) {
        List<Command> listOfCommand = dockerfile.getActions();
        List<List<OptimMultipleRun.Issue>> issuesFound = new ArrayList<>();
        ListIterator<Command> commandListIterator = listOfCommand.listIterator();

        List<Issue> localMerge = new ArrayList<>();
        while (commandListIterator.hasNext()) {
            Command current = commandListIterator.next();

            if (!(current instanceof RUNCommand)) {
                issuesFound.add(localMerge);
                localMerge = new ArrayList<>();
                continue;
            }

            RUNCommand currentRunCommand = (RUNCommand) current;

            if (commandListIterator.hasNext()) {
                Command next = commandListIterator.next();
                if (next instanceof RUNCommand) {
                    RUNCommand nextRunCommand = (RUNCommand) next;

                    localMerge.add(new Issue(dockerfile, nextRunCommand));

                    // merge next into current
                    List<ShellCommand> body = nextRunCommand.getBody();
                    currentRunCommand.getBody().addAll(body);

                    // delete next
                    commandListIterator.remove();
                    current = commandListIterator.previous();
                }
            }

            /*
            //  List that contains the local mergeable RUN commands
            List<Issue> newIssue = new ArrayList<>();
            while (current instanceof RUNCommand) {
                //  Add new mergeable command to issue
                Issue currentIssue = new Issue(dockerfile, (RUNCommand) current);
                newIssue.add(currentIssue);
                if (commandListIterator.hasNext()) {
                    current = commandListIterator.next();
                } else break;
            }

            if (newIssue.size() > 1) {
                issuesFound.add(newIssue);
                current = commandListIterator.previous();
            }
            */
        }

        if (!localMerge.isEmpty()) {
            issuesFound.add(localMerge);
        }

        /*
        for (int i = 0; i < listOfCommand.size() - 1; i++) {

            int j = i;
            Command currentCommand = listOfCommand.get(j);
            while (currentCommand instanceof RUNCommand && j <= listOfCommand.size() - 1) {
                currentCommand = listOfCommand.get(j);
                j++;
            }

            if (j - i > 1) {
                List<ShellCommand> newBody = new ArrayList<>();
                List<Issue> currentIssue = new ArrayList<>();
                for (int current = i; current < j; current++) {
                    RUNCommand runCommand = (RUNCommand) listOfCommand.get(current);
                    newBody.addAll(runCommand.getBody());
                    currentIssue.add(new Issue(dockerfile, runCommand));
                    i++;
                }
                issues.add(currentIssue);


                //for (int current = i; current < j; current++) {
                //    listOfCommand.remove(i);
                //}

                //listOfCommand.add(i, new RUNCommand(newBody));

            }


        }
*/
        return issuesFound;
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
