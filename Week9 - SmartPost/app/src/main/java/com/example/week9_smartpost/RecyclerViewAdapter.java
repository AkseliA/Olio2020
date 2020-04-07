/* Author: Akseli Aula
 * Environment: Android Studio
 * Assignment week 9*/

package com.example.week9_smartpost;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerViewAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<SmartPost> items;

    public RecyclerViewAdapter(Context context, ArrayList<SmartPost> arrayList){
        this.context = context;
        this.items = arrayList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.recycler_view_row, parent, false);
        Item item = new Item(row);
        return item;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String availability;
        //Skips the "All" row
        position++;
        ((Item)holder).tvName.setText(items.get(position).getName());
        ((Item)holder).tvAddress.setText(items.get(position).getAddress() + ", " + items.get(position).getCity() + ", " + items.get(position).getPostalcode());
        //Edit availability dates from numbers to weekdays.
        availability = items.get(position).getAvailability();
        ((Item)holder).tvAvailability.setText(availability);
    }

    @Override
    public int getItemCount() {
        //skips "All" row so size is one smaller
        return items.size() - 1;
    }
    public class Item extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvAddress;
        TextView tvAvailability;
        public Item(View itemView){
            super(itemView);
            tvName = itemView.findViewById(R.id.displayName);
            tvAddress = itemView.findViewById(R.id.displayAddress);
            tvAvailability = itemView.findViewById(R.id.displayAvailability);
        }
    }
}
