package com.toptal.ggurgul.timezones.domain.models

import com.toptal.ggurgul.timezones.domain.models.security.User
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.hibernate.validator.constraints.Length
import org.springframework.data.annotation.ReadOnlyProperty
import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@ApiModel(value = "Timezone", description = "A representation of timezone")
@Entity
@Table(name = "TIMEZONES")
data class Timezone(

        @Id
        @Column(name = "ID")
        @SequenceGenerator(name = "timezone_seq", sequenceName = "timezone_seq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timezone_seq")
        @get:ApiModelProperty(readOnly = true, hidden = true)
        @get:ReadOnlyProperty
        var id: Long? = null,

        @Column(name = "NAME", length = 50)
        @get:NotNull
        @get:Length(min = 4, max = 30)
        var name: String? = null,

        @Column(name = "LOCATION_NAME", length = 50)
        @get:NotNull
        @get:Length(min = 4, max = 30)
        var locationName: String? = null,

        @Column(name = "DIFF_TO_GMT")
        @get:NotNull
        @get:Min(-12)
        @get:Max(12)
        var differenceToGMT: Int? = null,

        @ManyToOne
        @get:NotNull
        @get:ReadOnlyProperty
        @get:ApiModelProperty(readOnly = true, hidden = true)
        var owner: User? = null

)