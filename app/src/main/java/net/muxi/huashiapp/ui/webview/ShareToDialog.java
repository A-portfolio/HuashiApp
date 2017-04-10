package net.muxi.huashiapp.ui.webview;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.main.OnRecyclerItemClickListener;
import net.muxi.huashiapp.widget.BottomDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 17/4/5.
 */

public class ShareToDialog extends BottomDialogFragment {


    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private Integer [] pics = {R.drawable.ic_share_toqq,R.drawable.ic_share_towx ,R.drawable.ic_share_toweibo,
                               R.drawable.ic_share_toqzone,R.drawable.ic_share_tomoments,0,
                               R.drawable.ic_share_refresh,R.drawable.ic_share_copy_link,R.drawable.ic_share_safari};

    private String [] desc = {"QQ好友","微信好友","微博","QQ空间","朋友圈"," ","刷新页面","复制链接","浏览器打开"};

    private List<Integer> mpic;
    private List<String> mdesc;


    private ShareAdapter mShareAdapter;

    private OnItemClick mOnItemClick;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_shareto, null);
        ButterKnife.bind(this, view);
        Dialog dialog = createBottomDialog(view);

        mpic = new ArrayList<>();
        mdesc = new ArrayList<>();

        mpic.addAll(Arrays.asList(pics));
        mdesc.addAll(Arrays.asList(desc));
        mShareAdapter = new ShareAdapter(mpic,mdesc);
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mShareAdapter);

        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView){
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                if (mOnItemClick != null){
                    mOnItemClick.onItemClick(vh.getLayoutPosition());
                }
            }
        });


        return dialog;

    }



    public void setOnItemClickListener(OnItemClick onItemClick){
        mOnItemClick = onItemClick;
    }

    public interface OnItemClick{

        void onItemClick(int position);
    }

}
