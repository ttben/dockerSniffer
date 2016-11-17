package fr.unice.i3s.sparks.docker.tree;

import fr.unice.i3s.sparks.docker.core.Image;
import fr.unice.i3s.sparks.docker.core.ImageID;
import fr.unice.i3s.sparks.docker.conflicts.MalFormedImageException;
import fr.unice.i3s.sparks.docker.conflicts.commands.Command;
import fr.unice.i3s.sparks.docker.conflicts.commands.FROMCommand;

import java.util.*;
import java.util.stream.Collectors;

public class ImageTreeBuilder {
    public static ImageTree build(final Set<Image> images) throws MalFormedImageException {
        Map<ImageID, Image> imageIDImageMap = new HashMap<>();
        for (Image image : images) {
            ImageID currentImageID = image.getImageID();
            imageIDImageMap.put(currentImageID, image);

            ImageID parent = null;
            try {
                parent = getParent(image);
            } catch (IllegalStateException e) {
                //e.printStackTrace();
            }

            if (imageIDImageMap.containsKey(parent)) {
                Image parentImage = imageIDImageMap.get(parent);
                parentImage.addChild(image);
                image.setParent(parentImage);
            }
        }

        Set<Image> roots = new HashSet<>();
        for (Image image : imageIDImageMap.values()) {
            try {
                ImageID parent = getParent(image);
                Image parentImage = imageIDImageMap.get(parent);
                if (!parentImage.getChildren().contains(image)) {
                    parentImage.addChild(image);
                }
                image.setParent(parentImage);
            } catch (IllegalStateException e) {
                //e.printStackTrace();
                roots.add(image);
            }
        }

        return new ImageTree(imageIDImageMap, roots);
    }

    private static ImageID getParent(Image image) throws MalFormedImageException {
        List<Command> commandList = image.getCommandList();
        Set<Command> fromCommandSet = commandList.stream().filter(c -> c instanceof FROMCommand).collect(Collectors.toSet());
        int size = fromCommandSet.size();
        if (size > 1) {
            throw new MalFormedImageException("An image can not have more that one FROM command.");
        }

        FROMCommand[] fromCommandsArray = new FROMCommand[1];
        fromCommandsArray = fromCommandSet.toArray(fromCommandsArray);
        FROMCommand fromCommand = fromCommandsArray[0];

        if (fromCommandsArray.length == 0 || fromCommand == null) {
            //System.out.printf("Image:%s has no parent\n", image.getImageID());
            throw new IllegalStateException("Can not get parent: does not have FROM close.");
        }

        return fromCommand.getParent();
    }


}
