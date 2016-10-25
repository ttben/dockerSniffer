package fr.unice.i3s.sparks.docker.conflicts;

import java.util.Map;
import java.util.Set;

public class ImageTree {
    private final Map<ImageID, Image> imageIDImageMap;
    private final Set<Image> roots;

    public ImageTree(Map<ImageID, Image> imageIDImageMap, Set<Image> roots) {
        this.imageIDImageMap = imageIDImageMap;
        this.roots = roots;
    }

    public Map<ImageID, Image> getImageIDImageMap() {
        return imageIDImageMap;
    }

    public Set<Image> getRoots() {
        return roots;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ImageTree{");
        sb.append("imageIDImageMap=").append(imageIDImageMap);
        sb.append(", roots=").append(roots);
        sb.append('}');
        return sb.toString();
    }
}
