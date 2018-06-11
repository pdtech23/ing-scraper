package scrapper.ing.security;

public class UnauthenticatedSession {

    public final String salt;
    public final String mask;
    public final String key;
    public final String unauthenticatedSessionId;

    public UnauthenticatedSession(String salt, String mask, String key, String unauthenticatedSessionId) {
        this.salt = salt;
        this.mask = mask;
        this.key = key;
        this.unauthenticatedSessionId = unauthenticatedSessionId;
    }
}
