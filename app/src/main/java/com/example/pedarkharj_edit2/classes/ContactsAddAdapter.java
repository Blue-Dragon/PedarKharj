package com.example.pedarkharj_edit2.classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedarkharj_edit2.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsAddAdapter extends RecyclerView.Adapter<ContactsAddAdapter.ViewHolder>  {
    ArrayList<Contact> contacts;
    Context mContext;
    Class mClass;

    public ContactsAddAdapter(Context mContext, ArrayList<Contact> contacts) {
        this.mContext = mContext;
        this.contacts = contacts;
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView profImv;
        TextView nameTv;
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            profImv = itemView.findViewById(R.id.contact_add_img);
            nameTv = itemView.findViewById(R.id.contact_add_tv);
            relativeLayout = itemView.findViewById(R.id.rl_fuck);
        }

        @Override
        public void onClick(View view) {
        }
    }


    @NonNull
    @Override
    public ContactsAddAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_contact, parent, false);
        return new ContactsAddAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAddAdapter.ViewHolder holder, final int position) {
        Contact contact = contacts.get(position);

        holder.nameTv.setText(contact.getName());
        if (contact.getBitmap() !=null)
            holder.profImv.setImageBitmap(contact.getBitmap());

        holder.relativeLayout.setOnClickListener(item -> {
            Toast.makeText(mContext, (position +1) + " clicked", Toast.LENGTH_SHORT).show();

        });


    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}