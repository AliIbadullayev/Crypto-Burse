package db.service.crypto.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


@Entity
@Table(name="admin")
@Data
public class Admin {

    @Id
    @Column(name="user_login")
    private String userLogin;


    @OneToOne
    @PrimaryKeyJoinColumn()
    private User user;

    @Column
    private String name;
}
