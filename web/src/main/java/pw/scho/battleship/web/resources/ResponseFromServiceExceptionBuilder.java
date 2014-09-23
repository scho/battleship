package pw.scho.battleship.web.resources;

import pw.scho.battleship.core.ServiceException;

import javax.ws.rs.core.Response;

public class ResponseFromServiceExceptionBuilder {

    private final ServiceException exception;

    public ResponseFromServiceExceptionBuilder(ServiceException exception) {
        this.exception = exception;
    }

    public Response build() {
        javax.ws.rs.core.Response.Status status = null;
        switch (exception.getKind()) {
            case INVALID_ACTION:
                status = javax.ws.rs.core.Response.Status.CONFLICT;
                break;
            case NOT_FOUND:
                status = javax.ws.rs.core.Response.Status.NOT_FOUND;
                break;
            case UNAUTHORIZED:
                status = javax.ws.rs.core.Response.Status.UNAUTHORIZED;
                break;
        }
        return Response.status(status).build();
    }
}
