package pw.scho.battleship.core;

public class ServiceException extends Exception {

    private Kind kind;

    private ServiceException(Kind kind) {
        this.kind = kind;
    }

    public static ServiceException createUnauthorized() {
        return new ServiceException(Kind.UNAUTHORIZED);
    }

    public static ServiceException createInvalidAction() {
        return new ServiceException(Kind.INVALID_ACTION);
    }

    public static ServiceException createNotFound() {
        return new ServiceException(Kind.NOT_FOUND);
    }

    public Kind getKind() {
        return kind;
    }

    public enum Kind {
        UNAUTHORIZED,
        INVALID_ACTION,
        NOT_FOUND
    }
}
