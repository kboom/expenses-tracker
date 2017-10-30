package com.ggurgul.playground.extracker.auth.services

import org.springframework.stereotype.Component

import java.io.Serializable
import java.util.Date

@Component
class TimeService : Serializable {

    fun now(): Date {
        return Date()
    }

    companion object {

        private const val serialVersionUID = -3301695478208950415L
    }
}
