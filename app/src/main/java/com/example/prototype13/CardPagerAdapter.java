package com.example.prototype13;

import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<CardItem> mData;
    private float mBaseElevation;

    public CardPagerAdapter() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItem(CardItem item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.adapter, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(CardItem item, View view) {

        ImageView imageView =  view.findViewById(R.id.details_image_view);
        //contentTextView.setText(item.getText());
        Picasso.get().load(item.getURL()).into(imageView);
        //imageView.requestLayout();
        // imageView.getLayoutParams().height = (int)Math.round(imageView.getWidth()*item.getRatio());
        // Log.i("EROR", String.valueOf(imageView.getHeight()));
        /*Drawable drawable = imageView.getDrawable();
        long ratio = (drawable.getIntrinsicHeight())/(drawable.getIntrinsicWidth());
        if(ratio>1.3){
            imageView.requestLayout();
            imageView.getLayoutParams().height = (int)Math.round(imageView.getWidth()*1.3);
        }
        if(ratio<=1.3){
            imageView.requestLayout();;
            imageView.getLayoutParams().height = (int)Math.round(imageView.getWidth()*ratio);
        }*/
    }

}
