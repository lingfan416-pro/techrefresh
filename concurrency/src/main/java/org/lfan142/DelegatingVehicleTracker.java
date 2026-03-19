package org.lfan142;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DelegatingVehicleTracker {

    private final ConcurrentMap<String, ImmutablePoint> locations;
    private final Map<String, ImmutablePoint> unmodifiedMap;

    public DelegatingVehicleTracker(Map<String, ImmutablePoint> points){
        this.locations = new ConcurrentHashMap<>(points);
        this.unmodifiedMap = Collections.unmodifiableMap(locations);
    }


    public Map<String, ImmutablePoint> getLocations(){
        return unmodifiedMap;
    }

    public ImmutablePoint getLocation(String id){
        return locations.get(id);
    }

    public void setLocation(String id, int x, int y){
        if(locations.put(id, new ImmutablePoint(x, y)) == null){
            throw new IllegalArgumentException("Invalid vehicle Name : " + id);
        }
    }


}


