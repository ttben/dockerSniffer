package fr.unice.i3s.sparks.docker.core.tree;

import fr.unice.i3s.sparks.docker.core.model.Image;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DepthFirstSearch {
    private Set<List<Image>> paths = new HashSet<>();

    public Set<List<Image>> linearize(Image currentRoot) {
        traverse(currentRoot);
        return paths;
    }

    private void traverse(Image root) {
        // assume root != NULL
        LinkedList<Image> path = new LinkedList<>();
        traverse(root, path);
        paths.add(path);
    }

    private void traverse(Image currentNode, LinkedList<Image> path) {
        //System.out.println("Traversing node:"+ currentNode.getImageID());
        path.add(currentNode);
        //  Leaf
        if (currentNode.getChildren().isEmpty()) {
            //System.out.println("Leaf reached!");
            paths.add(path);
            return;
        }
        for (Image child : currentNode.getChildren()) {
            traverse(child, new LinkedList<Image>(path));
        }
        paths.add(path);
    }
}
