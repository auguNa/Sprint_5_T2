package S5_T2.IT_ACADEMY.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Cache;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CachingConfig {
    @Bean
    public Cache<String, String> jwtTokenCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.MINUTES) // Adjust expiration as needed
                .build();
    }
}
