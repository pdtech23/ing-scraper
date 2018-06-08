package scrapper.ing.security;

public class AuthenticatedSession {

    private final String token;
    private final String authenticatedSessionId;

    public AuthenticatedSession(String token, String sessionId) {
        this.token = token;
        this.authenticatedSessionId = sessionId;
    }

    public String getToken() {
        return this.token;
    }

    public String getAuthenticatedSessionId() {
        return this.authenticatedSessionId;
    }
}
