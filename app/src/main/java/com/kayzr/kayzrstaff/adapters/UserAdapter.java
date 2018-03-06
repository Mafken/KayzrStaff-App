package com.kayzr.kayzrstaff.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kayzr.kayzrstaff.R;
import com.kayzr.kayzrstaff.domain.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private int itemCount;
    private List<User> users;
    private Context context;

    public UserAdapter(List<User> users, Context context ) {
        this.users = users;
        this.itemCount = users.size();
        this.context = context ;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_team_info, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, final int position) {
        TextView TIFullName = holder.TIFullName;
        TextView TIUsername = holder.TIUsername;
        TextView TINumber = holder.TINumber;
        Button callButton = holder.callButton;

        TIFullName.setText(users.get(position).getFullname());
        TIUsername.setText(users.get(position).getUsername());
        TINumber.setText(users.get(position).getGsm());

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(users.get(position).getGsm().length() > 9){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + users.get(position).getGsm()));
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context,"No phone number found!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.TIFullName)
        public TextView TIFullName;

        @BindView(R.id.TIUsername)
        public TextView TIUsername;

        @BindView(R.id.TINumber)
        public TextView TINumber;

        @BindView(R.id.callButton)
        public Button callButton;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}