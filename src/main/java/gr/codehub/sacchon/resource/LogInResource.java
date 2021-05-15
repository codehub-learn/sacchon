package gr.codehub.sacchon.resource;

import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.ChiefDoctor;
import gr.codehub.sacchon.model.Doctor;
import gr.codehub.sacchon.model.Patient;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.ChiefDoctorRepository;
import gr.codehub.sacchon.repository.DoctorRepository;
import gr.codehub.sacchon.repository.PatientRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class LogInResource extends ServerResource {


    @Get
    public List<Integer> logIn() {
        String username = getQueryValue("username");
        String password = getQueryValue("password");
        List<Integer> result = new ArrayList<>(3);

        Patient patient = isPatient(username, password);
        if (patient != null) {
            result.add(1);
            result.add((int) patient.getId());
            result.add(patient.isConsultationChanged() ? 1 : 0);
            resetFlag(patient);
        }

        Doctor doctor = isDoctor(username, password);
        if (doctor != null) {
            result.add(2);
            result.add((int) doctor.getId());
        }

        ChiefDoctor chiefDoctor = isChiefDoctor(username, password);
        if (chiefDoctor != null) {
            result.add(3);
            result.add((int) chiefDoctor.getId());
        }
        return result;
    }

    public Patient isPatient(String username, String password) {
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        Patient patient = patientRepository.getByUsername(username);
        if (patient != null) {
            if (patient.getUsername().equals(username) && patient.getPassword().equals(password)) {
                return patient;
            }
        }
        return null;
    }

    public Doctor isDoctor(String username, String password) {
        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);
        Doctor doctor = doctorRepository.getByUsername(username);
        if (doctor != null) {
            if (doctor.getUsername().equals(username) && doctor.getPassword().equals(password)) {
                return doctor;
            }
        }
        return null;
    }

    public ChiefDoctor isChiefDoctor(String username, String password) {
        EntityManager em = JpaUtil.getEntityManager();
        ChiefDoctorRepository chiefDoctorRepository = new ChiefDoctorRepository(em);
        ChiefDoctor chiefDoctor = chiefDoctorRepository.getByUsername(username);
        if (chiefDoctor != null) {
            if (chiefDoctor.getUsername().equals(username) && chiefDoctor.getPassword().equals(password)) {
                return chiefDoctor;
            }
        }
        return null;
    }

    public void resetFlag(Patient patient) {
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        patient.setConsultationChanged(false);
        patientRepository.update(patient);
    }


}
