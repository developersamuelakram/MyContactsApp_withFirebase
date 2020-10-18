package com.example.mycontacts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mycontacts.Model.Contacts;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Contactholder> {


    Context context;
    List<Contacts> contactsList;

    public ContactAdapter(Context context, List<Contacts> contactsList) {
        this.context = context;
        this.contactsList = contactsList;
    }


    @NonNull
    @Override
    public Contactholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.chatitem, parent, false);
        return new Contactholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Contactholder holder, int position) {


        final Contacts contacts = contactsList.get(position);

        holder.contactname.setText(contacts.getContactname());

        Glide.with(context).load(contacts.getImageURL()).into(holder.imageView);


        holder.callingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contacts.getContactnumber()));
                context.startActivity(intent);
            }
        });



        holder.emailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("mailto:" + contacts.getContactemail()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    class Contactholder extends RecyclerView.ViewHolder{


        TextView contactname;
        CircleImageView imageView;
        Button callingbtn, emailbtn;


        public Contactholder(@NonNull View itemView) {
            super(itemView);


            contactname = itemView.findViewById(R.id.contactName);
            imageView = itemView.findViewById(R.id.imageOfContact);

            callingbtn = itemView.findViewById(R.id.callintent);

            emailbtn = itemView.findViewById(R.id.emailIntent);

        }
    }
}
