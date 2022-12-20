package db.service.crypto.model;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;

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
