package gr.codehub.sacchon.resource.chiefDoctor;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Glucose;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.PatientRepository;
import gr.codehub.sacchon.representation.GlucoseRepresentation;
import gr.codehub.sacchon.resource.ResourceUtils;
import gr.codehub.sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReportPatientGlucoseListResource extends ServerResource {
    private long patientId;

    protected void doInit() {
        patientId = Long.parseLong(getAttribute("patientId"));
    }

    @Get
    public List<GlucoseRepresentation> getPatientGlucoseList() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_CHIEF_DOCTOR);
        String period = getQueryValue("period");

        Date date1 = ResourceUtils.stringToDate(period, 0);
        Date date = new Date();
        long diff = date.getTime() - date1.getTime();
        Long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        List<Glucose> glucoseList = patientRepository.getGlucoseList(patientId, days);
        List<GlucoseRepresentation> glucoseRepresentationList = new ArrayList<>();
        for (Glucose glucose : glucoseList)
            glucoseRepresentationList.add(new GlucoseRepresentation(glucose));
        em.close();
        return glucoseRepresentationList;
    }
}