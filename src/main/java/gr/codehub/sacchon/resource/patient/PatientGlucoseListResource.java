package gr.codehub.sacchon.resource.patient;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Glucose;
import gr.codehub.sacchon.model.Patient;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.GlucoseRepository;
import gr.codehub.sacchon.repository.PatientRepository;
import gr.codehub.sacchon.representation.GlucoseRepresentation;
import gr.codehub.sacchon.resource.ResourceUtils;
import gr.codehub.sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class PatientGlucoseListResource extends ServerResource {
    private long patientId;

    protected void doInit() {
        patientId = Long.parseLong(getAttribute("patientId"));
    }


    @Get("json")
    public List<GlucoseRepresentation> getGlucoseList() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
        EntityManager em = JpaUtil.getEntityManager();

        PatientRepository patientRepository = new PatientRepository(em);
        List<Glucose> glucoseList = patientRepository.getGlucoseList(this.patientId);
        List<GlucoseRepresentation> glucoseRepresentationList = new ArrayList<>();

        for (Glucose c : glucoseList) {
            glucoseRepresentationList.add(new GlucoseRepresentation(c));
        }

        em.close();
        return glucoseRepresentationList;
    }

    @Post("json")
    public GlucoseRepresentation add(GlucoseRepresentation glucoseRepresentationIn) throws AuthorizationException {

        ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);


        if (glucoseRepresentationIn == null) return null;

        glucoseRepresentationIn.setPatientId(this.patientId);
        Glucose glucose = glucoseRepresentationIn.createGlucose();
        EntityManager em = JpaUtil.getEntityManager();
        GlucoseRepository glucoseRepository = new GlucoseRepository(em);
        glucoseRepository.save(glucose);
        GlucoseRepresentation g = new GlucoseRepresentation(glucose);

        PatientRepository patientRepository = new PatientRepository(em);
        Patient patient = patientRepository.read(patientId);
        em.detach(patient);
        patient.setRecentGlucose(glucose.getDate());
        patientRepository.update(patient);

        em.close();

        return g;
    }
}
