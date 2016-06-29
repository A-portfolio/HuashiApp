package net.muxi.huashiapp.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;

/**
 * Created by ybao on 16/5/2.
 */
public class LibraryListFragment extends Fragment {


    //    @Bind(R.id.fragment_recyclerview)
    RecyclerView mFragmentRecyclerview;
    private LinearLayout ll;
    private LibraryAdapter mLibraryAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] s;

    public static Fragment newInstance() {
        LibraryListFragment fragment = new LibraryListFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ll = (LinearLayout) view.findViewById(R.id.ll);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag","layout has been click");
            }
        });
        initRecyclerView(view);

//        ButterKnife.bind(this, view);
        return view;
    }

    private void initRecyclerView(View view) {
        mFragmentRecyclerview = (RecyclerView) view.findViewById(R.id.fragment_recyclerview);
        s = new String[7];
        for (int i = 0; i < 7; i++) {
            s[i] = "it is the " + i;
        }
//        mLibraryAdapter = new LibraryAdapter(s);
//        mFragmentRecyclerview.setHasFixedSize(true);
        mFragmentRecyclerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("listfragment","recyclerview");
            }
        });
        mLayoutManager = new LinearLayoutManager(App.getContext());
        mFragmentRecyclerview.setLayoutManager(mLayoutManager);
//        mLibraryAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.frame_layout,LibraryDetailFragment.newInstance());
//                ft.addToBackStack(null);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                ft.commit();
//
//            }
//        });
        mFragmentRecyclerview.setAdapter(mLibraryAdapter);
        mFragmentRecyclerview.setItemAnimator(new DefaultItemAnimator());


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
