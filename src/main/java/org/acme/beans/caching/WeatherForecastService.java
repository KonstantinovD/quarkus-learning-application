package org.acme.beans.caching;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;

// Each of the Caffeine caches backing up the Quarkus application data
// caching extension can be configured using the following properties in
// the application.properties file
@ApplicationScoped
public class WeatherForecastService {

    @CacheResult(cacheName = "weather-cache")
    // Loads a method result from the cache
    // without executing the method body whenever possible. If the
    // method has one or more arguments, the key computation is done
    // from all arguments if none of them is annotated with @CacheKey
    //
    // Each non-primitive method argument that is part of the key
    // must implement equals() and hashCode()
    //
    // A method annotated with CacheResult is protected by a lock on
    // cache miss mechanism. If several concurrent invocations try to
    // retrieve a cache value from the same missing key, the method will
    // only be invoked once. The first concurrent invocation will trigger
    // the method invocation while the subsequent concurrent invocations
    // will wait for the end of the method invocation to get the cached
    // result. The lockTimeout parameter can be used to interrupt the
    // lock after a given delay. The lock timeout is disabled by default,
    // meaning the lock is never interrupted.
    //
    // This annotation cannot be used on a method returning void
    public String gtDailyForecast(LocalDate date, String city) {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return date.getDayOfWeek() + " will be " +
          getDailyResult(date.getDayOfMonth() % 4) + " in " + city;
    }

    private String getDailyResult(int day) {
        switch (day){
            case 0:
                return "sunny";
            case 1:
                return "cloudy";
            case 2:
                return "chilly";
            case 3:
                return "rainy";
            default:
                throw new IllegalArgumentException();
        }
    }

    @CacheInvalidate(cacheName = "weather-cache")
    // Removes an entry from the cache.
    public void invalidate1(String city, LocalDate date) {
        // Calling this method WILL NOT invalidate values cached
        // because the key elements order is different.
    }

    @CacheInvalidate(cacheName = "weather-cache")
    public void invalidate2(@CacheKey String city, String date) {
        // Calling this method WILL NOT invalidate values cached
    }

    // @CacheKey annotation is optional and should only be used when some
    // of the method arguments are NOT part of the cache key.
    @CacheInvalidate(cacheName = "weather-cache")
    public void invalidate3(Object notPartOfTheKey,
      @CacheKey  LocalDate date, @CacheKey String city) {
        // Calling this method WILL invalidate values
    }

    @CacheInvalidate(cacheName = "weather-cache")
    public void invalidate4(LocalDate date,String city) {
        // Calling this method WILL invalidate values
    }

    @CacheInvalidateAll(cacheName = "weather-cache")
    // Quarkus will remove all entries from the cache.
    public void invalidate5() {
        // Calling this method WILL invalidate ALL values
    }

}
