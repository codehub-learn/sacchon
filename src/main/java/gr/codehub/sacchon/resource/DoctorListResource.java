package gr.codehub.sacchon.resource;

import gr.codehub.sacchon.exception.AuthorizationException;
import gr.codehub.sacchon.jpaUtil.JpaUtil;
import gr.codehub.sacchon.model.Doctor;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import gr.codehub.sacchon.repository.DoctorRepository;
import gr.codehub.sacchon.representation.DoctorRepresentation;
import gr.codehub.sacchon.security.Shield;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class DoctorListResource extends ServerResource {
    @Get("json")
    public List<DoctorRepresentation> getDoctor() throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_CHIEF_DOCTOR);

        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);
        List<Doctor> doctors = doctorRepository.findAll();
        em.close();

        List<DoctorRepresentation> doctorRepresentationList = new ArrayList<>();
        for (Doctor p : doctors)
            doctorRepresentationList.add(new DoctorRepresentation(p));

        return doctorRepresentationList;
    }

    @Post("json")
    public DoctorRepresentation add(DoctorRepresentation doctorRepresentationIn) throws AuthorizationException {
        ResourceUtils.checkRole(this, Shield.ROLE_CHIEF_DOCTOR);
        if (doctorRepresentationIn == null) return null;
        if (doctorRepresentationIn.getUsername() == null) return null;
        if (doctorRepresentationIn.getPassword() == null) return null;

        Doctor doctor = doctorRepresentationIn.createDoctor();
        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);
        doctorRepository.save(doctor);
        DoctorRepresentation p = new DoctorRepresentation(doctor);
        return p;
    }
}
