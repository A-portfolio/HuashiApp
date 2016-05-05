package net.muxi.huashiapp.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.muxi.huashiapp.R;

/**
 * Created by ybao on 16/5/2.
 */
public class LibraryDetailFragment extends Fragment{

    public static Fragment newInstance(){
        LibraryDetailFragment libraryDetailFragment = new LibraryDetailFragment();
        return libraryDetailFragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail,container,false);

        return view;
    }



}
