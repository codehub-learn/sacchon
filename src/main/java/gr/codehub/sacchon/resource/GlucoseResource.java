package gr.codehub.sacchon.resource;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Glucose;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.GlucoseRepository;
import gr.codehub.sacchon.representation.GlucoseRepresentation;
import gr.codehub.sacchon.security.Shield;

import javax.persistence.EntityManager;

public class GlucoseResource extends ServerResource{
    private long id;

    protected void doInit(){
        id= Long.parseLong(getAttribute("id"));
    }


    @Get("json")
    public GlucoseRepresentation getGlucose() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_CHIEF_DOCTOR);
        EntityManager em= JpaUtil.getEntityManager();
        GlucoseRepository glucoseRepository= new GlucoseRepository(em);
        Glucose glucose= glucoseRepository.read(id);
        GlucoseRepresentation glucoseRepresentation= new GlucoseRepresentation(glucose);
        em.close();
        return glucoseRepresentation;
    }
}
