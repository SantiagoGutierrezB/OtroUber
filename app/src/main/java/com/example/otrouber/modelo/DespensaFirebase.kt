package com.example.uber2.modelo

import com.example.otrouber.modelo.Item
import com.google.firebase.database.*

class DespensaFirebase {

    private lateinit var database: DatabaseReference// ...

    constructor(){
        database = FirebaseDatabase.getInstance().reference
    }

    fun cargaUnItem(item: Item): Item {
        val key = database.child("despensa").push().key
        item.id = key!!
        database.child("despensa").child(key!!).setValue(item)

        return item
    }

    fun borraUnItem(item: Item) {
        database.child("despensa").child(item.id!!).removeValue()
    }

    fun borraTodo() {
        database.child("despensa").removeValue()
    }

    fun modificaUnItem( item: Item) {
        val childUpdates = HashMap<String, Any>()
        var newValues = item.toMap()
        val key = item.id
        childUpdates["/despensa/$key"] = newValues

        database.updateChildren(childUpdates)

    }

}