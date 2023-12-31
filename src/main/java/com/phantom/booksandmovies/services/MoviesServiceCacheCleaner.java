package com.phantom.booksandmovies.services;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Previous version of cache needed to be cleaned manually
 */

@RequiredArgsConstructor
@Component
public class MoviesServiceCacheCleaner {

    private final MoviesService moviesService;

    //@Scheduled(fixedRate = 6000)
    public void evictAllcachesAtIntervals() {moviesService.evictAllCacheValues();}
}
