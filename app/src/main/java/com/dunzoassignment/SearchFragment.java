package com.dunzoassignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;

public class SearchFragment extends Fragment {

    public SearchFragment(){}

    private EditText search_view;
    private MaterialButton search_btn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.search_fragment, container, false);

        search_view = rootView.findViewById(R.id.query_et);
        search_btn = rootView.findViewById(R.id.search_btn);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(CommonUtils.keywords, search_view.getText().toString());

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment searchResults = new SearchResults();
                searchResults.setArguments(bundle);
                ft.replace(R.id.main_frame, searchResults);
                ft.commit();
                ft.addToBackStack(null);
            }
        });


        return rootView;
    }
}
