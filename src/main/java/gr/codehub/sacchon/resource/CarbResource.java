package gr.codehub.sacchon.resource;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Carb;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.CarbRepository;
import gr.codehub.sacchon.representation.CarbRepresentation;
import gr.codehub.sacchon.security.Shield;

import javax.persistence.EntityManager;

public class CarbResource extends ServerResource {
    private long id;

    protected void doInit() {
        id = Long.parseLong(getAttribute("id"));
    }


    @Get("json")
    public CarbRepresentation getCarb() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_CHIEF_DOCTOR);
        EntityManager em = JpaUtil.getEntityManager();
        CarbRepository carbRepository = new CarbRepository(em);
        Carb carb = carbRepository.read(id);
        CarbRepresentation carbRepresentation = new CarbRepresentation(carb);
        em.close();
        return carbRepresentation;
    }
}
