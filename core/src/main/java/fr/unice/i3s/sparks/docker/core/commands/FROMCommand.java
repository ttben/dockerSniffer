package fr.unice.i3s.sparks.docker.core.commands;

import fr.unice.i3s.sparks.docker.core.model.ImageID;

public class FROMCommand extends Command {
    private ImageID parent;

    public FROMCommand(ImageID parent) {
        this.parent = parent;
    }

    public ImageID getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return "FROMCommand{" +
                "parent=" + parent +
                '}';
    }
}
