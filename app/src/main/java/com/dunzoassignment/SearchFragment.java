package com.dunzoassignment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

public class SearchFragment extends Fragment implements View.OnClickListener {

    public SearchFragment(){}

    private EditText search_view;
    private MaterialButton search_btn;
    private Bundle bundle;
    private Chip tr, latest, popular, upcoming;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.search_fragment, container, false);
        bundle = new Bundle();
        search_view = rootView.findViewById(R.id.query_et);
        search_btn = rootView.findViewById(R.id.search_btn);
        tr = rootView.findViewById(R.id.top_rated);
        latest = rootView.findViewById(R.id.latest);
        popular = rootView.findViewById(R.id.popular);
        upcoming = rootView.findViewById(R.id.upcoming);

        search_btn.setOnClickListener(this);
        tr.setOnClickListener(this);
        latest.setOnClickListener(this);
        popular.setOnClickListener(this);
        upcoming.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_rated: bundle.putString(CommonUtils.FILTER_TYPE, "top_rated"); SearchResult(bundle); break;
            case R.id.latest: bundle.putString(CommonUtils.FILTER_TYPE, "now_playing"); SearchResult(bundle); break;
            case R.id.popular: bundle.putString(CommonUtils.FILTER_TYPE, "popular"); SearchResult(bundle); break;
            case R.id.upcoming: bundle.putString(CommonUtils.FILTER_TYPE, "upcoming"); SearchResult(bundle); break;
            case R.id.search_btn:
                          if (!search_view.getText().toString().isEmpty()) {
                              bundle.putString(CommonUtils.FILTER_TYPE, "search");
                              bundle.putString(CommonUtils.KEYWORDS, search_view.getText().toString());
                              SearchResult(bundle);
                          } else {
                              search_view.setError("Search field can't be empty!");
                          }
                          break;
        }
    }

    private void SearchResult(Bundle bundle) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment searchResults = new SearchResults();
        searchResults.setArguments(bundle);
        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        ft.replace(R.id.main_frame, searchResults);
        ft.commit();
        ft.addToBackStack(null);
    }
}
