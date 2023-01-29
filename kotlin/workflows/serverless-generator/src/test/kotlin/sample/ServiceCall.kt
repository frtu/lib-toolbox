package sample

import java.util.UUID

interface ServiceCall {
    fun query(request: ServiceRequest): ServiceResponse
}

data class ServiceRequest(val id: UUID?, val name: String?)
data class ServiceResponse(val id: UUID, val name: String, val description: String)