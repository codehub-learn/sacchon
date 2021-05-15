package gr.codehub.sacchon.resource;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Patient;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.PatientRepository;
import gr.codehub.sacchon.representation.PatientRepresentation;
import gr.codehub.sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientListResource extends ServerResource {
    @Get("json")
    public List<PatientRepresentation> getPatient() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_CHIEF_DOCTOR);

        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        List<Patient> patients = patientRepository.findAll();
        em.close();

        List<PatientRepresentation> patientRepresentationList = new ArrayList<>();
        for (Patient p : patients)
            patientRepresentationList.add(new PatientRepresentation(p));

        return patientRepresentationList;
    }

    @Post("json")
    public PatientRepresentation add(PatientRepresentation patientRepresentationIn) throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_CHIEF_DOCTOR);
        if (patientRepresentationIn == null) return null;
        if (patientRepresentationIn.getUsername() == null) return null;
        if (patientRepresentationIn.getPassword() == null) return null;

        Patient patient = patientRepresentationIn.createPatient();
        if (patientRepresentationIn.getDateRegistered() == null) patient.setDateRegistered(new Date());
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        patientRepository.save(patient);
        PatientRepresentation p = new PatientRepresentation(patient);
        return p;
    }
}
