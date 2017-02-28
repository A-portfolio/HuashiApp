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
import net.muxi.huashiapp.common.data.Item;

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

//    private String[] titles = {"成绩", "校园通知", "电费", "校园卡", "算学分", "空闲教室", "部门信息", "校历", "常用网址", "学而"};
//    private Integer[] icons =
//            {R.drawable.ic_score, R.drawable.ic_news, R.drawable.ic_ele, R.drawable.ic_card,
//                    R.drawable.ic_credit, R.drawable.ic_empty_room, R.drawable.ic_apartment,
//                    R.drawable.ic_calendar, R.drawable.ic_net, R.drawable.ic_xueer};

    private List<Item> results = new ArrayList<Item>();


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

        results.add(new Item(0,"成绩",R.drawable.ic_score));
        results.add(new Item(1,"校园通知", R.drawable.ic_news));
        results.add(new Item(2,"电费",R.drawable.ic_ele));
        results.add(new Item(3,"校园卡", R.drawable.ic_card));
        results.add(new Item(4,"算学分", R.drawable.ic_credit));
        results.add(new Item(5,"空闲教室", R.drawable.ic_empty_room));
        results.add(new Item(6,"部门信息",R.drawable.ic_apartment));
        results.add(new Item(7,"校历", R.drawable.ic_calendar));
        results.add(new Item(8,"常用网站",R.drawable.ic_net));
        results.add(new Item(9,"学而", R.drawable.ic_xueer));
        results.add(new Item(results.size(),"更多", R.drawable.ic_more));

        mToolbar.setTitle("华师匣子");
        mMainAdapter = new MainAdapter(results);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMainAdapter);

        itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(mMainAdapter).setOnDragListener(this));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView){
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                if (vh.getLayoutPosition() != results.size() ) {
                    itemTouchHelper.startDrag(vh);
                }
            }
        });
        return view;
    }

    @Override
    public void onFinishDrag() {

    }
}
