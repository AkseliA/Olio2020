package com.example.olio_ht;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//Source: https://www.youtube.com/watch?v=AtzJUDvRktI
public class ItemSpacingDecorator extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public ItemSpacingDecorator(int verticalSpaceHeight){
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = verticalSpaceHeight;
    }
}