package gr.codehub.sacchon.resource.patient;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Carb;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.CarbRepository;
import gr.codehub.sacchon.repository.PatientRepository;
import gr.codehub.sacchon.representation.CarbRepresentation;
import gr.codehub.sacchon.resource.ResourceUtils;
import gr.codehub.sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.util.List;

public class PatientCarbResource extends ServerResource {
    private long patientId;
    private long carbId;

    protected void doInit() {
        patientId = Long.parseLong(getAttribute("patientId"));
        carbId = Long.parseLong(getAttribute("carbId"));
    }


    @Get("json")
    public CarbRepresentation getCarb() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
        EntityManager em = JpaUtil.getEntityManager();

        PatientRepository patientRepository = new PatientRepository(em);
        List<Carb> carbList = patientRepository.getCarbList(this.patientId);
        Carb carb = new Carb();

        for (Carb c : carbList) {
            if (c.getId() == carbId) {
                carb = c;
            }
        }
        CarbRepresentation carbRepresentation = new CarbRepresentation(carb);
        em.close();
        return carbRepresentation;
    }

    @Put("json")
    public CarbRepresentation updateCarb(CarbRepresentation carbRepresentation) throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
        if (carbRepresentation == null) return null;

        carbRepresentation.setId(carbId);
        carbRepresentation.setPatientId(patientId);
        EntityManager em = JpaUtil.getEntityManager();
        CarbRepository carbRepository = new CarbRepository(em);
        Carb carb = carbRepresentation.createCarb();
        em.detach(carb);
        carb.setId(carbId);
        carbRepository.update(carb);
        return carbRepresentation;
    }

    @Delete("json")
    public void deleteCarb() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
        EntityManager em = JpaUtil.getEntityManager();
        CarbRepository carbRepository = new CarbRepository(em);
        carbRepository.delete(carbRepository.read(carbId).getId());
    }
}