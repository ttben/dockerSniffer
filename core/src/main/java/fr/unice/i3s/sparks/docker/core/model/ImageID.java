package fr.unice.i3s.sparks.docker.core.model;

public class ImageID {
    private final String digest;

    public ImageID(String digest) {
        this.digest = digest;
    }

    public String getDigest() {
        return digest;
    }

    @Override
    public String toString() {
        return digest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageID imageID = (ImageID) o;

        return digest != null ? digest.equals(imageID.digest) : imageID.digest == null;

    }

    @Override
    public int hashCode() {
        return digest != null ? digest.hashCode() : 0;
    }
}
