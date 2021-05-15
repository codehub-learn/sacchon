package gr.codehub.sacchon.resource.patient;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Carb;
import gr.codehub.sacchon.model.Patient;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;
import gr.codehub.sacchon.repository.CarbRepository;
import gr.codehub.sacchon.repository.PatientRepository;
import gr.codehub.sacchon.representation.CarbRepresentation;
import gr.codehub.sacchon.resource.ResourceUtils;
import gr.codehub.sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class PatientCarbListResource extends ServerResource {
    private long patientId;

    protected void doInit() {

      //  patientId = Long.parseLong(getAttribute("patientId"));
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
//   Request request = Request.getCurrent();


       Series headers = (Series) getRequestAttributes().get("org.restlet.http.headers");
      String username = headers.getFirstValue("UserId");
        patientId = patientRepository.getByUsername(username).getId();
    }


    @Get("json")
    public List<CarbRepresentation> getCarbList() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
        EntityManager em = JpaUtil.getEntityManager();

        PatientRepository patientRepository = new PatientRepository(em);
        List<Carb> carbList = patientRepository.getCarbList(this.patientId);
        List<CarbRepresentation> carbRepresentationList = new ArrayList<>();

        for (Carb c : carbList) {
            carbRepresentationList.add(new CarbRepresentation(c));
        }

        em.close();
        return carbRepresentationList;
    }

    @Post("json")
    public CarbRepresentation addCarb(CarbRepresentation carbRepresentationIn) throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
        if (carbRepresentationIn == null) return null;

        carbRepresentationIn.setPatientId(this.patientId);
        Carb carb = carbRepresentationIn.createCarb();
        EntityManager em = JpaUtil.getEntityManager();
        CarbRepository carbRepository = new CarbRepository(em);
        carbRepository.save(carb);
        CarbRepresentation c = new CarbRepresentation(carb);

        PatientRepository patientRepository = new PatientRepository(em);
        Patient patient = patientRepository.read(patientId);

        em.detach(patient);
        patient.setRecentCarb(carb.getDate());
        patientRepository.update(patient);

        em.close();

        return c;
    }

}

