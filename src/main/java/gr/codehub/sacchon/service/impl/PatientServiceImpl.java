package gr.codehub.sacchon.service.impl;


import gr.codehub.sacchon.exception.EntityException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Patient;
import gr.codehub.sacchon.repository.PatientRepository;
import gr.codehub.sacchon.representation.PatientRepresentation;
import gr.codehub.sacchon.service.PatientService;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PatientServiceImpl implements PatientService {
    private PatientRepository patientRepository;


    public PatientServiceImpl(EntityManager em) {
        this.patientRepository = new PatientRepository(em);
    }


    @Override
    public PatientRepresentation createPatient(PatientRepresentation patientRepresentation) throws EntityException {
        if (patientRepresentation == null) return null;
        if (patientRepresentation.getUsername() == null) return null;
        if (patientRepresentation.getPassword() == null) return null;

        Patient patient = patientRepresentation.createPatient();
        if (patientRepresentation.getDateRegistered() == null) patient.setDateRegistered(new Date());

        patientRepository.save(patient);
        PatientRepresentation p = new PatientRepresentation(patient);

        return p;
    }

    @Override
    public List<PatientRepresentation> readPatient() {

        return   patientRepository.findAll()
                .stream()
                .map(PatientRepresentation::new)
                .collect(Collectors.toList());

    }

    @Override
    public List<PatientRepresentation> readPatient(PatientRepresentation patientRepresentation) {
        //not implemented
        return null;
    }

    @Override
    public PatientRepresentation readPatient(long patientId) throws EntityNotFoundException {

        Patient patient = patientRepository.read(patientId);
        PatientRepresentation patientRepresentation = new PatientRepresentation(patient);

        return patientRepresentation;
    }

    @Override
    public PatientRepresentation updatePatient(long patientId, PatientRepresentation patientRepresentation) throws EntityNotFoundException, EntityException {
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        Patient patient = patientRepresentation.createPatient();
        Patient oldPatient = patientRepository.read(patientId);
        em.detach(patient);
        patient.setId(patientId);
        patient.setDateRegistered(oldPatient.getDateRegistered());
        patientRepository.update(patient);

        return patientRepresentation;
    }

    @Override
    public boolean deletePatient(long patientId) throws EntityNotFoundException, EntityException {
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        Patient patient = patientRepository.read(patientId);
        if (patient == null) return false;
        return patientRepository.delete(patient.getId());
    }
}
