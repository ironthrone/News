package com.guo.news.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/11/29.
 */

public class AsyncImageGetter implements Html.ImageGetter {

    private TextView mTargetView;
    private final Context mContext;

    public AsyncImageGetter(Context context, TextView targetView) {
        mContext = context;
        this.mTargetView = targetView;
    }

    @Override
    public Drawable getDrawable(String source) {
        DrawableWrapper drawableWrapper = new DrawableWrapper();
        new FillDrawableWrapper(drawableWrapper).execute(source);
        return drawableWrapper;
    }

    private class FillDrawableWrapper extends AsyncTask<String, Void, Drawable> {
        DrawableWrapper mDrawableWrapper;

        public FillDrawableWrapper(DrawableWrapper drawableWrapper) {
            this.mDrawableWrapper = drawableWrapper;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            Drawable drawable = null;
            try {
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();
//                drawable = Drawable.createFromStream(inputStream, "image");
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                bitmap.setDensity(Bitmap.DENSITY_NONE);
                drawable = new BitmapDrawable(mContext.getResources(),bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) inputStream.close();
                    if (httpURLConnection != null) httpURLConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return drawable;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            if (drawable != null) {
//                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//                mDrawableWrapper.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                int width = mTargetView.getWidth();
                int height = width * drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth();
                drawable.setBounds(0, 0, width, height);
                mDrawableWrapper.setBounds(0, 0, width, height);
                mDrawableWrapper.setDrawable(drawable);
                AsyncImageGetter.this.mTargetView.setText(mTargetView.getText());
            }
        }
    }

    private static class DrawableWrapper extends BitmapDrawable {
        private Drawable mDrawable;

        @Override
        public void draw(Canvas canvas) {
            if (mDrawable != null) {
                mDrawable.draw(canvas);
            }
        }

        public void setDrawable(Drawable drawable) {
            mDrawable = drawable;
        }
    }

}
