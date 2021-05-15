package gr.codehub.sacchon.model;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class User {
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String password;

    private String name;

    private String email;
    private String address;


}
