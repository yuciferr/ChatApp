package com.example.chatapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Models.Messages;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter{

    ArrayList<Messages> mList;
    Context context;
    String recId;

    int SENDER_TYPE = 1;
    int RECEIVER_TYPE = 2;

    public ChatAdapter(ArrayList<Messages> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }
    public ChatAdapter(ArrayList<Messages> mList, Context context, String recId) {
        this.mList = mList;
        this.context = context;
        this.recId = recId;
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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                new AlertDialog.Builder(context).setTitle("Delete").setMessage("Are you sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        String sRoom = FirebaseAuth.getInstance().getUid() + recId;
                        database.getReference().child("Chats").child(sRoom).child(messages.getMessageId()).setValue(null);


                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();

                return false;
            }
        });

        if (holder.getClass() == SenderViewHolder.class) {
            ((SenderViewHolder) holder).sMessage.setText(messages.getMessage());

            Date date = new Date(messages.getTimeStamp());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
            String strDate= simpleDateFormat.format(date);
            ((SenderViewHolder) holder).sTime.setText(strDate.toString());

        } else if (holder.getClass() == ReceiverViewHolder.class) {
            ((ReceiverViewHolder) holder).rMessage.setText(messages.getMessage());

            Date date = new Date(messages.getTimeStamp());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
            String strDate= simpleDateFormat.format(date);
            ((ReceiverViewHolder) holder).rTime.setText(strDate.toString());
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
