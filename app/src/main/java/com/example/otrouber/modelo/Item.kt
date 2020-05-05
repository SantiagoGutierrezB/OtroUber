package com.example.otrouber.modelo

data class Item(
    var id: String? = "",
    var descripcion: String? = "",
    var cantidad: Int? = 0
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "descripcion" to descripcion,
            "cantidad" to cantidad
        )
    }
}
