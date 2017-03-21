package net.muxi.huashiapp.ui.more;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.BuildConfig;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.base.BaseFragment;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.service.DownloadService;
import net.muxi.huashiapp.ui.AboutActivity;
import net.muxi.huashiapp.ui.SettingActivity;
import net.muxi.huashiapp.ui.SuggestionActivity;
import net.muxi.huashiapp.ui.webview.WebViewActivity;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.ToastUtil;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 17/2/16.
 */

public class MoreFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private MoreAdapter mAdapter;

    private String downloadUrl;


    private String[] titles = {"常见问题Q&A", "分享App给好友", "通知栏提醒", "意见反馈", "检查更新 ", "关于", "退出账号"};
    private Integer[] icons =
            {R.drawable.ic_more_qa, R.drawable.ic_more_share, R.drawable.ic_more_notice,
                    R.drawable.ic_more_feedback,
                    R.drawable.ic_more_update, R.drawable.ic_more_about,
                    R.drawable.ic_more_sign_out};


    public static MoreFragment newInstance() {
        MoreFragment fragment = new MoreFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);

        mToolbar.setTitle("更多");
        initView();
        return view;
    }

    public void initView() {
        mAdapter = new MoreAdapter((List<String>) Arrays.asList(titles), Arrays.asList(icons));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setItemClickListener(new MoreAdapter.ItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        Intent intent = WebViewActivity.newIntent(getContext(),"https://ccnubox.muxixyz.com/qa/");
                        startActivity(intent);
                        break;
                    case 1:
                        ShareDialog shareDialog = new ShareDialog();
                        shareDialog.show(getFragmentManager(),"dialog_share");
                        break;
                    case 2:
                        SettingActivity.start(getContext());
                        break;
                    case 3:
                        SuggestionActivity.start(getContext());
                        break;
                    case 4:
                        checkUpdates();
                        break;
                    case 5:
                        AboutActivity.start(getContext());
                        break;
                    case 6:
                        logout();
                        break;

                }
            }
        });
    }

    private void checkUpdates() {
        CampusFactory.getRetrofitService().getLatestVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(versionData -> {
                    if (versionData.getVersion() != null &&
                            !BuildConfig.VERSION_NAME.equals(versionData.version)) {
                        CheckUpdateDialog checkUpdateDialog = new CheckUpdateDialog();
                        checkUpdateDialog.setTitle(App.sContext.getString(R.string.title_update)
                                + versionData.getVersion());
                        checkUpdateDialog.setContent(
                                App.sContext.getString(R.string.tip_update_intro)
                                        + versionData.getIntro() + "/n" +
                                        App.sContext.getString(R.string.tip_update_size)
                                        + versionData.getSize());
                        checkUpdateDialog.setOnPositiveButton(
                                App.sContext.getString(R.string.btn_update),
                                () -> {
                                    beginUpdate(versionData.download);
                                    checkUpdateDialog.dismiss();
                                });
                        checkUpdateDialog.setOnNegativeButton(
                                App.sContext.getString(R.string.btn_cancel),
                                () -> checkUpdateDialog.dismiss());
                        checkUpdateDialog.show(getFragmentManager(), "dialog_update");
                    } else {
                        ((BaseActivity) getActivity()).showSnackbarShort(
                                R.string.title_not_have_to_update);
                    }
                },throwable -> throwable.printStackTrace());
    }

    private void beginUpdate(String download) {
        deleteApkBefore();
        Intent intent = new Intent(getContext(), DownloadService.class);
        intent.putExtra("url", download);
        intent.putExtra("fileType", "apk");
        intent.putExtra("fileName", "ccnubox.apk");
        getActivity().startService(intent);
        Logger.d("download");
        ToastUtil.showShort(getString(R.string.tip_start_download_apk));
    }

    private void deleteApkBefore() {
        String path = Environment.getExternalStorageDirectory() + "/Download/" + "ccnubox.apk";
        File file = new File(path);
        if (file.exists()) {
            file.delete();
            Logger.d("apk file delete");
        } else {
            Logger.d("file not exists");
        }
    }

    private void logout() {
        LogoutDialog logoutDialog = new LogoutDialog();
        logoutDialog.setBtnIdLogout(new LogoutDialog.OnIdClickListener() {
            @Override
            public void OnIdClick() {
                App.logoutUser();
                logoutDialog.dismiss();
                ((BaseActivity) getActivity()).showSnackbarShort(App.sContext.getString(R.string.tip_id_log_out));
            }
        });

        logoutDialog.setBtnLibraryLogout(new LogoutDialog.OnLibraryClickListener() {
            @Override
            public void OnLibraryClick() {
                App.logoutLibUser();
                logoutDialog.dismiss();
                ((BaseActivity) getActivity()).showSnackbarShort(App.sContext.getString(R.string.tip_library_log_out));
            }
        });

        logoutDialog.setBtnAllLogout(new LogoutDialog.OnAllClickListener() {
            @Override
            public void OnAllClick() {
                App.logoutUser();
                App.logoutLibUser();
                logoutDialog.dismiss();
                ((BaseActivity) getActivity()).showSnackbarShort(App.sContext.getString(R.string.tip_all_log_out));
            }
        });
        logoutDialog.show(getFragmentManager(), "dialog_logout");
    }

}
