package org.example.ejb;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import org.example.Interfaces.TrackPointsMBean;


import javax.management.*;
import java.lang.management.ManagementFactory;
@LocalBean
@Startup
@Singleton
public class TrackPoints implements TrackPointsMBean {

    private int totalPoints = 0;
    private int totalHits = 0;

    @PostConstruct
    public void init() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("org.example:type=MBeanPoints");
            if (!mbs.isRegistered(name)) {
                mbs.registerMBean(this, name);
                System.out.println("MBean зарегистрирован: " + name);
            } else {
                System.out.println("MBean уже зарегистрирован: " + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void sendNotification() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sum of points ").append(totalPoints)
                .append(" , Sum of hits: ").append(totalHits);
        System.out.println(sb.toString());
    }

    @Override
    public void incrementCounter(boolean isHit) {
        totalPoints++;
        if (isHit) totalHits++;
        if (totalPoints % 10 == 0) sendNotification();
    }

    @Override
    public void clearCounter() {
        totalPoints = 0;
        totalHits = 0;
    }

    @Override
    public int getTotalPoints() {
        return totalPoints;
    }

    @Override
    public int getTotalHits() {
        return totalHits;
    }
}
