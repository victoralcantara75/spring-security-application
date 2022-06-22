package com.secutiry.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.secutiry.entities.Role;
import com.secutiry.entities.User;
import com.secutiry.repositories.UserRepository;

@RestController
@RequestMapping(value = "api/token")
public class TokenController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {

                String refreshToken = authorizationHeader.substring("Bearer ".length());
                DecodedJWT decodedJWT = SecurityUtil.decodeJwt(refreshToken);
                String username = decodedJWT.getSubject();

                Optional<User> optionalUser = userRepository.findByUsername(username);
                if (optionalUser.isEmpty())
                    throw new Exception("Username not found!");

                User user = optionalUser.get();
                List<String> authorities = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
                String accessToken = SecurityUtil.createAccessJwt(request, user.getUsername(), authorities);
                
                SecurityUtil.writeResponseBody(response, SecurityUtil.getJsonTokens(accessToken, refreshToken));
                
            } catch (Exception e) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid token!");
                SecurityUtil.writeResponseBody(response, error);
            }

        }
    }
    
}
