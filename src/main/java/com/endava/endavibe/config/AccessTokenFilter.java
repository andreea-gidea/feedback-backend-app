package com.endava.endavibe.config;

import com.endava.endavibe.appUser.AppUserEntity;
import com.endava.endavibe.appUser.AppUserService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@RequiredArgsConstructor
public class AccessTokenFilter extends OncePerRequestFilter {

    private static final String MICROSOFT_GRAPH_URL = "https://graph.microsoft.com/v1.0/me";
    private final AppUserService appUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String tokenValue = authHeader.substring(7);
            if (tokenValue == null || tokenValue.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Token in Bearer Header");
            } else {
                try {
                    URL url = new URL(MICROSOFT_GRAPH_URL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Authorization", authHeader);
                    conn.setRequestProperty("Accept", "application/json");

                    if (conn.getResponseCode() == HttpStatus.OK.value()) {
                        CustomAuthentication customAuthentication = new CustomAuthentication(getUser(conn.getInputStream()), appUserService);
                        if (SecurityContextHolder.getContext().getAuthentication() == null) {
                            SecurityContextHolder.getContext().setAuthentication(customAuthentication);
                        }
                    }
                } catch (Exception exc) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Token" + exc);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private AppUserEntity getUser(InputStream inputStream) throws IOException {
        JSONObject jsonObject = readResponse(inputStream);

        AppUserEntity appUserEntity = new AppUserEntity();

        appUserEntity.setUuid(UUID.fromString((String) jsonObject.get("id")));
        appUserEntity.setFirstName((String) jsonObject.get("givenName"));
        appUserEntity.setLastName((String) jsonObject.get("surname"));
        appUserEntity.setEmail((String) jsonObject.get("mail"));

        return appUserEntity;
    }

    private JSONObject readResponse(InputStream input) throws IOException {
        BufferedReader in = null;
        StringBuilder graphResponse = new StringBuilder();
        try {
            in = new BufferedReader(
                    new InputStreamReader(input));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                graphResponse.append(inputLine);
            }
        } finally {
            assert in != null;
            in.close();
        }
        return new JSONObject(graphResponse.toString());
    }
}
