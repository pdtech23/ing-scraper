package scrapper.ing.security;

public class AuthenticatedSession {

    public static final AuthenticatedSession EMPTY = new AuthenticatedSession();

    private String token;
    private String authenticatedSessionId;

    public AuthenticatedSession(String token, String sessionId) {
        this.token = token;
        this.authenticatedSessionId = sessionId;
    }

    private AuthenticatedSession() {
    }

    public String getToken() {
        return this.token;
    }

    public String getAuthenticatedSessionId() {
        return this.authenticatedSessionId;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
