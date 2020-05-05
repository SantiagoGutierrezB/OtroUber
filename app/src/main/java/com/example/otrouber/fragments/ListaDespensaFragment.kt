package com.example.otrouber.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.otrouber.R
import com.example.otrouber.modelo.Item
import com.example.uber2.modelo.DespensaFirebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tutorialsbuzz.recylerviewswipetodelete.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.fragment_lista_despensa.*

/**
 * A simple [Fragment] subclass.
 */
class ListaDespensaFragment : Fragment() {
    var list = mutableListOf<Item>()
    val despensa = DespensaFirebase()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_despensa, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        list_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = DespensaRecyclerAdapter(mutableListOf<Item>())

            val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val pos = viewHolder.adapterPosition

                    //Swipe a la derecha para borrar
                    if (direction == 8) {
                        despensa.borraUnItem(list.get(pos))
                    }

                    //Swipe a la izquierda para editar
                    else if (direction == 4) {
                        showAlert(activity!!.findViewById(R.id.main), list.get(pos))
                    }

                }
            }

            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(this)
        }
        getProducts()
    }

    fun getProducts(){
        val ref = FirebaseDatabase.getInstance().getReference("/despensa")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                list = mutableListOf<Item>()
                p0.children.forEach {
                    val product = it.getValue(Item::class.java)
                    list.add(product!!)

                }
                list_recycler_view.adapter = DespensaRecyclerAdapter(list)
            }


        } )
    }

    fun showAlert(view: View, item: Item) {
        val builder = AlertDialog.Builder(view.context)
        val inflater = layoutInflater
        builder.setTitle("Editar Producto")
        val dialogLayout = inflater.inflate(R.layout.alert_dialog, null)
        var cantidad  = dialogLayout.findViewById<EditText>(R.id.cantidad_edittext)
        var descripcion = dialogLayout.findViewById<EditText>(R.id.description_edittext)
        cantidad.setText(item.cantidad.toString())
        descripcion.setText(item.descripcion.toString())

        builder.setView(dialogLayout)
        builder.setPositiveButton("Update") { dialogInterface, i ->
            item.cantidad = cantidad.text.toString().toInt()
            item.descripcion = descripcion.text.toString()
            despensa.modificaUnItem(item)
        }
        builder.show()

    }

}
