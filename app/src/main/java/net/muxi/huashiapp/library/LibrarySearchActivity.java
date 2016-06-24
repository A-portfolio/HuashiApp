package net.muxi.huashiapp.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import net.muxi.huashiapp.AppConstants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ybao on 16/5/14.
 */
public class LibrarySearchActivity extends ToolbarActivity {

    @Bind(R.id.btn_search)
    Button mBtnSearch;
    @Bind(R.id.edit_search_view)
    EditText mEditSearchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_search);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setTitle("图书馆");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_login:
                Intent intent = new Intent(this, MineActivity.class);
                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    private String getQueryText() {
        return mEditSearchView.getText().toString();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.btn_search)
    public void onClick() {
        Intent intent = new Intent(this, LibraryActivity.class);
        String queryText = getQueryText();
        intent.putExtra(AppConstants.LIBRARY_QUERY_TEXT,queryText);
        startActivity(intent);
    }
}
