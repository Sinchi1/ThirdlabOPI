package org.example.Interfaces;

import javax.management.MXBean;

@MXBean
public interface ProcentagePointsMBean {
    double getPercentage();
    void calculatePercentage();
    void incrementShots(boolean isHit);
}
