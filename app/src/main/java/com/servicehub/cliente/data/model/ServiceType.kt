package com.servicehub.cliente.data.model

/** Fixed catalog of services available to the client. */
enum class ServiceType(val displayName: String) {
    PLUMBER("Plomero"),
    ELECTRICIAN("Electricista"),
    LOCKSMITH("Cerrajero"),
    COMPUTER_TECHNICIAN("Técnico de computadoras"),
    VETERINARIAN("Veterinario"),
    PET_CAREGIVER("Cuidador de mascotas"),
    BABYSITTER("Niñera"),
    GARDENING("Jardinería"),
    CLEANING("Limpieza"),
    OTHER("Otro");

    companion object {
        fun fromDisplayName(displayName: String): ServiceType? = entries.firstOrNull { it.displayName == displayName }
    }
}
