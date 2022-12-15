package db.service.crypto.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="admin")
@Data
public class Admin {

    @Id
    @Column
    private String user_login;


    @OneToOne
    @PrimaryKeyJoinColumn()
    private User user;

    @Column
    private String name;
}
