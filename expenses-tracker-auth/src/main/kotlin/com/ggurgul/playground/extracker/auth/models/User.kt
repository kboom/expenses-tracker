package com.ggurgul.playground.extracker.auth.models

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "USERS")
class User(

        @Id
        @Column(name = "ID")
        @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
        val id: Long? = null,

        @Column(name = "USERNAME", length = 50, unique = true)
        @Size(min = 4, max = 50)
        val username: String,

        @Column(name = "password")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        var password: String? = null,

        @Column(name = "FIRST_NAME", length = 50)
        @Size(min = 4, max = 50)
        var firstName: String? = null,

        @Column(name = "LAST_NAME", length = 50)
        @Size(min = 4, max = 50)
        var lastName: String? = null,

        @Column(name = "EMAIL", length = 50)
        @NotNull
        @Size(min = 4, max = 50)
        var email: String,

        @Column(name = "ENABLED")
        @NotNull
        var enabled: Boolean = false,

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("USER_ID", "AUTHORITY_NAME"))), name = "USER_AUTHORITIES", joinColumns = arrayOf(JoinColumn(name = "USER_ID", referencedColumnName = "ID")), inverseJoinColumns = arrayOf(JoinColumn(name = "AUTHORITY_NAME", referencedColumnName = "AUTHORITY_NAME")))
        var authorities: MutableList<Authority> = mutableListOf(),

        @OneToMany
        @JoinColumn(name = "id")
        var boundIdentities: MutableList<BoundIdentity> = mutableListOf()

)