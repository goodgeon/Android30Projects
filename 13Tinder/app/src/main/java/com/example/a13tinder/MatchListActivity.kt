package com.example.a13tinder

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MatchListActivity: AppCompatActivity() {
    private val adapter = MatchedUserAdapter()
    private lateinit var usersDB: DatabaseReference
    private var auth: FirebaseAuth = Firebase.auth
    private val cardItems = mutableListOf<CardItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        val recyclerView = findViewById<RecyclerView>(R.id.matchedUserRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        usersDB = FirebaseDatabase.getInstance().reference.child("Users")

        getMatchUsers()
    }

    private fun getMatchUsers() {
        val matchedDB = usersDB.child(getCurrentUserID()).child("likedBy").child("match")
        matchedDB.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.key?.isNotEmpty() == true) {
                    getMatchUser(snapshot.key.orEmpty())
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun getMatchUser(userId: String) {
        val matchedDB = usersDB.child(userId)
        matchedDB.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                cardItems.add(CardItem(userId,snapshot.child("name").value.toString()))
                adapter.submitList(cardItems)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun getCurrentUserID(): String {
        if(auth.currentUser == null) {
            Toast.makeText(this, "로그인 되어있지 않습니다", Toast.LENGTH_SHORT).show()
            finish()
        }

        return auth.currentUser?.uid.orEmpty()
    }
}