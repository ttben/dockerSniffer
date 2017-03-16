package fr.unice.i3s.sparks.docker.core.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CMDCommand extends Command{

    private final List<String> body;

    public CMDCommand(String[] body) {
        this.body = new ArrayList<>(Arrays.asList(body));
    }

    public List<String> getBody() {
        return body;
    }
}
