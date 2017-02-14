package net.muxi.huashiapp.ui.library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.db.HuaShiDao;

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
        list = dao.loadSearchHistory();
        mArrayList = new ArrayAdapter<String>(this, R.layout.item_search_history, R.id.tv_search,
                list);
        mLv.setAdapter(mArrayList);
    }

    private void initListener() {
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                switch (i){
                    case EditorInfo.IME_ACTION_SEARCH:
                        String query = mEtSearch.getText().toString();
                        dao.insertSearchHistory(query);
                        LibrarySearchResultActivity.start(LibrarySearchActivity.this,query);
                }
                return false;
            }
        });
        mTvClear.setOnClickListener(v -> {
            dao.deleteAllHistory();
            list.clear();
            mArrayList.notifyDataSetChanged();
        });
        mLv.setOnItemClickListener((adapterView, view, i, l) -> {
            LibrarySearchResultActivity.start(LibrarySearchActivity.this,list.get(i));
        });
    }

}
