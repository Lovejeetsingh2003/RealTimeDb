package com.lovejeet.realtimedb

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lovejeet.realtimedb.databinding.ActivityMainBinding
import com.lovejeet.realtimedb.databinding.CustomdialogBinding

class MainActivity : AppCompatActivity(), ListInterface {
    lateinit var binding: ActivityMainBinding
    lateinit var recyclerAdapter: RecyclerAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var dbReference : DatabaseReference
    var notes:ArrayList<Notes> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbReference = FirebaseDatabase.getInstance().reference

        recyclerAdapter= RecyclerAdapter(notes,this)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvNotes.layoutManager = layoutManager
        binding.rvNotes.adapter = recyclerAdapter

       getCollectionData()

        binding.fabAdd.setOnClickListener {
            var dialog=Dialog(this)
            var dialogBinding: CustomdialogBinding
            dialogBinding= CustomdialogBinding.inflate(layoutInflater)
            dialog.setContentView(dialogBinding.root)
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.show()
            dialogBinding.textClock.setOnClickListener {
                Toast.makeText(this@MainActivity, "TextClock", Toast.LENGTH_SHORT).show()
            }
            dialogBinding.btnAdd.setOnClickListener {
                if (dialogBinding.etTitle.text?.isEmpty() == true) {
                    dialogBinding.etTitle.error = "Enter Title"
                } else if (dialogBinding.etDescription.text?.isEmpty() == true) {
                    dialogBinding.etDescription.error = "Enter Description"
                } else {
                    dialogBinding.textClock.visibility = View.VISIBLE
                    var db = Notes()
                    db.title = dialogBinding.etTitle.text.toString()
                    db.description = dialogBinding.etDescription.text.toString()
                    db.time = dialogBinding.textClock.text.toString()
                    dbReference.child("Users").push().setValue(db)
                    getCollectionData()
                    dialog.dismiss()
                    Toast.makeText(this,"Real Time Data Add",Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun getCollectionData() {
        dbReference.child("Users").addChildEventListener(object : ChildEventListener{

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var notesData = snapshot.getValue(Notes::class.java)
                if (notesData != null) {
                    notes.add(notesData)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
            }



    override fun onDeleteClick(notes: Notes, position: Int) {
        AlertDialog.Builder(this)
            .setTitle(this.resources.getString(R.string.delete))
            .setMessage(this.resources.getString(R.string.delete_msg))
            .setPositiveButton(this.resources.getString(R.string.yes)) { _, _ ->
//                dbReference.child("Users").get(notes.id).delete()
//                recyclerAdapter.notes.removeAt(notes.id)
            }
            .setNegativeButton(this.resources.getString(R.string.no)){ _, _->

            }
            .show()
    }

    override fun onUpdateClick(notes: Notes, position: Int) {
        var dialog=Dialog(this)
        var dialogBinding : CustomdialogBinding
        dialogBinding= CustomdialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogBinding.btnAdd.text = "Update"
        dialogBinding.textClock.visibility = View.GONE
        dialogBinding.etTitle.setText(notes.title)
        dialogBinding.etDescription.setText(notes.description)
        dialog.show()

        dialogBinding.btnAdd.setOnClickListener {
            if (dialogBinding.etTitle.text?.isEmpty() == true) {
                dialogBinding.etTitle.error = "Enter Title"
            } else if (dialogBinding.etDescription.text?.isEmpty() == true) {
                dialogBinding.etDescription.error = "Enter Description"
            } else {
                var updateNotes = Notes(
                    title = dialogBinding.etTitle.text.toString(),
                    description = dialogBinding.etDescription.text.toString(),
                    time = dialogBinding.textClock.text.toString(),
                    id = notes.id ?: ""
                )
//                firestore.collection("Users")
//                    .document(notes.id ?: "")
//                    .set(updateNotes)
//                    .addOnSuccessListener {
//                        Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show()
//                        getCollectionData()
//                    }
//                    .addOnFailureListener {
//                        Toast.makeText(this, "Data Updation Failed", Toast.LENGTH_SHORT).show()
//                    }
//                    .addOnCanceledListener {
//                        Toast.makeText(this, "Data Updation Cancelled", Toast.LENGTH_SHORT).show()
//                    }
                dialog.dismiss()
            }
        }
    }}