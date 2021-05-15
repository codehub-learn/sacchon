package gr.codehub.sacchon.resource.doctor;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Consultation;
import gr.codehub.sacchon.model.Patient;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.ConsultationRepository;
import gr.codehub.sacchon.repository.DoctorRepository;
import gr.codehub.sacchon.repository.PatientRepository;
import gr.codehub.sacchon.representation.ConsultationRepresentation;
import gr.codehub.sacchon.resource.ResourceUtils;
import gr.codehub.sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.util.List;

public class DoctorConsultationResource extends ServerResource {
    private long doctorId;
    private long consultationId;

    protected void doInit() {
        doctorId = Long.parseLong(getAttribute("doctorId"));
        consultationId = Long.parseLong(getAttribute("consultationId"));
    }

    @Get("json")
    public ConsultationRepresentation getConsultation() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);
        List<Consultation> consultationList = doctorRepository.getConsultationList(this.doctorId);
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

    @Put("json")
    public ConsultationRepresentation updateConsultation(ConsultationRepresentation consultationRepresentation) throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
        if (consultationRepresentation == null) return null;
        EntityManager em = JpaUtil.getEntityManager();
        ConsultationRepository consultationRepository = new ConsultationRepository(em);
        Consultation consultation = consultationRepresentation.createConsultation();
        em.detach(consultation);
        consultation.setId(consultationId);
        consultationRepository.update(consultation);

        PatientRepository patientRepository = new PatientRepository(em);
        Patient patient = patientRepository.read(consultationRepresentation.getPatientId());
        patient.setConsultationChanged(true);
        patientRepository.update(patient);
        return consultationRepresentation;
    }

}
