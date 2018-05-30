package net.muxi.huashiapp.ui.more;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muxistudio.appcommon.appbase.BaseAppActivity;
import com.muxistudio.appcommon.appbase.BaseAppFragment;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.common.util.Logger;
import com.muxistudio.common.util.PreferenceUtil;
import com.muxistudio.common.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.BuildConfig;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.service.DownloadService;
import net.muxi.huashiapp.ui.AboutActivity;
import net.muxi.huashiapp.ui.SettingActivity;
import net.muxi.huashiapp.ui.SuggestionActivity;
import net.muxi.huashiapp.ui.webview.WebViewActivity;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 17/2/16.
 */

public class MoreFragment extends BaseAppFragment {

    private MoreAdapter mAdapter;

    private String downloadUrl;

    private PreferenceUtil sp;

    private Integer[] colors = {R.color.blue,R.color.green,R.color.yellow
            ,R.color.grey,R.color.color_light_green,R.color.red,R.color.red};

    private String[] titles = {"常见问题Q&A", "分享App给好友", "通知栏提醒", "意见反馈", "检查更新 ", "关于", "退出账号"};
    private Integer[] icons =
            {R.drawable.ic_more_qa, R.drawable.ic_more_share, R.drawable.ic_more_notice,
                    R.drawable.ic_more_feedback,
                    R.drawable.ic_more_update, R.drawable.ic_more_about,
                    R.drawable.ic_more_sign_out};
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;


    public static MoreFragment newInstance() {
        MoreFragment fragment = new MoreFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        mToolbar = view.findViewById(R.id.toolbar);
        mRecyclerView = view.findViewById(R.id.recycler_view);

        sp = new PreferenceUtil();

        mToolbar.setTitle("更多");
        initView();
        return view;
    }

    public void initView() {
        mAdapter = new MoreAdapter((List<String>) Arrays.asList(titles), Arrays.asList(icons),Arrays.asList(colors));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setItemClickListener((view, position) -> {
            switch (position) {
                case 0:
                    Intent intent = WebViewActivity.newIntent(getContext(),
                            "https://ccnubox.muxixyz.com/qa/");
                    startActivity(intent);
                    break;
                case 1:
                    ShareDialog shareDialog = ShareDialog.newInstance(0);
                    shareDialog.show(getFragmentManager(), "dialog_share");
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
        });

    }

    private void checkUpdates() {
        CampusFactory.getRetrofitService().getLatestVersion()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(versionData -> {
                    if (versionData == null){
                        ((BaseAppActivity)getActivity()).showSnackbarShort(R.string.title_not_have_to_update);
                        return;
                    }
                    if (!versionData.getVersion().equals(BuildConfig.VERSION_NAME)) {
                        final CheckUpdateDialog checkUpdateDialog = new CheckUpdateDialog();
                        checkUpdateDialog.setTitle(App.sContext.getString(R.string.title_update)
                                + versionData.getVersion());
                        checkUpdateDialog.setContent(
                                App.sContext.getString(R.string.tip_update_intro)
                                        + versionData.getIntro() + "\n" +
                                        App.sContext.getString(R.string.tip_update_size)
                                        + versionData.getSize());
                        checkUpdateDialog.setOnPositiveButton(
                                App.sContext.getString(R.string.btn_update),
                                () -> {
                                    if (isStoragePermissionGranted()) {
                                        beginUpdate(versionData.download);
                                    } else {
                                        ((BaseAppActivity) getActivity()).showErrorSnackbarShort(R.string.tip_require_write_permission);
                                    }
                                    checkUpdateDialog.dismiss();
                                });
                        checkUpdateDialog.setOnNegativeButton(
                                App.sContext.getString(R.string.btn_cancel),
                                () -> checkUpdateDialog.dismiss());

                        checkUpdateDialog.show(getFragmentManager(), "dialog_update");
                    } else {
                        ((BaseAppActivity) getActivity()).showSnackbarShort(
                                R.string.title_not_have_to_update);
                    }
                }, throwable -> throwable.printStackTrace());
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
        // 信息门户和图书馆登陆合并，改为共同退出登录，取消LogoutDialog
        if (TextUtils.isEmpty(UserAccountManager.getInstance().getInfoUser().getSid())) {
            ((BaseAppActivity) getActivity()).showErrorSnackbarShort(
                    App.sContext.getString(R.string.not_log_in));
        } else {
            UserAccountManager.getInstance().logoutInfoUser();
            UserAccountManager.getInstance().logoutLibUser();
            MobclickAgent.onProfileSignOff();
            ((BaseAppActivity) getActivity()).showSnackbarShort(
                    App.sContext.getString(R.string.tip_all_log_out));
        }


    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

}
