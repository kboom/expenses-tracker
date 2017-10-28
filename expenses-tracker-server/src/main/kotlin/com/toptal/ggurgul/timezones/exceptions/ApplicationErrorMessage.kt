package com.toptal.ggurgul.timezones.exceptions

import io.swagger.annotations.ApiModel

@ApiModel(value = "Error", description = "A common representation for an error")
data class ApplicationErrorMessage(
        var code: Int? = null,
        var message: String? = null
)