package com.dunzoassignment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;


public class PaginatonListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private Context context;

    private ArrayList<ItemModel> list;

    public PaginatonListAdapter(Context context, ArrayList<ItemModel> list){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_loading, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ItemModel model = list.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                ListVH listVH = (ListVH) holder;
                listVH.title.setText(model.getTitle());
                Glide.with(context).load(model.getThumbnail()).into(listVH.icon);

                break;
            case LOADING:
//                Do nothing
                break;
        }

    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_model, parent, false);
        viewHolder = new ListVH(v1);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
    }

    public class ListVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        ExpandableRelativeLayout expandableRelativeLayout;
        ImageView icon;
        TextView title;

        public ListVH(@NonNull View itemView) {
            super(itemView);
            expandableRelativeLayout = itemView.findViewById(R.id.expandableLayout);
            icon = itemView.findViewById(R.id.poster);
            title = itemView.findViewById(R.id.title);
            expandableRelativeLayout.collapse();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            expandableRelativeLayout.toggle();
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }
}
