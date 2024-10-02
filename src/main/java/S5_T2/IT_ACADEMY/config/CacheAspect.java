package S5_T2.IT_ACADEMY.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.cache.annotation.Cacheable;

@Aspect
@Component
public class CacheAspect {
    @Autowired
    private CacheManager cacheManager;

    @Around("@annotation(cacheable)")
    public Object logCacheHit(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        String cacheName = cacheable.value()[0];
        Object key = joinPoint.getArgs()[0]; // assuming the first argument is the key
        boolean isCached = isInCache(cacheName, key);

        if (isCached) {
            // Log cache hit
            System.out.println("Cache hit for cache: " + cacheName + " with key: " + key);
            return cacheManager.getCache(cacheName).get(key).get();
        } else {

            System.out.println("Cache miss for cache: " + cacheName + " with key: " + key);
            return joinPoint.proceed();
        }
    }

    private boolean isInCache(String cacheName, Object key) {
        Cache cache = cacheManager.getCache(cacheName);
        return cache != null && cache.get(key) != null;
    }
}
