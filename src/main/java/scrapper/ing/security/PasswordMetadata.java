package scrapper.ing.security;

public class PasswordMetadata {

    public static final PasswordMetadata EMPTY = new PasswordMetadata();

    private String salt;
    private String mask;
    private String key;
    private String unauthenticatedSessionId;

    public PasswordMetadata(String salt, String mask, String key, String unauthenticatedSessionId) {
        this.salt = salt;
        this.mask = mask;
        this.key = key;
        this.unauthenticatedSessionId = unauthenticatedSessionId;
    }

    private PasswordMetadata() {
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
