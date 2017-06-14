package fr.unice.i3s.sparks.docker.core.conflicts;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class OptimMultipleRun {
    public List<List<Issue>> apply(Dockerfile dockerfile) {
        List<List<OptimMultipleRun.Issue>> issues = new ArrayList<>();

        List<Command> listOfCommand = dockerfile.getListOfCommand();

        ListIterator<Command> commandListIterator = listOfCommand.listIterator();
        while (commandListIterator.hasNext()) {
            Command current = commandListIterator.next();

            List<Issue> newIssue = new ArrayList<>();
            while(current instanceof RUNCommand) {
                Issue currentIssue = new Issue(dockerfile, (RUNCommand) current);
                newIssue.add(currentIssue);
                if(commandListIterator.hasNext())
                  current = commandListIterator.next();
                else break;
            }

            if (newIssue.size() > 1) {
                issues.add(newIssue);
                current = commandListIterator.previous();
            }
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
