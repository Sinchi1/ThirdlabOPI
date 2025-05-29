package org.example.Interfaces;

import javax.management.MXBean;

@MXBean
public interface TrackPointsMBean {
    int getTotalPoints();
    int getTotalHits();
    void sendNotification();
    void incrementCounter(boolean isHit);
    void clearCounter();
//    void countInit(HttpHeaders httpHeaders);
}
