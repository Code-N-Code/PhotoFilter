package com.codencode.photofilter;

import android.graphics.Bitmap;

import com.zomato.photofilters.imageprocessors.Filter;

public class CircleImage {
    Bitmap image;
    Filter filter;

    CircleImage()
    {

    }

    CircleImage(Bitmap image , Filter filter)
    {
        this.image = image;
        this.filter = filter;
    }

    void setImage(Bitmap image)
    {
        this.image = image;
    }

    void setFilter(Filter filter)
    {
        this.filter = filter;
    }

    Filter getFilter()
    {
        return this.filter;
    }

    Bitmap getImage()
    {
        return this.image;
    }
}
