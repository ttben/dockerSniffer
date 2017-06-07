package fr.unice.i3s.sparks.docker.core.conflicts.env;

import fr.unice.i3s.sparks.docker.core.model.dockerfile.commands.ENVCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ENVConflictMap {
    private final Map<String, List<ENVCommand>> conflictMap;

    public ENVConflictMap() {
        this.conflictMap = new HashMap<>();
    }

    public ENVConflictMap(Map<String, List<ENVCommand>> conflictMap) {
        this.conflictMap = conflictMap;
    }

    public Map<String, List<ENVCommand>> getConflictMap() {
        return conflictMap;
    }

    @Override
    public String toString() {
        return "ENVConflictMap{" +
                "conflictMap=" + conflictMap +
                '}';
    }
}
