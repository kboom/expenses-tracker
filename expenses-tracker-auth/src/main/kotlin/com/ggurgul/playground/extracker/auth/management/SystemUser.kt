package com.toptal.ggurgul.timezones.security

data class SystemUser(val id: Long = -1) {

    companion object {
        fun get(): SystemUser {
            return SystemUser()
        }
    }

}