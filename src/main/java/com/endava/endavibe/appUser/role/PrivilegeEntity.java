package com.endava.endavibe.appUser.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "privilege", schema = "public", catalog = "endavibe")
public class PrivilegeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1945670924947820279L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @ManyToMany(mappedBy = "privileges")
    @JsonIgnore
    private Collection<RoleEntity> roles;
}
