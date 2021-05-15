package gr.codehub.sacchon.service;

import gr.codehub.sacchon.exception.EntityException;
import gr.codehub.sacchon.representation.PatientRepresentation;

public interface PatientService {
    PatientRepresentation save(PatientRepresentation patientRepresentation) throws EntityException;
}
