package com.ggurgul.playground.extracker.auth.models

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "USERS", indexes = arrayOf(
        Index(name = "email", unique = true, columnList = "email")
))
class User(

        // todo use uuid
        /**
         * Used mainly because user emails should not be exposed in rest resources
         */
        @Id
        @Column(name = "id")
        @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
        val id: Long? = null,

        /**
         * The email is an e-mail account as this provides out of the box uniqueness.
         */
        @Column(name = "email", length = 50, unique = true)
        @Size(min = 4, max = 50)
        val email: String,

        @Column(name = "password")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        var password: String? = null,

        @Column(name = "first_name", length = 50)
        @Size(min = 4, max = 50)
        var firstName: String? = null,

        @Column(name = "last_name", length = 50)
        @Size(min = 4, max = 50)
        var lastName: String? = null,

        @Column(name = "enabled")
        @NotNull
        var enabled: Boolean = false,

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("USER_ID", "AUTHORITY_NAME"))), name = "USER_AUTHORITIES", joinColumns = arrayOf(JoinColumn(name = "USER_ID", referencedColumnName = "ID")), inverseJoinColumns = arrayOf(JoinColumn(name = "AUTHORITY_NAME", referencedColumnName = "AUTHORITY_NAME")))
        var authorities: MutableList<Authority> = mutableListOf(),

        @OneToMany(cascade = arrayOf(CascadeType.ALL))
        @JoinColumn(name = "identity")
        var boundIdentities: MutableList<BoundIdentity> = mutableListOf()

)