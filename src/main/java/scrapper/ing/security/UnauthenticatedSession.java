package scrapper.ing.security;

public class UnauthenticatedSession {

    public static final UnauthenticatedSession EMPTY = new UnauthenticatedSession();

    private String salt;
    private String mask;
    private String key;
    private String unauthenticatedSessionId;

    public UnauthenticatedSession(String salt, String mask, String key, String unauthenticatedSessionId) {
        this.salt = salt;
        this.mask = mask;
        this.key = key;
        this.unauthenticatedSessionId = unauthenticatedSessionId;
    }

    private UnauthenticatedSession() {
    }

    public String getSalt() {
        return this.salt;
    }

    public String getMask() {
        return this.mask;
    }

    public String getKey() {
        return this.key;
    }

    public String getUnauthenticatedSessionId() {
        return this.unauthenticatedSessionId;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
