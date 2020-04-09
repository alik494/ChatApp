package com.example.chatapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>  {

    private List<Message> messages;



    public MessageAdapter(){
        messages=new ArrayList<>();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,parent,false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
            holder.textViewAuthor.setText(messages.get(position).getAuthor());
            Picasso.get().load(messages.get(position).getMessage()).into(holder.imageViewSmallPoster);

            if (holder.imageViewSmallPoster.getDrawable()==null){
            holder.textViewMessage.setText(messages.get(position).getMessage());

            }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewAuthor;
        private TextView textViewMessage;
        private ImageView imageViewSmallPoster;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthor=itemView.findViewById(R.id.textViewAuthor);
            textViewMessage=itemView.findViewById(R.id.textViewMessage);
            imageViewSmallPoster=itemView.findViewById(R.id.imageViewSmallPoster);
        }
    }
}
