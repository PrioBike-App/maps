package com.mapbox.mapboxgl;

import android.location.Location;
import android.util.Log;
import android.app.PendingIntent;
import android.os.Looper;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineRequest;
import java.lang.UnsupportedOperationException;
import java.util.List;
import java.util.ArrayList;

class CustomLocationManager implements LocationEngine {
    private LocationEngine fallbackLocationEngine;
    private Location customLocation;
    private List<LocationEngineCallback<LocationEngineResult>> callbacks = new ArrayList<LocationEngineCallback<LocationEngineResult>>();
    private boolean useFallbackLocationEngine = true;

    CustomLocationManager(LocationEngine fallbackLocationEngine){
        this.fallbackLocationEngine = fallbackLocationEngine;
    }

    public void getLastLocation(LocationEngineCallback<LocationEngineResult> callback){
        if (customLocation != null && System.currentTimeMillis() - customLocation.getTime() < 4000) {
            callback.onSuccess(LocationEngineResult.create(customLocation));
        } else {
            fallbackLocationEngine.getLastLocation(callback);
        }
    }

    public void overrideLastLocation(Location location){
        if (this.useFallbackLocationEngine){
            for (int i = 0; i < this.callbacks.size(); i++) {
                this.fallbackLocationEngine.removeLocationUpdates(this.callbacks.get(i));
            }
            this.useFallbackLocationEngine = !this.useFallbackLocationEngine;
        }

        this.customLocation = location;
        for (int i = 0; i < this.callbacks.size(); i++) {
            this.callbacks.get(i).onSuccess(LocationEngineResult.create(customLocation));
        }
    }

    public void removeLocationUpdates(LocationEngineCallback<LocationEngineResult> callback){
        if (this.useFallbackLocationEngine){
            for (int i = 0; i < this.callbacks.size(); i++) {
                this.fallbackLocationEngine.removeLocationUpdates(this.callbacks.get(i));
            }
        }
        this.callbacks.remove(callback);
    }

    public void removeLocationUpdates(PendingIntent pendingIntent){
        throw new java.lang.UnsupportedOperationException("Not supported.");
    }

    public void requestLocationUpdates(LocationEngineRequest request, LocationEngineCallback<LocationEngineResult> callback, Looper looper){
        this.callbacks.add(callback);
        this.fallbackLocationEngine.requestLocationUpdates(request, callback, looper);
    }

    public void requestLocationUpdates(LocationEngineRequest request, PendingIntent pendingIntent){
        throw new java.lang.UnsupportedOperationException("Not supported.");
    }
}
