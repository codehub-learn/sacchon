package gr.codehub.sacchon.security;

import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.ChiefDoctor;
import gr.codehub.sacchon.model.Doctor;
import gr.codehub.sacchon.model.Patient;
import org.restlet.Request;
import org.restlet.security.Role;
import org.restlet.security.SecretVerifier;
import gr.codehub.sacchon.repository.ChiefDoctorRepository;
import gr.codehub.sacchon.repository.DoctorRepository;
import gr.codehub.sacchon.repository.PatientRepository;

import javax.persistence.EntityManager;

public class Verifier extends SecretVerifier {

    public int verify(String username, char[] password) {
        //check db for user
        EntityManager em = JpaUtil.getEntityManager();

        PatientRepository patientRepository = new PatientRepository(em);
        Patient patient = patientRepository.getByUsername(username);
        if (patient != null) {
            if (patient.getUsername().equals(username)) {
                String passwordInDb = patient.getPassword();
                if (compare(passwordInDb.toCharArray(), password)) {
                    Request request = Request.getCurrent();
                    request.getClientInfo().getRoles().add
                            (new Role(patient.getRole()));
                    em.close();
                    return SecretVerifier.RESULT_VALID;
                }
            }
        }
        DoctorRepository doctorRepository = new DoctorRepository(em);
        Doctor doctor = doctorRepository.getByUsername(username);
        if (doctor != null) {
            if (doctor.getUsername().equals(username)) {
                String passwordInDb = doctor.getPassword();
                if (compare(passwordInDb.toCharArray(), password)) {
                    Request request = Request.getCurrent();
                    request.getClientInfo().getRoles().add
                            (new Role(doctor.getRole()));
                    em.close();
                    return SecretVerifier.RESULT_VALID;
                }
            }
        }

        ChiefDoctorRepository chiefDoctorRepository = new ChiefDoctorRepository(em);
        ChiefDoctor chiefDoctor = chiefDoctorRepository.getByUsername(username);
        if (chiefDoctor != null) {
            if (chiefDoctor.getUsername().equals(username)) {
                String passwordInDb = chiefDoctor.getPassword();
                if (compare(passwordInDb.toCharArray(), password)) {
                    Request request = Request.getCurrent();
                    request.getClientInfo().getRoles().add
                            (new Role(chiefDoctor.getRole()));
                    em.close();
                    return SecretVerifier.RESULT_VALID;
                }
            }
        }
        em.close();
        return SecretVerifier.RESULT_INVALID;
    }
}
