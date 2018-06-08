package scrapper.ing.security;

public class UnauthenticatedSession {

    private final String salt;
    private final String mask;
    private final String key;
    private final String unauthenticatedSessionId;

    public UnauthenticatedSession(String salt, String mask, String key, String unauthenticatedSessionId) {
        this.salt = salt;
        this.mask = mask;
        this.key = key;
        this.unauthenticatedSessionId = unauthenticatedSessionId;
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
}
