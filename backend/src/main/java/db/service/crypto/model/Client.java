package db.service.crypto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


@Entity
@Table(name="client")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client{

    @Id
    @Column(name="user_login")
    private String userLogin;

    @OneToOne
    @PrimaryKeyJoinColumn()
    private User user;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    private double fiatBalance;
}
