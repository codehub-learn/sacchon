package gr.codehub.sacchon;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import gr.codehub.sacchon.router.CustomRouter;
import gr.codehub.sacchon.security.CorsFilter;
import gr.codehub.sacchon.security.Shield;

import java.util.logging.Logger;

public class Main extends Application {
    public static final Logger LOGGER = Engine.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        LOGGER.info("Sacchon app starting");

        Component c = new Component();
        c.getServers().add(Protocol.HTTP, 9000);
        c.getDefaultHost().attach("/v1", new Main());
        c.start();

        LOGGER.info("sample web api started");
        LOGGER.info("URl: http://localhost:9000/v1/ping");
    }


    //crating router, controller
    public Restlet createInboundRoot() {
        CustomRouter customRouter = new CustomRouter(this);
        Shield shield = new Shield(this);

        Router publicRouter = customRouter.publicResources();

        // Create the api router, protected by a patient guard

        ChallengeAuthenticator guard = shield.createApiGuard();
        Router userRouter = customRouter.protectedResources();
        guard.setNext(userRouter);
        publicRouter.attach(guard);

        CorsFilter corsFilter = new CorsFilter(this);
        return corsFilter.createCorsFilter(publicRouter);

    }


}
