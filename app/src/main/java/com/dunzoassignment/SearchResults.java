package com.dunzoassignment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.dunzoassignment.CommonUtils.TAG;

public class SearchResults extends Fragment {

    private RecyclerView recyclerView;
    private PaginatonListAdapter adapter;
    private ArrayList<ItemModel> list = new ArrayList<>();
    private String query;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 0;
    private int currentPage = PAGE_START;
    private ProgressDialog pd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list.clear();
        query = getArguments().getString(CommonUtils.keywords);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.search_results, container, false);
        recyclerView = rootView.findViewById(R.id.rv);

        adapter = new PaginatonListAdapter(getContext(), list);
        showProcessDialog();
        LoadInitialResponse();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                currentPage +=1;

                LoadInitialResponse();
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

        return rootView;
    }

    public void LoadInitialResponse() {
            AndroidNetworking.get("https://api.themoviedb.org/3/search/movie?api_key={api_key}&language=en-US&query={keyword}&page={page_no}")
                    .addPathParameter("api_key", BuildConfig.API_KEY)
                    .addPathParameter("keyword", query).addPathParameter("page_no", String.valueOf(currentPage))
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                hideProcessDialog();
                                int total_results = response.getInt("total_results");

                                if (total_results == 0)
                                    Toast.makeText(getContext(), "No results found", Toast.LENGTH_SHORT).show();
                                TOTAL_PAGES = response.getInt("total_pages");
                                JSONArray results = response.getJSONArray("results");
                                for (int i=0; i<results.length(); i++) {

                                    JSONObject object = results.getJSONObject(i);
                                    String title = object.getString("title");
                                    String overview = object.getString("overview");
                                    String release_date = object.getString("release_date");
                                    String thumbnail = object.getString("poster_path");

                                    ItemModel itemModel = new ItemModel();
                                    itemModel.setTitle(title); itemModel.setThumbnail(thumbnail); itemModel.setRelease_date(release_date); itemModel.setOverview(overview);
                                    list.add(itemModel);
                                    adapter.notifyDataSetChanged();
                                }

                                if (currentPage == TOTAL_PAGES) adapter.removeLoadingFooter();

                            } catch (Exception e){
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
        pd.setTitle("Searching...");
        pd.setCancelable(false);
        pd.show();
    }

    private void hideProcessDialog() {
        if (pd.isShowing()) {
            pd.dismiss();
        }
        pd.dismiss();
    }

}
