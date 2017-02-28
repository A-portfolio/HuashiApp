package net.muxi.huashiapp.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseFragment;
import net.muxi.huashiapp.common.data.ItemData;
import net.muxi.huashiapp.ui.score.ScoreSelectActivity;
import net.muxi.huashiapp.util.ACache;
import net.muxi.huashiapp.util.PreferenceUtil;
import net.muxi.huashiapp.util.VibratorUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 17/1/25.
 */

public class MainFragment extends BaseFragment implements MyItemTouchCallback.OnDragListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private MainAdapter mMainAdapter;

    private ItemTouchHelper itemTouchHelper;

    private PreferenceUtil sp;


    private List<ItemData> mItemDatas = new ArrayList<ItemData>();


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        ArrayList<ItemData> items = (ArrayList<ItemData>) ACache.get(getActivity()).getAsObject("items");

        if (items != null) {
            mItemDatas.addAll(items);
        } else {
            mItemDatas.add(new ItemData("成绩", R.drawable.ic_score + ""));
            mItemDatas.add(new ItemData("校园通知", R.drawable.ic_news + ""));
            mItemDatas.add(new ItemData("电费", R.drawable.ic_ele + ""));
            mItemDatas.add(new ItemData("校园卡", R.drawable.ic_card + ""));
            mItemDatas.add(new ItemData("算学分", R.drawable.ic_credit + ""));
            mItemDatas.add(new ItemData("空闲教室", R.drawable.ic_empty_room + ""));
            mItemDatas.add(new ItemData("部门信息", R.drawable.ic_apartment + ""));
            mItemDatas.add(new ItemData("校历", R.drawable.ic_calendar + ""));
            mItemDatas.add(new ItemData("常用网站", R.drawable.ic_net + ""));
            mItemDatas.add(new ItemData("学而", R.drawable.ic_xueer + ""));
            mItemDatas.add(new ItemData("更多", R.drawable.ic_more + ""));
        }

        mToolbar.setTitle("华师匣子");
        mMainAdapter = new MainAdapter(mItemDatas);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMainAdapter);

        itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(mMainAdapter).setOnDragListener(this));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                if (vh.getLayoutPosition() != mItemDatas.size()) {
                    itemTouchHelper.startDrag(vh);
                    VibratorUtil.Vibrate(getActivity(), 50);
                }
            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                ItemData itemData = mItemDatas.get(vh.getLayoutPosition());
                switch (itemData.getName()){
                    case "成绩":
                        ScoreSelectActivity.start(getContext());
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void onFinishDrag() {
        ACache.get(getActivity()).put("items", (ArrayList<ItemData>) mItemDatas);
    }
}
