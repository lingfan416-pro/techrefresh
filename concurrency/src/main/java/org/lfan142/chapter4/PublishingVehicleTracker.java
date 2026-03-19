package org.lfan142.chapter4;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PublishingVehicleTracker {

    private final Map<String, SafePoint> locations;
    private final Map<String, SafePoint> unmodifiedMap;

    public PublishingVehicleTracker(Map<String, SafePoint> locations) {
        this.locations = new ConcurrentHashMap<>(locations);
        this.unmodifiedMap = Collections.unmodifiableMap(this.locations);
    }

    public Map<String, SafePoint> getLocations(){
        return unmodifiedMap;
    }

    public SafePoint getLocation(String key){
        return locations.get(key);
    }

    public void setLocation(String id, int x, int y){
        if(!locations.containsKey(id)){
            throw new IllegalArgumentException("Invalid vehicle Name:" + id);
        }
        locations.get(id).set(x, y);
    }


    public static void main(String[] args) {
        Map<String, SafePoint> locations = new HashMap<>();
        locations.put("1", new SafePoint(1,3));
        PublishingVehicleTracker vehicleTracer = new PublishingVehicleTracker(locations);
        Map<String, SafePoint> allLocations = vehicleTracer.getLocations();
        allLocations.remove(1);
        System.out.println( allLocations.get("1"));
    }
}
