package gr.codehub.sacchon.service;

import gr.codehub.sacchon.exception.EntityException;
import gr.codehub.sacchon.representation.PatientRepresentation;
import gr.codehub.sacchon.representation.ResultData;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface PatientService {
    //CRUD
    ResultData<PatientRepresentation> createPatient(PatientRepresentation patientRepresentation)  ;
    List<PatientRepresentation> readPatient();
    List<PatientRepresentation> readPatient(PatientRepresentation patientRepresentation);
    ResultData<PatientRepresentation> readPatient(long patientId)throws EntityNotFoundException;
    PatientRepresentation updatePatient (long patientId, PatientRepresentation patientRepresentation)
            throws EntityNotFoundException, EntityException;
    boolean deletePatient(long patientId) throws EntityNotFoundException, EntityException;

}
