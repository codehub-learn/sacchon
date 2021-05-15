package gr.codehub.sacchon.resource.doctor;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Patient;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.DoctorRepository;
import gr.codehub.sacchon.representation.PatientRepresentation;
import gr.codehub.sacchon.resource.ResourceUtils;
import gr.codehub.sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.util.List;

public class DoctorPatientResource extends ServerResource {
    private long doctorId;
    private long patientId;

    protected void doInit() {
        doctorId = Long.parseLong(getAttribute("doctorId"));
        patientId = Long.parseLong(getAttribute("patientId"));
    }

    @Get("json")
    public PatientRepresentation getPatient() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_DOCTOR);
        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);
        List<Patient> patientList = doctorRepository.getPatientList(this.doctorId);
        Patient patient = new Patient();
        for (Patient p : patientList) {
            if (p.getId() == patientId) {
                patient = p;
            }
        }

        PatientRepresentation patientRepresentation = new PatientRepresentation(patient);
        em.close();

        return patientRepresentation;
    }


}
