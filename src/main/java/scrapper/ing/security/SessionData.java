package scrapper.ing.security;

public class SessionData {

    public static final SessionData EMPTY = new SessionData();

    private String token;
    private String sessionId;

    public SessionData(String token, String sessionId) {
        this.token = token;
        this.sessionId = sessionId;
    }

    private SessionData() {
    }

    public String getToken() {
        return this.token;
    }

    public String getSessionId() {
        return this.sessionId;
    }
}
