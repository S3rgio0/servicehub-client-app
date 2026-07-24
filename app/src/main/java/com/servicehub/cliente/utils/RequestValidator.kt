package com.servicehub.cliente.utils

/** Holds validation messages for the request form. */
data class RequestFieldErrors(
    val clientNameError: String? = null,
    val serviceTypeError: String? = null,
    val descriptionError: String? = null,
) {
    val hasErrors: Boolean
        get() = clientNameError != null || serviceTypeError != null || descriptionError != null
}

object RequestValidator {
    fun validate(clientName: String, serviceType: String, description: String): RequestFieldErrors {
        val nameError = if (clientName.isBlank()) "El nombre del cliente es obligatorio." else null
        val serviceError = if (serviceType.isBlank()) "Debes seleccionar un tipo de servicio." else null
        val descriptionError = if (description.isBlank()) "La descripción es obligatoria." else null

        return RequestFieldErrors(
            clientNameError = nameError,
            serviceTypeError = serviceError,
            descriptionError = descriptionError
        )
    }
}
