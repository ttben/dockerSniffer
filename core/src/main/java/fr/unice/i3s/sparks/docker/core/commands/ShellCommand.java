package fr.unice.i3s.sparks.docker.core.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShellCommand extends Command {
    private List<String> body;

    public ShellCommand(List<String> body) {
        this.body = body;
    }

    public ShellCommand(String... body) {
        this.body = new ArrayList<>(Arrays.asList(body));
    }

    public List<String> getBody() {
        return body;
    }

    public void setBody(List<String> body) {
        this.body = body;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ShellCommand{");
        sb.append("body=").append(body);
        sb.append('}');
        return sb.toString();
    }
}
