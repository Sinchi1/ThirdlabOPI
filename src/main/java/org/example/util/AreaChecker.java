package org.example.util;

public class AreaChecker {
    public static boolean checkHit(double x, double y, double r) {
        if (r > 0) {
            // Прямоугольник (первая четверть)
            if (x >= 0 && y >= 0 && x <= r && y <= r) {
                return true;
            }
            // Треугольник (вторая четверть)
            if (x <= 0 && y >= 0 && y < x + r) {
                return true;
            }
            // Круг (четвёртая четверть)
            if (x >= 0 && y <= 0 && (x * x + y * y) <= (r * r) / 4) {
                return true;
            }
            return false;
        } if (r < 0) {
            double R = Math.abs(r);
            // Зеркальный прямоугольник
            if (x >= 0 && y >= 0 && x <= R && y <= R) {
                return true;
            }
            // Зеркальный треугольник
            if (x <= 0 && y >= 0 && y <= x + R) {
                return true;
            }
            // и так понятно
            if (x >= 0 && y <= 0 && (x * x + y * y) <= (R * R) / 4) {
                return true;
            }
            return false;
        }
        return false;
    }
}