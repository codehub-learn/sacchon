package gr.codehub.sacchon.resource.chiefDoctor;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Consultation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.DoctorRepository;
import gr.codehub.sacchon.representation.ConsultationRepresentation;
import gr.codehub.sacchon.resource.ResourceUtils;
import gr.codehub.sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReportDoctorConsultationListResource extends ServerResource {
    private long doctorId;

    protected void doInit() {
        doctorId = Long.parseLong(getAttribute("doctorId"));
    }

    @Get
    public List<ConsultationRepresentation> getConsultationList() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_CHIEF_DOCTOR);

        String period = getQueryValue("period");
        Date date1 = ResourceUtils.stringToDate(period, 0);
        Date date = new Date();
        long diff = date.getTime() - date1.getTime();
        Long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);

        List<Consultation> consultationList = doctorRepository.getConsultationList(doctorId, days);
        List<ConsultationRepresentation> consultationRepresentationList = new ArrayList<>();

        for (Consultation p : consultationList)
            consultationRepresentationList.add(new ConsultationRepresentation(p));

        em.close();
        return consultationRepresentationList;

    }
}