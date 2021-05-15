package gr.codehub.sacchon.service.impl;

import gr.codehub.sacchon.model.Patient;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import gr.codehub.sacchon.repository.PatientRepository;
import gr.codehub.sacchon.representation.PatientRepresentation;
import gr.codehub.sacchon.service.PatientService;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;



//mocking the repository

class PatientServiceImplTest   {


    @Mock
    private PatientRepository patientRepositoryMock  ;
    @Mock
    private EntityManager emMock  ;

    @InjectMocks
    PatientService patientService = new PatientServiceImpl();


    @BeforeEach
    void init_mocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void save() {
        assertNotNull(patientRepositoryMock);
        Patient patientExp = new Patient();

        when(patientRepositoryMock.save(any(Patient.class)) ).thenReturn(patientExp);



        PatientRepresentation patientRepresentation = new PatientRepresentation();

        patientRepresentation.setName("S0kis");
        patientRepresentation.setUsername("s0kis");
        patientRepresentation.setPassword("1011");

        Patient patientFront = patientRepresentation.createPatient();

        try {
            PatientRepresentation patientReprReturned = patientService.save(patientRepresentation);
            assertEquals(patientRepresentation.getName(), patientReprReturned.getName());
        }
        catch(Exception e){
            assertEquals(1,2);
        }


    }
}