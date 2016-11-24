package fr.unice.i3s.sparks.docker.core.commands;

public class ENVCommand extends Command {
    private String key;
    private String value;

    public ENVCommand(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ENVCommand{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
