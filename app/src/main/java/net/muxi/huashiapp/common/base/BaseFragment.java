package net.muxi.huashiapp.common.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.RxBus;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.event.NetErrorEvent;
import net.muxi.huashiapp.event.RefreshSessionEvent;
import net.muxi.huashiapp.ui.login.LoginPresenter;


/**
 * Created by ybao on 16/4/19.
 */
public class BaseFragment extends Fragment{

    public static Fragment newInstance(){
        Fragment fragment = new Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retryObserver();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_empty,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void retryObserver(){
        RxBus.getDefault()
                .toObservable(RefreshSessionEvent.class)
                .subscribe(event->{
                    Log.d("received", "showToast: ");
                    User user = new User();
                    user.setSid(App.sUser.sid);
                    user.setPassword(App.sUser.password);
                    new LoginPresenter().loginRetry(user);
                },Throwable::printStackTrace,()->{});
        RxBus.getDefault()
                .toObservable(NetErrorEvent.class)
                .subscribe(netErrorEvent -> {
                },Throwable::printStackTrace);

    }
}
