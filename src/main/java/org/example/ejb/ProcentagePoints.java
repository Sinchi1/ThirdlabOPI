package org.example.ejb;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import org.example.Interfaces.ProcentagePointsMBean;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

@LocalBean
@Startup
@Singleton
public class ProcentagePoints implements ProcentagePointsMBean {

    @PostConstruct
    public void init() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("org.example:type=MBeanPercentage");
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

    private double percentage;
    private double shots = 0;
    private double hits = 0;

    public  void sendNotification() {
        StringBuilder sb = new StringBuilder();
        sb.append("Amount of all shots ").append(shots)
                .append(" Percentage of hits ").append(percentage);
        System.out.println(sb.toString());
    }

    @Override
    public  double getPercentage() {
        calculatePercentage();
        return percentage;
    }

    @Override
    public  void calculatePercentage() {
        percentage = hits /(shots);
        sendNotification();
    }

    public  void incrementShots(boolean isHit) {
        shots++;
        if (isHit) {
            hits++;
        }
    }

}


