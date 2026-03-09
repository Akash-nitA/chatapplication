package com.example.chatApp.Utility;

import com.example.chatApp.Services.JwtService;
import com.example.chatApp.Services.Impl.UserDetailsServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;
    private final Map<String, Set<WebSocketSession>> userSessions = new ConcurrentHashMap<>();

    public ChatWebSocketHandler(
            JwtService jwtService,
            UserDetailsServiceImpl userDetailsService,
            ObjectMapper objectMapper
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String token = getQueryParam(session.getUri(), "token");
        if (token == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Missing token"));
            return;
        }

        String username;
        try {
            username = jwtService.extractUserName(token);
            var userDetails = userDetailsService.loadUserByUsername(username);
            if (!jwtService.isTokenValid(token, userDetails)) {
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid token"));
                return;
            }
        } catch (Exception e) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid token"));
            return;
        }

        session.getAttributes().put("username", username);
        userSessions.computeIfAbsent(username, key -> ConcurrentHashMap.newKeySet()).add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        removeSession(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        removeSession(session);
        if (session.isOpen()) {
            session.close(CloseStatus.SERVER_ERROR);
        }
    }

    public void notifyUsers(String sender, String receiver, String message) {
        MessageEvent event = new MessageEvent("NEW_MESSAGE", sender, receiver, message);
        String payload = toJson(event);
        if (payload == null) {
            return;
        }
        sendToUser(sender, payload);
        if (!Objects.equals(sender, receiver)) {
            sendToUser(receiver, payload);
        }
    }

    private void sendToUser(String username, String payload) {
        Set<WebSocketSession> sessions = userSessions.get(username);
        if (sessions == null || sessions.isEmpty()) {
            return;
        }

        for (WebSocketSession session : sessions) {
            if (!session.isOpen()) {
                sessions.remove(session);
                continue;
            }
            try {
                session.sendMessage(new TextMessage(payload));
            } catch (IOException ignored) {
                sessions.remove(session);
            }
        }
    }

    private void removeSession(WebSocketSession session) {
        Object usernameAttr = session.getAttributes().get("username");
        if (!(usernameAttr instanceof String username)) {
            return;
        }
        Set<WebSocketSession> sessions = userSessions.get(username);
        if (sessions == null) {
            return;
        }
        sessions.remove(session);
        if (sessions.isEmpty()) {
            userSessions.remove(username);
        }
    }

    private String getQueryParam(URI uri, String key) {
        if (uri == null || uri.getQuery() == null) {
            return null;
        }
        String[] queryParts = uri.getQuery().split("&");
        for (String queryPart : queryParts) {
            String[] keyValue = queryPart.split("=", 2);
            if (keyValue.length == 2 && key.equals(keyValue[0])) {
                return URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    private String toJson(MessageEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private record MessageEvent(String type, String sender, String receiver, String message) {}
}
