package com.example.a14secondhandtrade.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a14secondhandtrade.DBKey.Companion.CHILD_CHAT
import com.example.a14secondhandtrade.DBKey.Companion.DB_ARTICLES
import com.example.a14secondhandtrade.DBKey.Companion.DB_USERS
import com.example.a14secondhandtrade.R
import com.example.a14secondhandtrade.chatlist.ChatListItem
import com.example.a14secondhandtrade.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class HomeFragment: Fragment(R.layout.fragment_home) {
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var articleDB: DatabaseReference
    private lateinit var userDB: DatabaseReference

    private var binding: FragmentHomeBinding? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val articleList = mutableListOf<ArticleModel>()
    private val listener = object: ChildEventListener{
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val articleModel = snapshot.getValue(ArticleModel::class.java)

            articleModel ?: return

            articleList.add(articleModel)

            articleAdapter.submitList(articleList)

        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentHomeBinding = FragmentHomeBinding.bind(view)
        binding = fragmentHomeBinding

        articleList.clear()

        articleDB = FirebaseDatabase.getInstance().reference.child(DB_ARTICLES)
        userDB = FirebaseDatabase.getInstance().reference.child(DB_USERS)

        articleAdapter = ArticleAdapter(
            onItemClicked = { articleModel ->
                if(auth.currentUser != null) {
                    auth.currentUser?.let { currentUser ->
                        if(currentUser.uid != articleModel.sellerId ) {

                            val key = "${currentUser.uid}_${articleModel.sellerId}_${articleModel.title}"

                            val chatRoom = ChatListItem(
                                currentUser.uid,
                                articleModel.sellerId,
                                articleModel.title,
                                key
                            )

                            userDB.child(currentUser.uid)
                                .child(CHILD_CHAT).orderByChild("key").equalTo(key).addListenerForSingleValueEvent(object: ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        snapshot.children.forEach {
                                        }
                                        if(snapshot.exists()) {
                                            Snackbar.make(view, "이미 생성된 채팅방입니다", Snackbar.LENGTH_SHORT).show()
                                            return
                                        } else {
                                            userDB.child(currentUser.uid)
                                                .child(CHILD_CHAT)
                                                .child(key).setValue(chatRoom)
                                            Snackbar.make(view, "채팅방이 생성되었습니다", Snackbar.LENGTH_SHORT).show()
                                        }

                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }

                                })
//                            userDB.child(currentUser.uid)
//                                .child(CHILD_CHAT)
//                                .push()
//                                .setValue(chatRoom)
//
//                            userDB.child(articleModel.sellerId)
//                                .child(CHILD_CHAT)
//                                .push()
//                                .setValue(chatRoom)

                        } else {
                            Snackbar.make(view, "내가 등록한 아이템입니다", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Snackbar.make(view, "로그인 후 이용해 주세요", Snackbar.LENGTH_SHORT).show()
                }
            }
        )

        fragmentHomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHomeBinding.articleRecyclerView.adapter = articleAdapter

        fragmentHomeBinding.addFloatingButton.setOnClickListener {
            context?.let {
                if (auth.currentUser != null) {
                    startActivity(Intent(requireContext(), AddArticleActivity::class.java))
                } else {
                    Snackbar.make(view, "로그인 후 사용해 주세요", Snackbar.LENGTH_SHORT).show()
                }
            }

        }

        articleDB.addChildEventListener(listener)
    }

    override fun onResume() {
        super.onResume()
        articleAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        articleDB.removeEventListener(listener)
    }
}