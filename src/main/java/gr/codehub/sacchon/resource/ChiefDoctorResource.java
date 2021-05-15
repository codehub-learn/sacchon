package gr.codehub.sacchon.resource;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.ChiefDoctor;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.ChiefDoctorRepository;
import gr.codehub.sacchon.representation.ChiefDoctorRepresentation;

import javax.persistence.EntityManager;

public class ChiefDoctorResource extends ServerResource {
    private long id;

    protected void doInit() {
        id = Long.parseLong(getAttribute("id"));
    }


    @Get("json")
    public ChiefDoctorRepresentation getChiefDoctor() throws AuthorizationException {
//        ResourceUtils.checkRole(this, Shield.ROLE_CHIEF_DOCTOR);
        EntityManager em = JpaUtil.getEntityManager();
        ChiefDoctorRepository chiefDoctorRepository = new ChiefDoctorRepository(em);
        ChiefDoctor chiefDoctor = chiefDoctorRepository.read(id);
        ChiefDoctorRepresentation chiefDoctorRepresentation = new ChiefDoctorRepresentation(chiefDoctor);
        em.close();
        return chiefDoctorRepresentation;
    }
}
