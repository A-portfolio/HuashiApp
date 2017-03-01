package net.muxi.huashiapp.ui.card;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.CardData;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.util.DateUtil;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.NetStatus;
import net.muxi.huashiapp.util.PreferenceUtil;
import net.muxi.huashiapp.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tencent.bugly.crashreport.inner.InnerAPI.context;

/**
 * Created by december on 16/7/18.
 */
public class CardActivity extends ToolbarActivity {

    @BindView(R.id.tv_date)
    TextView mDate;
    @BindView(R.id.money)
    TextView mMoney;
    @BindView(R.id.chart1)
    CombinedChart mChart1;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    public static void start(Context context) {
        Intent starter = new Intent(context, CardActivity.class);
        context.startActivity(starter);
    }


    private CombinedChart mChart;
    private final int itemcount = 7;

    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
    Date date = new Date();
    String day = formatter.format(new Date());
    String day1 = formatter.format(new Date(date.getTime() - (long) 1 * 24 * 60 * 60 * 1000));
    String day2 = formatter.format(new Date(date.getTime() - (long) 2 * 24 * 60 * 60 * 1000));
    String day3 = formatter.format(new Date(date.getTime() - (long) 3 * 24 * 60 * 60 * 1000));
    String day4 = formatter.format(new Date(date.getTime() - (long) 4 * 24 * 60 * 60 * 1000));
    String day5 = formatter.format(new Date(date.getTime() - (long) 5 * 24 * 60 * 60 * 1000));
    String day6 = formatter.format(new Date(date.getTime() - (long) 6 * 24 * 60 * 60 * 1000));

    private String[] mWeeks = new String[]{day6, day5, day4, day3, day2, day1, day};


    private List<CardData> mCardDatas;
    private float sum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        ButterKnife.bind(this);
        setTitle("校园卡");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);


        init();
        User user = new User();
        PreferenceUtil sp = new PreferenceUtil();
        user.setSid(sp.getString(PreferenceUtil.STUDENT_ID));
        user.setPassword(sp.getString(PreferenceUtil.STUDENT_PWD));
        if (!NetStatus.isConnected()) {
            ToastUtil.showShort(getString(R.string.tip_check_net));
        } else {
            CampusFactory.getRetrofitService()
                    .getCardBalance(user.getSid(), "90", "0", "20")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Observer<List<CardData>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtil.showShort(getString(R.string.tip_school_server_error));
                        }

                        @Override
                        public void onNext(List<CardData> cardDatas) {
                            Logger.d("id card");
                            mDate.setText(cardDatas.get(0).getDealDateTime());
                            mMoney.setText(cardDatas.get(0).getOutMoney());
                            mCardDatas = cardDatas;
                            setupCountView();
                        }
                    });
        }
    }

    public void init() {
        mToolbar.setTitle("学生卡");
    }


    public void setupCountView() {

        mChart = (CombinedChart) findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBorders(false);

        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);


        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mWeeks[(int) value % mWeeks.length];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        CombinedData data = new CombinedData();

        data.setData(generateLineData());
        data.setData(generateBarData());
        xAxis.setAxisMaximum(data.getXMax() + 0.5f);

        mChart.setData(data);
        mChart.invalidate();
    }

    /**
     * 获取指定日的消费总额
     *
     * @param day 前七天为0，今天为6
     * @return
     */
    private float getDailySum(int day) {
        String date = DateUtil.getTheDateInYear(new Date(), -6 + day);
        Logger.d(date);
        float sum = 0;
        for (int i = 0, size = mCardDatas.size(); i < size; i++) {
            if (mCardDatas.get(i).getDealTypeName().equals("消费"))
                if (date.equals(mCardDatas.get(i).getDealDateTime().substring(0, 10))) {
                    sum += Float.valueOf(mCardDatas.get(i).getTransMoney());
                }
        }
        Logger.d(sum + "");
        return sum;

    }

//    private float getRandom(float range, float startsfrom) {
//        return (float) (Math.random() * range) + startsfrom;
//    }


    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (int index = 0; index < itemcount; index++)
            entries.add(new Entry(index + 0.1f, getDailySum(index) / 2));

        LineDataSet set = new LineDataSet(entries, "");
        set.setColor(Color.rgb(141, 139, 219));
        set.setLineWidth(1f);
        set.setFillColor(Color.rgb(141, 139, 219));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCircleColor(Color.rgb(141, 139, 219));
        set.setCircleRadius(1f);
        set.setDrawValues(true);
        set.setValueTextSize(0.1f);
        set.setValueTextColor(Color.rgb(141, 139, 219));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarData() {

        ArrayList<BarEntry> entries1 = new ArrayList<BarEntry>();

        for (int index = 0; index < itemcount; index++) {
            entries1.add(new BarEntry(index + 0.1f, getDailySum(index)));
        }

        // stacked}

        BarDataSet set1 = new BarDataSet(entries1, "");
        set1.setColor(Color.rgb(141, 139, 219));
        set1.setValueTextColor(Color.rgb(141, 139, 219));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.2f;
        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);


        return d;
    }


}
