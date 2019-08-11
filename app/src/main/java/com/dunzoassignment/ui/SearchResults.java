package com.dunzoassignment.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.dunzoassignment.models.ItemModel;
import com.dunzoassignment.utils.PaginationScrollListener;
import com.dunzoassignment.adaters.PaginatonListAdapter;
import com.dunzoassignment.R;
import com.dunzoassignment.utils.CommonUtils;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchResults extends Fragment {

    private RecyclerView recyclerView;
    private PaginatonListAdapter adapter;
    private ArrayList<ItemModel> list = new ArrayList<>();
    private String keyword, type = "";
    private ImageView no_result_img;
    private TextView showing_results;
    private MaterialToolbar toolbar;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 0;
    private int currentPage = PAGE_START;
    private int total_results = 0;
    private ProgressDialog pd;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list.clear();
        handleBundleArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.search_results, container, false);
        recyclerView = rootView.findViewById(R.id.rv);
        no_result_img = rootView.findViewById(R.id.no_result_image);
        showing_results = rootView.findViewById(R.id.showing_results);
        _SetupToolbar(rootView);

        showProcessDialog();

        adapter = new PaginatonListAdapter(getContext(), list);

        _SetRecyclerScroll(rootView, recyclerView, adapter);

        return rootView;
    }

    private void _SetRecyclerScroll(View rootView, RecyclerView recyclerView, final PaginatonListAdapter adapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                currentPage += 1;
                if (type != "") {
                    if (type != "search") FilterMovies(type);
                    else FetchMovies(keyword);
                }
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                adapter.addLoadingFooter();
                return isLoading;
            }

            @Override
            public int getCurrentPage() {
                return currentPage;
            }
        });
    }

    private void handleBundleArguments() {
        type = getArguments().getString(CommonUtils.FILTER_TYPE);
        if (type.equals(CommonUtils.TOP_RATED)) FilterMovies(CommonUtils.TOP_RATED);
        else if (type.equals(CommonUtils.NOW_PLAYING)) FilterMovies(CommonUtils.NOW_PLAYING);
        else if (type.equals(CommonUtils.POPULAR)) FilterMovies(CommonUtils.POPULAR);
        else if (type.equals(CommonUtils.UPCOMING)) FilterMovies(CommonUtils.UPCOMING);
        else {
            keyword = getArguments().getString(CommonUtils.KEYWORDS);
            FetchMovies(keyword);
        }
    }

    private void _SetupToolbar(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

    }

    public void FetchMovies(final String query) {

        AndroidNetworking.get(CommonUtils.SEARCH_URL)
                .addPathParameter(CommonUtils.QUERY, query)
                .addPathParameter(CommonUtils.PAGE_NO, String.valueOf(currentPage))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            hideProcessDialog();
                            total_results = response.getInt("total_results");

                            if (total_results == 0)
                                no_result_img.setVisibility(View.VISIBLE);
                            else {
                                showing_results.setVisibility(View.VISIBLE);
                                showing_results.setText("Showing " + total_results + " results for \"" + query + "\"");
                                TOTAL_PAGES = response.getInt("total_pages");
                                JSONArray results = response.getJSONArray("results");
                                for (int i = 0; i < results.length(); i++) {

                                    JSONObject object = results.getJSONObject(i);
                                    String title = object.getString("title");
                                    String overview = object.getString("overview");
                                    String release_date = object.getString("release_date");
                                    String thumbnail = object.getString("poster_path");
                                    float rating = Float.parseFloat(String.valueOf(object.getDouble("vote_average"))) / 2;

                                    ItemModel itemModel = new ItemModel();
                                    itemModel.setTitle(title);
                                    itemModel.setThumbnail(thumbnail);
                                    itemModel.setRelease_date(release_date);
                                    itemModel.setOverview(overview);
                                    itemModel.setRating(rating);
                                    list.add(itemModel);
                                    adapter.notifyDataSetChanged();
                                }

                                if (currentPage == TOTAL_PAGES) adapter.removeLoadingFooter();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    public void FilterMovies(final String type) {

        AndroidNetworking.get(CommonUtils.FILTER_URL)
                .addPathParameter(CommonUtils.FILTER_TYPE, type)
                .addPathParameter(CommonUtils.PAGE_NO, String.valueOf(currentPage))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            hideProcessDialog();
                            total_results = response.getInt("total_results");

                            if (total_results == 0)
                                no_result_img.setVisibility(View.VISIBLE);
                            else {
                                showing_results.setVisibility(View.VISIBLE);
                                showing_results.setText("Showing " + total_results + " results for \"" + type.toUpperCase().replace("_", " ") + "\"");
                                TOTAL_PAGES = response.getInt("total_pages");
                                JSONArray results = response.getJSONArray("results");
                                for (int i = 0; i < results.length(); i++) {

                                    JSONObject object = results.getJSONObject(i);
                                    String title = object.getString("title");
                                    String overview = object.getString("overview");
                                    String release_date = object.getString("release_date");
                                    String thumbnail = object.getString("poster_path");
                                    float rating = Float.parseFloat(String.valueOf(object.getDouble("vote_average"))) / 2;

                                    ItemModel itemModel = new ItemModel();
                                    itemModel.setTitle(title);
                                    itemModel.setThumbnail(thumbnail);
                                    itemModel.setRelease_date(release_date);
                                    itemModel.setOverview(overview);
                                    itemModel.setRating(rating);
                                    list.add(itemModel);
                                    adapter.notifyDataSetChanged();
                                }

                                if (currentPage == TOTAL_PAGES) adapter.removeLoadingFooter();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    private void showProcessDialog() {
        pd = new ProgressDialog(getContext());
        pd.setMessage("Searching...");
        pd.setCancelable(true);
        pd.show();
    }

    private void hideProcessDialog() {
        if (pd.isShowing()) {
            pd.dismiss();
        }
        pd.dismiss();
    }

}
