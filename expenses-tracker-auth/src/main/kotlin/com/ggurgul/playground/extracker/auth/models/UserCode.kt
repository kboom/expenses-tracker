package com.ggurgul.playground.extracker.auth.models

import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@Table(name = "USER_CODES")
class UserCode {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "user_code_seq", sequenceName = "user_code_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_code_seq")
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    var user: User? = null

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    var type: UserCodeType? = null

    @Column(name = "SENT_TO")
    @NotNull
    var sentToEmail: String? = null

    @Column(name = "CODE")
    @NotNull
    var code: String? = null

}