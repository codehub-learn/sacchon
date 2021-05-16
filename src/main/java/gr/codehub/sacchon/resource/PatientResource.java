package gr.codehub.sacchon.resource;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.exception.EntityException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Patient;
import gr.codehub.sacchon.service.PatientService;
import gr.codehub.sacchon.service.impl.PatientServiceImpl;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.PatientRepository;
import gr.codehub.sacchon.representation.PatientRepresentation;
import gr.codehub.sacchon.security.Shield;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;

public class PatientResource extends ServerResource {
    private long id;
    private PatientService patientService;
    private EntityManager entityManager;

    @Override
    protected void doInit() {
        entityManager = JpaUtil.getEntityManager();
        patientService = new PatientServiceImpl(entityManager);
        id = Long.parseLong(getAttribute("id"));
    }

    @Override
    protected void doRelease(){
        entityManager.close();
    }
    @Get("json")
    public PatientRepresentation getPatient() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_CHIEF_DOCTOR);
        return patientService.readPatient(id);
    }

    @Put("json")
    public PatientRepresentation updatePatient(PatientRepresentation patientRepresentation) throws AuthorizationException, EntityException {
        ResourceUtils.checkRole(this, Shield.ROLE_CHIEF_DOCTOR);
        return patientService.updatePatient(id, patientRepresentation);
    }

    @Delete("json")
    public boolean deletePatient() throws AuthorizationException, EntityException {
        ResourceUtils.checkRole(this, Shield.ROLE_CHIEF_DOCTOR);
        return patientService.deletePatient(id);
    }

}
