package com.toptal.ggurgul.timezones.domain.models.security

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "USERS")
class User {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    var id: Long? = null

    @Column(name = "USERNAME", length = 50, unique = true)
    @NotNull
    @Size(min = 4, max = 50)
    var username: String? = null

    @Column(name = "PASSWORD")
    @NotNull
    @JsonProperty(access = WRITE_ONLY)
    var password: String? = null

    @Column(name = "FIRST_NAME", length = 50)
    @Size(min = 4, max = 50)
    var firstName: String? = null

    @Column(name = "LAST_NAME", length = 50)
    @Size(min = 4, max = 50)
    var lastName: String? = null

    @Column(name = "EMAIL", length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    var email: String? = null

    @Column(name = "ENABLED")
    @NotNull
    var enabled: Boolean? = null

    @Column(name = "LAST_PWD_RST_DT")
    @Temporal(TemporalType.TIMESTAMP)
    var lastPasswordResetDate: Date? = null

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("USER_ID", "AUTHORITY_NAME"))), name = "USER_AUTHORITIES", joinColumns = arrayOf(JoinColumn(name = "USER_ID", referencedColumnName = "ID")), inverseJoinColumns = arrayOf(JoinColumn(name = "AUTHORITY_NAME", referencedColumnName = "AUTHORITY_NAME")))
    var authorities: List<Authority>? = null

    @Transient
    var oldPassword: String? = null

    @PostLoad
    fun rememberOldUser() {
        this.oldPassword = this.password;
    }

}