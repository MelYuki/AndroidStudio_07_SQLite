package be.melyuki.demo07_sqlite.models

import java.time.LocalDate

data class Person(
    val id: Long = 0,
    val firstname: String,
    val lastname: String,
    val birthDate: LocalDate,
    val email: String?,
    val phone: String?
)
