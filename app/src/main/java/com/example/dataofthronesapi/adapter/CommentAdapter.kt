package com.example.dataofthronesapi.adapter

import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dataofthronesapi.R
import com.example.dataofthronesapi.data.Comment
import com.example.dataofthronesapi.databinding.CommentItemBinding


class CommentAdapter(
    private val commentList: List<Comment>
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = CommentItemBinding.bind(view)

        fun bind(comment: Comment) {
            binding.tvUser.text = comment.userName
            binding.tvComment.text = comment.text
            binding.tvComment.setMovementMethod(ScrollingMovementMethod())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CommentViewHolder(layoutInflater.inflate(R.layout.comment_item, parent, false))
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(commentList[position])
    }

    override fun getItemCount(): Int {
        return commentList.size
    }
}