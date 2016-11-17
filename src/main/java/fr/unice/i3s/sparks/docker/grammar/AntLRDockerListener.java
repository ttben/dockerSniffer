package fr.unice.i3s.sparks.docker.grammar;

import com.github.dockerjava.core.dockerfile.Dockerfile;
import fr.unice.i3s.sparks.docker.core.DockerFile;
import fr.unice.i3s.sparks.docker.DockerfileBaseListener;
import fr.unice.i3s.sparks.docker.DockerfileParser;
import fr.unice.i3s.sparks.docker.conflicts.commands.Command;
import fr.unice.i3s.sparks.docker.conflicts.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.conflicts.commands.ShellCommand;
import org.antlr.v4.runtime.tree.ErrorNode;

import java.util.ArrayList;
import java.util.List;

public class AntLRDockerListener extends DockerfileBaseListener {

    List<Dockerfile> listDockerFile = new ArrayList<>();

    List<Command> listOfCommand = new ArrayList<>();
    private DockerFile dockerfile;

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
        this.dockerfile = new DockerFile(listOfCommand);
        //System.out.println(listOfCommand);
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        System.err.println("loooo");
        super.visitErrorNode(node);
    }

    public DockerFile getDockerfile() {
        return dockerfile;
    }
}
