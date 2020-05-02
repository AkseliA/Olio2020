package com.example.olio_ht;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class TransactionRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Transaction> items;


    public TransactionRecyclerViewAdapter(Context context, ArrayList<Transaction> arrayList) {
        this.context = context;
        this.items = arrayList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.transactions_rview_row, parent, false);
        Item item = new Item(row);
        return item;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        //SET STUFFF
        ((Item) holder).dispTrans.setText(items.get(position).getAction());
        ((Item) holder).dispDate.setText("Date: "+items.get(position).getDate());
        if(!items.get(position).getAmount().isEmpty()){
            ((Item) holder).dispAmount.setText("Amount: "+items.get(position).getAmount() + "€");
        }
        if(!items.get(position).getBalance().isEmpty()){
            ((Item) holder).dispBalance.setText("Balance: "+items.get(position).getBalance() + "€");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Item extends RecyclerView.ViewHolder {
        TextView dispTrans;
        TextView dispDate;
        TextView dispAmount;
        TextView dispBalance;


        public Item(View itemView) {
            super(itemView);
            dispTrans = itemView.findViewById(R.id.displayTransaction);
            dispDate = itemView.findViewById(R.id.displayDate);
            dispAmount = itemView.findViewById(R.id.displayAmount);
            dispBalance = itemView.findViewById(R.id.displayBalance);



        }
    }
}