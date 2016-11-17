package fr.unice.i3s.sparks.docker.grammar;

import fr.unice.i3s.sparks.docker.DockerfileBaseListener;
import fr.unice.i3s.sparks.docker.DockerfileParser;
import fr.unice.i3s.sparks.docker.conflicts.commands.Command;
import fr.unice.i3s.sparks.docker.conflicts.commands.RUNCommand;
import fr.unice.i3s.sparks.docker.conflicts.commands.ShellCommand;
import org.antlr.v4.runtime.tree.ErrorNode;

import java.util.ArrayList;
import java.util.List;

public class AntLRDockerListener extends DockerfileBaseListener {

    List<Command> list = new ArrayList<>();

    public List<Command> getList() {
        return list;
    }

    public void setList(List<Command> list) {
        this.list = list;
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

        list.add(runCommand);
    }

    @Override
    public void exitDockerfile(DockerfileParser.DockerfileContext ctx) {
        //System.out.println(list);
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        System.err.println("loooo");
        super.visitErrorNode(node);
    }
}
