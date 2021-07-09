package org.acme.beans.caching;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.Dependent;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Dependent
public class CachedService {

    // A unique default cache key derived from the cache name is used
    // because the method has no arguments.
    @CacheResult(cacheName = "foo")
    public String load() {
        return UUID.randomUUID().toString();
        // Call expensive service here.
    }

    @CacheInvalidate(cacheName = "foo")
    public void invalidate() {
    }

    @CacheInvalidateAll(cacheName = "foo")
    public void invalidateAllCached() {
    }

    // This method will invalidate all entries
    // from the 'foo' and 'weather-cache' caches with a single call.
    @CacheInvalidateAll(cacheName = "foo")
    @CacheInvalidateAll(cacheName = "weather-cache")
    public void multipleInvalidateAll() {
    }


    // Sometimes one wants to cache the result of an (expensive) remote
    // call. If the remote call fails, one may not want to cache the
    // result or exception, but rather re-try the remote call.
    //
    //A simple approach could be to catch the exception and return null,
    // so that the caller can act accordingly
    //
    // This approach has an unfortunate side effect: as we said before,
    // ---  Quarkus CAN CACHE NULL VALUES, whereas Caffeine cannot  ---
    // Which means that the next call to callRemote() with the same
    // parameter value will be answered out of the cache, returning
    // null and no remote call will be done. This may be desired in
    // some scenarios, but usually one wants to retry the remote call
    // until it returns a result.
    public String caller(int val) {
        Integer result = callRemote(val);
        if (result != null) {
            log.info("Result is " + result);
            return "Result is " + result;
        } else {
            log.error("Got an exception!");
            return "Got an exception!";
        }
    }

    @CacheResult(cacheName = "bar")
    Integer callRemote(int val)  {
        try {
            Random random = new Random();
            boolean b = random.nextBoolean();
            if (!b) {
                throw new IllegalArgumentException(
                  "value cannot be false");
            }
            return val;
        } catch (Exception e) {
            return null;
        }
    }

    // To prevent the cache from caching (marker) results from a remote
    // call, we need to let the exception bubble out of the called method
    // and catch it at the caller side:
    public String caller2(int val) {
        try {
        Integer result = callRemote2(val);
            log.info("Result is " + result);
            return "Result is " + result;
        } catch (Exception ec){
            log.error("Got an exception!");
            return "Got an exception!";
        }
    }

    @CacheResult(cacheName = "bar")
    Integer callRemote2(int val) throws Exception  {
        Random random = new Random();
        boolean b = random.nextBoolean();
        if (!b) {
            throw new IllegalArgumentException(
              "value cannot be false");
        }
        return val;
    }


}
