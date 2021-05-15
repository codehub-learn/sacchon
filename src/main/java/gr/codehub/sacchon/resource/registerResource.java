package gr.codehub.sacchon.resource;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Patient;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.PatientRepository;
import gr.codehub.sacchon.representation.PatientRepresentation;

import javax.persistence.EntityManager;
import java.util.Date;

public class registerResource extends ServerResource {


    @Post("json")
    public PatientRepresentation add(PatientRepresentation patientRepresentationIn) throws AuthorizationException {

        //validation
        if (patientRepresentationIn == null) return null;
        if (patientRepresentationIn.getUsername() == null) return null;
        if (patientRepresentationIn.getPassword() == null) return null;

        Patient patient = patientRepresentationIn.createPatient();
        if (patientRepresentationIn.getDateRegistered() == null) patient.setDateRegistered(new Date());
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        patientRepository.save(patient);
        PatientRepresentation p = new PatientRepresentation(patient);
        em.close();
        return p;
    }

}
