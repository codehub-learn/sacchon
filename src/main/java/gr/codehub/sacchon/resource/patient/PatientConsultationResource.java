package gr.codehub.sacchon.resource.patient;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Consultation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.PatientRepository;
import gr.codehub.sacchon.representation.ConsultationRepresentation;
import gr.codehub.sacchon.resource.ResourceUtils;
import gr.codehub.sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.util.List;

public class PatientConsultationResource extends ServerResource {
    private long patientId;
    private long consultationId;

    protected void doInit() {
        patientId = Long.parseLong(getAttribute("patientId"));
        consultationId = Long.parseLong(getAttribute("consultationId"));
    }


    @Get("json")
    public ConsultationRepresentation getConsultation() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
        EntityManager em = JpaUtil.getEntityManager();

        PatientRepository patientRepository = new PatientRepository(em);
        List<Consultation> consultationList = patientRepository.getConsultationList(this.patientId);
        Consultation consultation = new Consultation();
        for (Consultation c : consultationList) {
            if (c.getId() == consultationId) {
                consultation = c;
            }
        }
        ConsultationRepresentation consultationRepresentation = new ConsultationRepresentation(consultation);
        em.close();
        return consultationRepresentation;
    }

}
