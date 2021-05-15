package gr.codehub.sacchon.service.impl;


import gr.codehub.sacchon.exception.EntityException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Patient;
import gr.codehub.sacchon.repository.PatientRepository;
import gr.codehub.sacchon.representation.PatientRepresentation;
import gr.codehub.sacchon.service.PatientService;

import javax.persistence.EntityManager;
import java.util.Date;

public class PatientServiceImpl implements PatientService {
  //  private PatientRepository patientRepository;


  //  public PatientServiceImpl(PatientRepository patientRepository){
  //      this.patientRepository=patientRepository;
  //  }

    @Override
    public PatientRepresentation save(PatientRepresentation patientRepresentationIn) throws EntityException {


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
