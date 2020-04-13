package com.example.chatapp;

import android.content.Context;
import android.preference.PreferenceManager;
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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private static final int TYPE_MY_MESSAGE = 0;
    private static final int TYPE_OTHER_MESSAGE = 1;
    private List<Message> messages;
    private OnMessageListener onMessageListener;
    private Context context;

    interface  OnMessageListener{
        void onMessageClick(int position);
        void onMessageLongClick(int position);
    }

    public void setOnMessageListener(OnMessageListener onMessageListener) {
        this.onMessageListener = onMessageListener;
    }

    public MessageAdapter(Context context) {
        messages = new ArrayList<>();
        this.context = context;
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
        View view;
        if (viewType == TYPE_MY_MESSAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_my_message, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_other_message, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        String author = message.getAuthor();
        String textOfMessage = message.getMessage();
        String urlToImage = message.getImageURl();
        holder.textViewAuthor.setText(author);
        if (urlToImage == null || urlToImage.isEmpty()) {
            holder.imageViewSmallPoster.setVisibility(View.GONE);
        } else {
            holder.imageViewSmallPoster.setVisibility(View.VISIBLE);
        }
        if (textOfMessage != null && !textOfMessage.isEmpty()) {
            holder.textViewMessage.setText(textOfMessage);
        }

        if (urlToImage != null && !urlToImage.isEmpty()) {
            Picasso.get().load(urlToImage).into(holder.imageViewSmallPoster);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        String author = message.getAuthor();
        if (author != null && author.equals(PreferenceManager.getDefaultSharedPreferences(context).getString("author", "Anonim"))) {
            return TYPE_MY_MESSAGE;
        } else {
            return TYPE_OTHER_MESSAGE;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewAuthor;
        private TextView textViewMessage;
        private ImageView imageViewSmallPoster;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            imageViewSmallPoster = itemView.findViewById(R.id.imageViewSmallPoster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onMessageListener!=null){
                    //    onMessageListener.onMessageClick(messages.get(getAdapterPosition()).getTimestamp());
                    }
                }
            });
        }
    }
}
