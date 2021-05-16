package gr.codehub.sacchon.service;

import gr.codehub.sacchon.exception.EntityException;
import gr.codehub.sacchon.representation.PatientRepresentation;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface PatientService {
    //CRUD
    PatientRepresentation createPatient(PatientRepresentation patientRepresentation) throws EntityException;
    List<PatientRepresentation> readPatient();
    List<PatientRepresentation> readPatient(PatientRepresentation patientRepresentation);
    PatientRepresentation readPatient(long patientId)throws EntityNotFoundException;
    PatientRepresentation updatePatient (long patientId, PatientRepresentation patientRepresentation)
            throws EntityNotFoundException, EntityException;
    boolean deletePatient(long patientId) throws EntityNotFoundException, EntityException;

}
