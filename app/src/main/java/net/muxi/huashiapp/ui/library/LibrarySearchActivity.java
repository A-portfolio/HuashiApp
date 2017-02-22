package net.muxi.huashiapp.ui.library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.util.DimensUtil;
import net.muxi.huashiapp.util.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/5/14.
 */
public class LibrarySearchActivity extends BaseActivity {

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.tv_clear)
    TextView mTvClear;
    @BindView(R.id.lv)
    ListView mLv;

    private HuaShiDao dao;
    private List<String> list;
    private ArrayAdapter<String> mArrayList;

    public static void start(Context context) {
        Intent starter = new Intent(context, LibrarySearchActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_search);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initView() {
        dao = new HuaShiDao();
        list = dao.loadSearchHistory();
        if (list.size() > 5) {
            list = list.subList(0, 5);
        }
        mArrayList = new ArrayAdapter<String>(this, R.layout.item_search_history, R.id.tv_book,
                list);
        mLv.setAdapter(mArrayList);
    }

    private void initListener() {
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        String query = mEtSearch.getText().toString();
                        dao.insertSearchHistory(query);
                        LibrarySearchResultActivity.start(LibrarySearchActivity.this, query);
                }
                return false;
            }
        });
        mEtSearch.setOnTouchListener(((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                float x = motionEvent.getRawX();
                float y = motionEvent.getRawY();
                Logger.d(DimensUtil.dp2px(48) + "");
                if (x < DimensUtil.dp2px(48) && x > DimensUtil.dp2px(24) && y < DimensUtil.dp2px(44)
                        + DimensUtil.getStatusBarHeight()
                        && y > DimensUtil.dp2px(20) + DimensUtil.getStatusBarHeight()) {
                    finish();
                } else if (x < DimensUtil.getScreenWidth() - DimensUtil.dp2px(24)
                        && x > DimensUtil.getScreenWidth() - DimensUtil.dp2px(48)
                        && y < DimensUtil.dp2px(44) + DimensUtil.getStatusBarHeight()
                        && y > DimensUtil.dp2px(20) + DimensUtil.getStatusBarHeight()) {
                    mEtSearch.setText("");
                }
            }
            return true;
        }));
        mTvClear.setOnClickListener(v -> {
            dao.deleteAllHistory();
            list.clear();
            mArrayList.notifyDataSetChanged();
        });
        mLv.setOnItemClickListener((adapterView, view, i, l) -> {
            LibrarySearchResultActivity.start(LibrarySearchActivity.this, list.get(i));
        });
    }

}
