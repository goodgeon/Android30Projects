package com.example.a14secondhandtrade.chatlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a14secondhandtrade.DBKey.Companion.CHILD_CHAT
import com.example.a14secondhandtrade.DBKey.Companion.DB_USERS
import com.example.a14secondhandtrade.R
import com.example.a14secondhandtrade.chatdetail.ChatRoomActivity
import com.example.a14secondhandtrade.databinding.FragmentChatlistBinding
import com.example.a14secondhandtrade.home.ArticleAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatListFragment : Fragment(R.layout.fragment_chatlist) {
    private var binding: FragmentChatlistBinding? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val chatRoomList = mutableListOf<ChatListItem>()

    private lateinit var chatListAdapter: ChatListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentChatlistBinding = FragmentChatlistBinding.bind(view)
        binding = fragmentChatlistBinding

        chatListAdapter = ChatListAdapter { chatListItem ->
            val intent = Intent(requireContext(), ChatRoomActivity::class.java)
            intent.putExtra("chatKey", chatListItem.key)
            startActivity(intent)
        }


        chatRoomList.clear()

        fragmentChatlistBinding.chatListRecyclerView.adapter = chatListAdapter
        fragmentChatlistBinding.chatListRecyclerView.layoutManager = LinearLayoutManager(context)

        if (auth.currentUser == null) {
            return
        }

        auth.currentUser?.let { currentUser ->
            val chatDB = Firebase.database.reference.child(DB_USERS).child(currentUser.uid)
            Log.d("GG", "currentUser: ${currentUser.uid}")
//            chatDB.child(CHILD_CHAT).addListenerForSingleValueEvent(object: ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    Log.d("GG", "addListenerForSingleValueEvent")
//                    snapshot.children.forEach {
//                        val model = it.getValue(ChatListItem::class.java)
//                        model ?: return
//
//                        chatRoomList.add(model)
//
//                        chatListAdapter.submitList(chatRoomList)
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {}
//
//            })
            chatDB.child(CHILD_CHAT).addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val model = snapshot.getValue(ChatListItem::class.java)
                    model ?: return
                    Log.d("GG", "onChildAdded2 - ${model.itemTitle}")
                    chatRoomList.add(model)
                    chatListAdapter.submitList(chatRoomList)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildRemoved(snapshot: DataSnapshot) {}

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {}

            })
        }
    }

    override fun onResume() {
        super.onResume()
        chatListAdapter.notifyDataSetChanged()
    }
}