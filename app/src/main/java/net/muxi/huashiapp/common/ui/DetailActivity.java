package net.muxi.huashiapp.common.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.util.News;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by december on 16/4/27.
 */
public class DetailActivity extends AppCompatActivity {

    @Bind(R.id.news_info_photo)
    ImageView mNewsInfoPhoto;
    @Bind(R.id.news_info_title)
    TextView mNewsInfoTitle;
    @Bind(R.id.news_info_desc)
    TextView mNewsInfoDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        News item = (News)intent.getSerializableExtra("News");
        mNewsInfoPhoto.setImageResource(item.getPhotoId());
        mNewsInfoTitle.setText(item.getTitle());
        mNewsInfoDesc.setText(item.getDesc());



    }
}
