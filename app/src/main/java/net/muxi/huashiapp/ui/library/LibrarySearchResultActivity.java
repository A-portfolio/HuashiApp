package net.muxi.huashiapp.ui.library;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 17/2/13.
 */

public class LibrarySearchResultActivity extends ToolbarActivity {

    @BindView(R.id.drawee)
    SimpleDraweeView mDrawee;

    public static void start(Context context,String query) {
        Intent starter = new Intent(context, LibrarySearchResultActivity.class);
        starter.putExtra("query",query);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        ButterKnife.bind(this);
        Uri uri = Uri.parse("asset://net.muxi.huashiapp/loading.gif");
        DraweeController controller = Fresco.newDraweeControllerBuilder().setUri(uri).setAutoPlayAnimations(true).build();
        mDrawee.setController(controller);
    }
}
