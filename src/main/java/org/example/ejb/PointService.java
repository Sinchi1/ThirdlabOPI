package org.example.ejb;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.entity.Point;
import org.example.entity.User;
import org.example.util.AreaChecker;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class PointService {
    @PersistenceContext(unitName = "web4PU")
    private EntityManager em;

    @Inject
    private UserService userService;

    @EJB
    private TrackPoints mBeanPointsprovider;

    @EJB
    private ProcentagePoints procentagePointsprovider;


    public Point addPoint(String x, String y, String r, Long userId) {

        long startTime = System.nanoTime();

        User user = em.find(User.class, userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        double y1 = Double.parseDouble(y);
        double x1 = Double.parseDouble(x);
        Point point = new Point(x1, y1, Double.parseDouble(r));
        point.setUser(user);
        point.setHit(AreaChecker.checkHit(x1, y1, Double.parseDouble(r)));
        point.setCreatedAt(LocalDateTime.now());
        point.setExecutionTime((System.nanoTime() - startTime) / 1000000);
        mBeanPointsprovider.incrementCounter(point.isHit());
        procentagePointsprovider.incrementShots(point.isHit());
        procentagePointsprovider.calculatePercentage();
        System.out.println(mBeanPointsprovider.getTotalPoints());

        em.persist(point);
        return point;
    }

    public List<Point> getUserPoints(Long userId, int offset, int limit) {
        return em.createQuery(
                        "SELECT p FROM Point p WHERE p.user.id = :userId ORDER BY p.createdAt DESC", Point.class)
                .setParameter("userId", userId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }


    public void clearUserPoints(Long userId) {
        em.createQuery("DELETE FROM Point p WHERE p.user.id = :userId")
            .setParameter("userId", userId)
            .executeUpdate();
    }
}

// history