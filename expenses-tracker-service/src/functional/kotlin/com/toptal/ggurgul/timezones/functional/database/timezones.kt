package com.toptal.ggurgul.timezones.functional.database

import com.ninja_squad.dbsetup.operation.Insert
import com.ninja_squad.dbsetup_kotlin.mappedValues

enum class Timezone(
        val id: Long,
        val customName: String,
        val locationName: String,
        val diffToGMT: Int
) {

    KRAKOW(
            id = 100L,
            customName = "My Krakow",
            locationName = "Krakow",
            diffToGMT = 2
    ),
    WARSAW(
            id = 101L,
            customName = "My Warsaw",
            locationName = "Warsaw",
            diffToGMT = 2
    ),
    TOKYO(
            id = 102L,
            customName = "My Tokyo",
            locationName = "Tokyo",
            diffToGMT = 9
    ),
    SYDNEY(
            id = 103L,
            customName = "My Sydney",
            locationName = "Sydney",
            diffToGMT = 10
    )

}

fun insertTimezone(insertBuilder: Insert.Builder, timezone: Timezone, owner: User) {
    insertBuilder.mappedValues(
            "id" to timezone.id,
            "location_name" to timezone.locationName,
            "name" to timezone.customName,
            "diff_to_gmt" to timezone.diffToGMT,
            "owner_id" to owner.id
    )
}