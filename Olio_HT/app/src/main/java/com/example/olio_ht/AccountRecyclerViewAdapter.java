package com.example.olio_ht;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AccountRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Account> items;

    public AccountRecyclerViewAdapter(Context context, ArrayList<Account> arrayList) {
        this.context = context;
        this.items = arrayList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.accounts_rview_row, parent, false);
        Item item = new Item(row);
        return item;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //SET STUFFF
        String type = items.get(position).getClass().getSimpleName().replace("Account", " Account");
        ((Item) holder).dispType.setText(type);
        ((Item) holder).dispNumber.setText(items.get(position).getAccountNumber());


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Item extends RecyclerView.ViewHolder {
        TextView dispType;
        TextView dispNumber;
        TextView dispBalance;

        public Item(View itemView) {
            super(itemView);
            dispType = itemView.findViewById(R.id.displayAccountType);
            dispNumber = itemView.findViewById(R.id.displayAccountNumber);
            dispBalance = itemView.findViewById(R.id.displayAccountBalance);
        }
    }
}