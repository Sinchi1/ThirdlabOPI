package org.example.rest;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.ejb.PointService;
import org.example.entity.Point;
import org.example.security.JwtUtil;

import java.util.List;

@Path("/points")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PointResource {
    @EJB
    private PointService pointService;

    @POST
    public Response addPoint(
            @Context HttpHeaders headers,
            PointRequest request
    ) {
        try {
            String token = extractToken(headers);
            Long userId = JwtUtil.getUserIdFromToken(token);

            Point point = pointService.addPoint(
                request.x,
                request.y,
                String.valueOf(request.r),
                userId
            );

            return Response.ok(point).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @GET
    public Response getPoints(@Context HttpHeaders headers) {
        try {
            String token = extractToken(headers);
            Long userId = JwtUtil.getUserIdFromToken(token);

            List<Point> points = pointService.getUserPoints(userId);
            return Response.ok(points).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @DELETE
    public Response clearPoints(@Context HttpHeaders headers) {
        try {
            String token = extractToken(headers);
            Long userId = JwtUtil.getUserIdFromToken(token);

            pointService.clearUserPoints(userId);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    private String extractToken(HttpHeaders headers) {
        String authHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("No valid authorization header found");
        }
        return authHeader.substring(7);
    }

    public static class PointRequest {
        public String x;
        public String y;
        public double r;
    }

    public static class ErrorResponse {
        public String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}
