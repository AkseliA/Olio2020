package com.example.olio_ht;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


public class CardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Card> items;


    public CardRecyclerViewAdapter(Context context, ArrayList<Card> arrayList) {
        this.context = context;
        this.items = arrayList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.cards_rview_row, parent, false);
        return new Item(row);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        //SET STUFFF
        ((Item) holder).dispType.setText(items.get(position).getCardType());
        ((Item) holder).dispCard.setText(items.get(position).getCardNumber());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class Item extends RecyclerView.ViewHolder {
        TextView dispCard;
        TextView dispType;

        public Item(View itemView) {
            super(itemView);
            dispCard = itemView.findViewById(R.id.displayCard_Number);
            dispType = itemView.findViewById(R.id.displayCard_Type);
        }
    }
}