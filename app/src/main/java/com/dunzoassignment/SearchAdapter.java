package com.dunzoassignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context _context;
    private ArrayList<ItemModel> list;

    public SearchAdapter(Context context, ArrayList<ItemModel> mData){
        this._context = context;
        this.list = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_context).inflate(R.layout.item_model, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ExpandableRelativeLayout expandableRelativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            expandableRelativeLayout = itemView.findViewById(R.id.expandableLayout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            expandableRelativeLayout.toggle();
        }
    }
}
