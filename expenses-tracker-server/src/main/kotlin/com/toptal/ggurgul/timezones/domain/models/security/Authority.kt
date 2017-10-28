package com.toptal.ggurgul.timezones.domain.models.security

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "AUTHORITIES")
class Authority {

    @Id
    @Column(name = "AUTHORITY_NAME")
    @Enumerated(EnumType.STRING)
    var name: AuthorityName? = null

}