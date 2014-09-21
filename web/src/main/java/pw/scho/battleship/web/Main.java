package pw.scho.battleship.web;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting server on port 8080");

        Server server = new Server(8080);

        HandlerList handlers = new HandlerList();

        handlers.setHandlers(new Handler[]{getStaticContentHandler(), getJerseyHandler(), new DefaultHandler()});

        server.setHandler(handlers);

        server.start();
        server.join();
    }

    private static Handler getStaticContentHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("./web/src/main/webapp/");
        return resourceHandler;
    }

    private static Handler getJerseyHandler() {
        ServletHolder h = new ServletHolder(new ServletContainer());

        h.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "pw.scho.battleship.web.resources");
        h.setInitParameter(ServerProperties.APPLICATION_NAME, "battleship");
        h.setInitOrder(1);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(h, "/*");
        context.setContextPath("/api");
        return context;
    }

}