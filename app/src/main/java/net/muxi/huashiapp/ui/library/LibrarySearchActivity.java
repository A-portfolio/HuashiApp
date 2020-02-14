package net.muxi.huashiapp.ui.library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.Nullable;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.muxistudio.appcommon.appbase.BaseAppActivity;
import com.muxistudio.appcommon.db.HuaShiDao;

import net.muxi.huashiapp.R;

import java.util.List;


/**
 * Created by ybao on 16/5/14.
 */
public class LibrarySearchActivity extends BaseAppActivity {

    private HuaShiDao dao;
    private List<String> list;
    private ArrayAdapter<String> mArrayList;
    private EditText mEtSearch;
    private ImageView mIvBack;
    private ImageView mIvClose;
    private TextView mTvClear;
    private ListView mLv;

    public static void start(Context context) {
        Intent starter = new Intent(context, LibrarySearchActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_search);
        mEtSearch = findViewById(R.id.et_search);
        mIvBack = findViewById(R.id.iv_back);
        mIvClose = findViewById(R.id.iv_close);
        mTvClear = findViewById(R.id.tv_clear);
        mLv = findViewById(R.id.lv);
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
        mEtSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            switch (i) {
                case EditorInfo.IME_ACTION_SEARCH:
                    String query = mEtSearch.getText().toString();
                    dao.insertSearchHistory(query);
                    LibrarySearchResultActivity.start(LibrarySearchActivity.this, query);
            }
            return false;
        });
        mIvBack.setOnClickListener(v -> {
            finish();
        });
        mIvClose.setOnClickListener(v -> mEtSearch.setText(""));
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
