package fr.unice.i3s.sparks.docker.conflicts;

import fr.unice.i3s.sparks.docker.conflicts.commands.Command;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Image {
    private final ImageID imageID;
    private Image parent;
    private Set<Image> children = new HashSet<>();
    private List<Command> commandList = new ArrayList<Command>();

    public Image(ImageID imageID, List<Command> commandList) {
        this.commandList = commandList;
        this.imageID = imageID;
    }

    public Image(ImageID imageID, Image parent, List<Command> commandList) {
        this.parent = parent;
        this.commandList = commandList;
        this.imageID = imageID;
    }

    public Image(ImageID imageID, Image parent, Set<Image> children, List<Command> commandList) {
        this.parent = parent;
        this.children = children;
        this.commandList = commandList;
        this.imageID = imageID;
    }

    public Image getParent() {
        return parent;
    }

    public void setParent(Image parent) {
        this.parent = parent;
    }

    public void addChild(Image child) {
        this.children.add(child);
    }

    public List<Command> getCommandList() {
        return commandList;
    }

    public ImageID getImageID() {
        return imageID;
    }

    public Set<Image> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Image{");
        sb.append("imageID=").append(imageID);
        if(parent != null) { sb.append(", parent=").append(parent.getImageID()); }
        sb.append(", commandList=").append(commandList);
        sb.append('}');
        return sb.toString();
    }
}
