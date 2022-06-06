package com.example.a14secondhandtrade.chatdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a14secondhandtrade.DBKey.Companion.DB_CHATS
import com.example.a14secondhandtrade.DBKey.Companion.DB_USERS
import com.example.a14secondhandtrade.R
import com.example.a14secondhandtrade.databinding.ActivityChatroomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatRoomActivity: AppCompatActivity() {
    /*TODO
    DB_CHATS -> 채팅방 키("chatKey", default: -1)의 child들을 리스트에 저장 -> submit
    send: chatDB에 저장

     */
    private val chatDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DB_CHATS)
    }

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val binding: ActivityChatroomBinding by lazy {
        ActivityChatroomBinding.inflate(layoutInflater)
    }
    private val adapter = ChatItemAdapter()

    private val chatList = mutableListOf<ChatItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.chatRecyclerView.adapter = adapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)

        val chatKey = intent.getStringExtra("chatKey")
        binding.sendButton.setOnClickListener {
            auth.currentUser?.let { currentUser ->
                val model = ChatItem(
                    currentUser.uid,
                    binding.messageEditText.text.toString()
                )

                chatDB.child(chatKey.toString())
                    .push()
                    .setValue(model)

                binding.messageEditText.setText("")
            }


        }

        chatDB.child(chatKey.toString()).addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val model = snapshot.getValue(ChatItem::class.java)
                model ?: return
                chatList.add(model)
                adapter.submitList(chatList)
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}

        })


    }
}