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

//RecyclerView Onclick method, source: https://www.youtube.com/watch?v=ZXoGG2XTjzU

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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        //SET STUFFF
        final String type = items.get(position).getClass().getSimpleName().replace("Account", " account");
        ((Item) holder).dispType.setText(type);
        ((Item) holder).dispNumber.setText("Account number: " + items.get(position).getAccountNumber());
        ((Item) holder).dispBalance.setText("Balance: " + items.get(position).getBalance() + "€");
        if(type.equals("Savings account")){
            ((Item) holder).dispInterest.setText("Interest: " + ((SavingsAccount)(items.get(position))).getInterest() + "%");
        }

        ((Item) holder).parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Move to AccountActivity when clicked on item.
                Intent intent = new Intent(context, AccountSettingsActivity.class);
                intent.putExtra("account_number", items.get(position).getAccountNumber());
                intent.putExtra("account_type", type);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Item extends RecyclerView.ViewHolder {
        TextView dispType;
        TextView dispNumber;
        TextView dispBalance;
        TextView dispInterest;
        ConstraintLayout parentLayout;



        public Item(View itemView) {
            super(itemView);
            dispType = itemView.findViewById(R.id.displayAccountType);
            dispNumber = itemView.findViewById(R.id.displayAccountNumber);
            dispBalance = itemView.findViewById(R.id.displayAccountBalance);
            dispInterest = itemView.findViewById(R.id.displayAccountInterest);
            parentLayout = itemView.findViewById(R.id.accounts_parent_layout);
            
        }
    }
}