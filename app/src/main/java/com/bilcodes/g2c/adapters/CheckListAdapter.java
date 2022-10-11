package com.bilcodes.g2c.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bilcodes.g2c.BookingDetailsActivity;
import com.bilcodes.g2c.R;
import com.bilcodes.g2c.models.CheckListModel;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ListViewHolder> {

    Context context;
    ArrayList<CheckListModel> checkList;
    DatabaseReference databaseReference;
    ArrayList<String> keys;
    TextView emptyText;

    public CheckListAdapter(Context context, ArrayList<CheckListModel> checkList, DatabaseReference databaseReference, ArrayList<String> keys, TextView emptyText) {
        this.context = context;
        this.checkList = checkList;
        this.databaseReference = databaseReference;
        this.keys = keys;
        this.emptyText = emptyText;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.check_list_item,parent,false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, @SuppressLint("RecyclerView") int position) {

        CheckListModel checkItem = checkList.get(position);
        holder.checkItemName.setText(checkItem.getTask());

        Dialog dialog = new Dialog(context);
        holder.layout.setOnClickListener(v -> {
            Intent page2 = new Intent(context, BookingDetailsActivity.class);
            context.startActivity(page2);
        });
        holder.layout.setOnLongClickListener(v -> {
            dialog.setContentView(R.layout.delete_item_dialog);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);

            dialog.show();

            Button deleteButton = dialog.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(v1 -> {
                databaseReference.child(keys.get(position)).removeValue().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        keys.remove(keys.get(position));
                        checkList.remove(checkList.get(position));
                        if(checkList.isEmpty()){
                            emptyText.setVisibility(View.VISIBLE);
                        }
                        notifyDataSetChanged();

                    }
                    dialog.dismiss();
                });
//                System.out.println(checkList.get(position).getTask());
            });
            return false;
        });




    }

    @Override
    public int getItemCount() {
        return checkList.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder{

        TextView checkItemName;
        ConstraintLayout layout;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            checkItemName = itemView.findViewById(R.id.checkItem);
            layout = itemView.findViewById(R.id.item);





        }


    }




}
