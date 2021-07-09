package org.acme.beans.caching;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;

import javax.enterprise.context.Dependent;
import java.util.UUID;

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
}
