package gr.codehub.sacchon.resource;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class PingServerResource extends ServerResource {
    @Get("json")
    public String ping() {
        return "Sacchon API is working!";
    }

}