package gr.codehub.sacchon.resource.patient;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Glucose;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.GlucoseRepository;
import gr.codehub.sacchon.repository.PatientRepository;
import gr.codehub.sacchon.representation.GlucoseRepresentation;
import gr.codehub.sacchon.resource.ResourceUtils;
import gr.codehub.sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.util.List;

public class PatientGlucoseResource extends ServerResource {
    private long patientId;
    private long glucoseId;

    protected void doInit() {
        patientId = Long.parseLong(getAttribute("patientId"));
        glucoseId = Long.parseLong(getAttribute("glucoseId"));
    }
//to check
protected void destroy(){

}
    @Get("json")
    public GlucoseRepresentation getGlucose() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        List<Glucose> glucoseList = patientRepository.getGlucoseList(this.patientId);
        Glucose glucose = new Glucose();
        for (Glucose g : glucoseList) {
            if (g.getId() == glucoseId) {
                glucose = g;
            }
        }
        GlucoseRepresentation glucoseRepresentation = new GlucoseRepresentation(glucose);
        em.close();
        return glucoseRepresentation;
    }

    @Put("json")
    public GlucoseRepresentation updateGlucose(GlucoseRepresentation glucoseRepresentationIn) throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
        if (glucoseRepresentationIn == null) return null;

        EntityManager em = JpaUtil.getEntityManager();
        GlucoseRepository glucoseRepository = new GlucoseRepository(em);
        Glucose glucose = glucoseRepository.read(glucoseId);
        glucose.setGlucose(glucoseRepresentationIn.getGlucose());

        em.detach(glucose);
        glucose.setId(glucoseId);
        glucoseRepository.update(glucose);
        return glucoseRepresentationIn;
    }

    @Delete("json")
    public void deleteGlucose() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
        EntityManager em = JpaUtil.getEntityManager();
        GlucoseRepository glucoseRepository = new GlucoseRepository(em);
        glucoseRepository.delete(glucoseRepository.read(glucoseId).getId());
    }
}
