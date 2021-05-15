package gr.codehub.sacchon.exception;

public class AuthorizationException extends Exception {
    public AuthorizationException(String description) {
        super(description);
    }
}
