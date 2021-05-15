package gr.codehub.sacchon.resource;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Consultation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.ConsultationRepository;
import gr.codehub.sacchon.representation.ConsultationRepresentation;
import gr.codehub.sacchon.security.Shield;

import javax.persistence.EntityManager;

public class ConsultationResource extends ServerResource {
    private long id;

    protected void doInit() {
        id = Long.parseLong(getAttribute("id"));
    }


    @Get("json")
    public ConsultationRepresentation getConsultation() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_CHIEF_DOCTOR);
        EntityManager em = JpaUtil.getEntityManager();
        ConsultationRepository consultationRepository = new ConsultationRepository(em);
        Consultation consultation = consultationRepository.read(id);
        ConsultationRepresentation consultationRepresentation = new ConsultationRepresentation(consultation);
        em.close();
        return consultationRepresentation;
    }
}
