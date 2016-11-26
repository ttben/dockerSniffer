package fr.unice.i3s.sparks.docker.grammar;

import fr.unice.i3s.sparks.docker.DockerfileBaseListener;
import fr.unice.i3s.sparks.docker.DockerfileParser;
import fr.unice.i3s.sparks.docker.core.commands.Command;
import fr.unice.i3s.sparks.docker.core.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.core.commands.ShellCommand;
import fr.unice.i3s.sparks.docker.core.model.Dockerfile;
import org.antlr.v4.runtime.tree.ErrorNode;

import java.util.ArrayList;
import java.util.List;

public class AntLRDockerListener extends DockerfileBaseListener {

    List<Dockerfile> listDockerFile = new ArrayList<>();

    List<Command> listOfCommand = new ArrayList<>();
    private Dockerfile dockerfile;

    public List<Command> getListOfCommand() {
        return listOfCommand;
    }

    public void setListOfCommand(List<Command> listOfCommand) {
        this.listOfCommand = listOfCommand;
    }

    @Override
    public void exitRun(DockerfileParser.RunContext ctx) {

        RUNCommand runCommand = new RUNCommand();

        ctx.body().shellCmd().forEach(shellCmdContext -> {
            List<String> shellBody = new ArrayList<>();

            shellCmdContext.ANYKEYS().forEach(terminalNode ->
                    shellBody.add(terminalNode.toString())
            );

            ShellCommand shellCommand = new ShellCommand(shellBody);
            runCommand.add(shellCommand);
        });

        listOfCommand.add(runCommand);
    }

    @Override
    public void exitDockerfile(DockerfileParser.DockerfileContext ctx) {
        this.dockerfile = new Dockerfile(listOfCommand);
        //System.out.println(listOfCommand);
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        System.out.println("loooo");
        System.out.println(node);
        //super.visitErrorNode(node);
    }

    public Dockerfile getDockerfile() {
        return dockerfile;
    }
}
