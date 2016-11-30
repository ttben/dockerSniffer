package fr.unice.i3s.sparks.docker.core.commands;

public class NonParsedCommand extends Command {
    private String s;

    public NonParsedCommand(String s) {

        if(s.toLowerCase().startsWith("run")) {
            throw new RuntimeException("PARSEDCOMMAND THAT CONTAINS RUN:" + s);
        }

        this.s = s;
    }
}
