package util;

/**
 * GameState Object
 */
public enum State {
    New         (""), // A new session or nothing has progress since
    CheckPoint1 ("8d8763cb3aa452b743c594f370c347b8"), // Passed the first checkpoint
    CheckPoint2 ("db315b07dddf87159c2238612325a242")  // Passed the second checkpoint
    ;


    private final String secret;

    State(String secret) {
        this.secret = secret;
    }

    public String getSecret() {
        return secret;
    }
}
