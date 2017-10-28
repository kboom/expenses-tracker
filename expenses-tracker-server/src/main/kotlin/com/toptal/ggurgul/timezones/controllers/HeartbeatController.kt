package com.toptal.ggurgul.timezones.controllers

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(value = "health", description = "Instance health operations", tags = arrayOf("health"))
@RequestMapping(value = "/health")
class HeartbeatController {

    @ApiOperation(value = "Check heartbeat")
    @ApiResponses(
            ApiResponse(code = 200, message = "Application is responsive")
    )
    @GetMapping(path = arrayOf("/heartbeat"))
    fun index(): String {
        return "ping"
    }

}