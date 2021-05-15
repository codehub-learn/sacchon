package gr.codehub.sacchon.resource.patient;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.PatientRepository;
import gr.codehub.sacchon.resource.ResourceUtils;
import gr.codehub.sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

public class PatientCarbDailyAverageResource extends ServerResource {
    private long patientId;

    protected void doInit() {

        patientId = Long.parseLong(getAttribute("patientId"));
    }

    @Get
    public List<Double> getAverageCarb() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_PATIENT);
        String start = getQueryValue("start");
        String end = getQueryValue("end");
        Date dateStart = ResourceUtils.stringToDate(start, -1);
        Date dateEnd = ResourceUtils.stringToDate(end, 1);

        EntityManager em = JpaUtil.getEntityManager();

        PatientRepository patientRepository = new PatientRepository(em);
        List<Double> carbList = patientRepository.getCarbAverageList(this.patientId, dateStart, dateEnd);

        em.close();
        return carbList;
    }
}
