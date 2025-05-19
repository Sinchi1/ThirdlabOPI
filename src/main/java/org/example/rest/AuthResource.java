package org.example.rest;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.ejb.UserService;
import org.example.entity.User;
import org.example.security.JwtUtil;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    @EJB
    private UserService userService;

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        try {
            User user = userService.authenticate(request.username, request.password);
            String token = JwtUtil.generateToken(user.getId(), user.getUsername());
            return Response.ok(new AuthResponse(token)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse("Invalid credentials"))
                    .build();
        }
    }

    @POST
    @Path("/register")
    public Response register(RegisterRequest request) {
        try {
            User user = userService.register(request.username, request.password);
            String token = JwtUtil.generateToken(user.getId(), user.getUsername());
            return Response.ok(new AuthResponse(token)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    public static class LoginRequest {
        public String username;
        public String password;
    }

    public static class RegisterRequest {
        public String username;
        public String password;
    }

    public static class AuthResponse {
        public String token;

        public AuthResponse(String token) {
            this.token = token;
        }
    }

    public static class ErrorResponse {
        public String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}