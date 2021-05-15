package gr.codehub.sacchon.resource;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Consultation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.ConsultationRepository;
import gr.codehub.sacchon.representation.ConsultationRepresentation;
import gr.codehub.sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConsultationListResource extends ServerResource {
    @Get("json")
    public List<ConsultationRepresentation> getConsultation() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_CHIEF_DOCTOR);
        EntityManager em = JpaUtil.getEntityManager();
        ConsultationRepository consultationRepository = new ConsultationRepository(em);
        List<Consultation> consultationList = consultationRepository.findAll();
        em.close();

        List<ConsultationRepresentation> consultationRepresentationList = new ArrayList<>();
        for (Consultation p : consultationList)
            consultationRepresentationList.add(new ConsultationRepresentation(p));

        return consultationRepresentationList;
    }

    @Post("json")
    public ConsultationRepresentation add(ConsultationRepresentation consultationRepresentationIn) throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_CHIEF_DOCTOR);
        if (consultationRepresentationIn == null) return null;

        Consultation consultation = consultationRepresentationIn.createConsultation();
        if (consultation.getDate() == null) consultation.setDate(new Date());

        EntityManager em = JpaUtil.getEntityManager();
        ConsultationRepository consultationListRepository = new ConsultationRepository(em);
        consultationListRepository.save(consultation);
        ConsultationRepresentation p = new ConsultationRepresentation(consultation);

        return p;
    }
}
