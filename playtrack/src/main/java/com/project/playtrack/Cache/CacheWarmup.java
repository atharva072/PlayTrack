package com.project.playtrack.Cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.project.playtrack.User.UserRepository;

@Component
public class CacheWarmup {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CacheManager cacheManager;

    @EventListener(ApplicationReadyEvent.class)
    public void warmUpUserNameCache () {
        Cache cache = cacheManager.getCache("usernameExists");
        if (cache == null) {
            System.out.print("Cache key: 'usernameExiists' not found. Skipping username cache warmup");
            return;
        }

        List<String> usernameList = userRepository.findAllUsernames();
        for (String username : usernameList) {
            cache.put(username, true);
        }
        System.out.println("Cache warmed: " + usernameList.size() + " usernames loaded.");
    }
}
