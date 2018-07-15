package net.muxi.huashiapp.ui.library;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import net.muxi.huashiapp.R;


/**
 * Created by ybao on 16/5/23.
 */
public class MySearchAdapter extends BaseAdapter {

    private String[] suggestions;
    private Drawable suggestionIcon;
    private LayoutInflater inflater;

    public MySearchAdapter(Context context, String[] suggestions) {
        inflater = LayoutInflater.from(context);
        this.suggestions = suggestions;
    }

    public MySearchAdapter(Context context, String[] suggestions, Drawable suggestionIcon) {
        inflater = LayoutInflater.from(context);
        this.suggestions = suggestions;
        this.suggestionIcon = suggestionIcon;
    }


    @Override
    public Object getItem(int position) {
        return suggestions[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SuggestionViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_suggestion, parent, false);
            viewHolder = new SuggestionViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SuggestionViewHolder) convertView.getTag();
        }
        viewHolder.mSuggestionText.setText((String) getItem(position));
        return convertView;
    }

    @Override
    public int getCount() {
        return suggestions.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class SuggestionViewHolder {
        private ImageView mSuggestionIcon;
        private TextView mSuggestionText;

        public SuggestionViewHolder(View view) {
            super();
            mSuggestionIcon = view.findViewById(R.id.suggestion_icon);
            mSuggestionText = view.findViewById(R.id.suggestion_text);
            mSuggestionIcon.setImageDrawable(suggestionIcon);
        }
    }


}
