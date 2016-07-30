package net.muxi.huashiapp.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.AppConstants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.data.VerifyResponse;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.Base64Util;
import net.muxi.huashiapp.common.util.NetStatus;
import net.muxi.huashiapp.common.util.ToastUtil;
import net.muxi.huashiapp.common.widget.LoginEditText;
import net.muxi.huashiapp.login.SimpleTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/5/15.
 */
public class LibraryLoginActivity extends ToolbarActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_user_name)
    LoginEditText mEditUserName;
    @BindView(R.id.edit_password)
    LoginEditText mEditPassword;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.search_view)
    MySearchView mSearchView;
    @BindView(R.id.accout_layout)
    LinearLayout mAccoutLayout;

    private String[] mSuggestions;
    private HuaShiDao dao;

    private TextWatcher mTextWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            updateBtn();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_login);
        ButterKnife.bind(this);
        dao = new HuaShiDao();
        mSuggestions = dao.loadSearchHistory().toArray(new String[0]);
        initView();
    }

    private void initView() {
        mEditPassword.setHint(getResources().getString(R.string.tip_lib_pwd));
        mToolbar.setTitle("图书馆");
        mEditUserName.addTextChangedListener(mTextWatcher);
        mEditPassword.addTextChangedListener(mTextWatcher);
        mSearchView.setSuggestions(mSuggestions);
        mSearchView.setOnQueryTextListener(new MySearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryText) {
                Intent intent = new Intent(LibraryLoginActivity.this, LibraryActivity.class);
                intent.putExtra(AppConstants.LIBRARY_QUERY_TEXT, queryText);
                mSearchView.closeSearchView();
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setOnSearchViewListener(new MySearchView.OnSearchViewListener() {
            @Override
            public void onSearchShown() {

            }

            @Override
            public void onSeachClose() {
                mAccoutLayout.setClickable(true);
//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        });
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.showSearchView();
                mAccoutLayout.setClickable(false);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            }
        });
        mEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        });
        mEditUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void updateBtn() {
        if (mEditUserName.length() != 0 && mEditPassword.length() != 0) {
            mBtnLogin.setEnabled(true);
        } else {
            mBtnLogin.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearchView();
            return;
        }
        super.onBackPressed();
    }

    @OnClick(R.id.btn_login)
    public void onClick() {
        final User libUser = new User();
        libUser.setSid(mEditUserName.getText().toString());
        libUser.setPassword(mEditPassword.getText().toString());
        if (NetStatus.isConnected()) {
            CampusFactory.getRetrofitService().libLogin(Base64Util.createBaseStr(libUser))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<VerifyResponse>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Response<VerifyResponse> verifyResponseResponse) {
                            if (verifyResponseResponse.code() == 200) {
                                App.saveLibUser(libUser);
                                Intent intent = new Intent(LibraryLoginActivity.this, MineActivity.class);
                                startActivity(intent);
                            } else if (verifyResponseResponse.code() == 403) {
                                ToastUtil.showLong(getResources().getString(R.string.tip_err_account));
                            } else {
                                ToastUtil.showLong(getResources().getString(R.string.tip_err_server));
                            }
                        }
                    });
        }
    }


}
