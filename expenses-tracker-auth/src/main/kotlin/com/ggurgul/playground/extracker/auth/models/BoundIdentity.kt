package com.ggurgul.playground.extracker.auth.models

import javax.persistence.*

@Entity
@Table(name = "BOUND_IDENTITY")
class BoundIdentity(

        @Id
        @Column(name = "ID")
        @SequenceGenerator(name = "bound_identity_seq", sequenceName = "bound_identity_seq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bound_identity_seq")
        val id: Long? = null,

        @ManyToOne
        @JoinColumn(name = "user_id")
        val user: User,

        @Column(name = "internal_id")
        val internalId: String,

        @Enumerated(value = EnumType.STRING)
        val identityType: IdentityType

)