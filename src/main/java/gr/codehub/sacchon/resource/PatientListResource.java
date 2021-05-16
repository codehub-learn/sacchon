package gr.codehub.sacchon.resource;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.exception.EntityException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Patient;
import gr.codehub.sacchon.representation.ResultData;
import gr.codehub.sacchon.service.PatientService;
import gr.codehub.sacchon.service.impl.PatientServiceImpl;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.PatientRepository;
import gr.codehub.sacchon.representation.PatientRepresentation;
import gr.codehub.sacchon.security.Shield;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientListResource extends ServerResource {
    private PatientService patientService ;
    private EntityManager entityManager;


    @Override
    protected void doInit() {
        entityManager = JpaUtil.getEntityManager();
        patientService = new PatientServiceImpl(entityManager);
    }

    @Override
    protected void doRelease(){
        entityManager.close();
    }

    @Get("json")
    public List<PatientRepresentation> getPatient() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_CHIEF_DOCTOR);
        return patientService.readPatient();
    }

    @Post("json")
    public ResultData<PatientRepresentation> add(PatientRepresentation patientRepresentationIn) throws AuthorizationException, EntityException {
        ResourceUtils.checkRole(this, Shield.ROLE_CHIEF_DOCTOR);
        return patientService.createPatient(patientRepresentationIn);
    }



}
