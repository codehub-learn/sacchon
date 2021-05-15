package gr.codehub.sacchon.resource.doctor;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Patient;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.DoctorRepository;
import gr.codehub.sacchon.repository.PatientRepository;
import gr.codehub.sacchon.representation.PatientRepresentation;
import gr.codehub.sacchon.resource.ResourceUtils;
import gr.codehub.sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.util.List;

public class DoctorNeedConsultationPatientResource extends ServerResource {
    private long doctorId;
    private long needConsultationPatientId;

    protected void doInit() {
        doctorId = Long.parseLong(getAttribute("doctorId"));
        needConsultationPatientId = Long.parseLong(getAttribute("needConsultationPatientId"));
    }

    @Get("json")
    public PatientRepresentation getPatient() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);
        List<Patient> patientList = doctorRepository.getNeedConsultationPatientList(this.doctorId);

        Patient patient = new Patient();
        for (Patient p : patientList) {
            if (p.getId() == needConsultationPatientId) {
                patient = p;
            }
        }
        PatientRepresentation patientRepresentation = new PatientRepresentation(patient);

        em.close();

        return patientRepresentation;
    }

    @Put("json")
    public PatientRepresentation updatePatient(PatientRepresentation patientRepresentation) throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        Patient patient = patientRepresentation.createPatient();
        em.detach(patient);
        patient.setId(needConsultationPatientId);
        patientRepository.update(patient);
        return patientRepresentation;
    }
}
