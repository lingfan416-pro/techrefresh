package org.lfan142.chapter4;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MonitorVehicleTracker {

    private final Map<String, MutablePoint> locations;

    public MonitorVehicleTracker(Map<String, MutablePoint> locations){
        this.locations = deepCopy(locations);
    }

    public synchronized Map<String, MutablePoint> getLocations(){
        return deepCopy(locations);
    }

    public synchronized MutablePoint getLocation(String id){
        MutablePoint loc = locations.get(id);
        return loc == null ? null : new MutablePoint(loc);
    }

    public synchronized void setLocation(String id, int x, int y){
        MutablePoint loc = locations.get(id);
        if(loc == null){
            throw new IllegalArgumentException("No such ID: " + id);
        }
        loc.setX(x);
        loc.setY(y);
    }

    private Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> locations) {
        Map<String, MutablePoint> result = new HashMap<>();
        for(Map.Entry<String, MutablePoint> location : locations.entrySet()){
            result.put(location.getKey(), new MutablePoint(location.getValue().getX(), location.getValue().getY()));
        }

        return Collections.unmodifiableMap(result);
    }

}
