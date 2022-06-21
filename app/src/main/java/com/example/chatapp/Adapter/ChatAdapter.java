package com.example.chatapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Models.Messages;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{

    ArrayList<Messages> mList;
    Context context;

    int SENDER_TYPE = 1;
    int RECEIVER_TYPE = 2;

    public ChatAdapter(ArrayList<Messages> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.writer_bubble, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.reader_bubble, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mList.get(position).getSenderId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_TYPE;
        }else{
            return RECEIVER_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages = mList.get(position);
        if (holder instanceof SenderViewHolder) {
            ((SenderViewHolder) holder).sMessage.setText(messages.getMessage());
        } else if (holder instanceof ReceiverViewHolder) {
            ((ReceiverViewHolder) holder).rMessage.setText(messages.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView rMessage, rTime;

        public ReceiverViewHolder(View itemView) {
            super(itemView);

            rMessage = itemView.findViewById(R.id.receiverMsg);
            rTime = itemView.findViewById(R.id.receiverTime);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView sMessage, sTime;

        public SenderViewHolder(View itemView) {
            super(itemView);

            sMessage = itemView.findViewById(R.id.senderMsg);
            sTime = itemView.findViewById(R.id.senderTime);
        }
    }
}
