    package com.mindvalley_smaran_android_test.adapter;

    import android.app.Activity;
    import android.content.Context;
    import android.content.res.Resources;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.util.DisplayMetrics;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.BaseAdapter;
    import android.widget.FrameLayout;
    import android.widget.ImageView;
    import android.widget.ProgressBar;

    import com.mindvalley_smaran_android_test.MainActivity;
    import com.mindvalley_smaran_android_test.R;
    import com.mindvalley_smaran_android_test.model.User;
    import com.nostra13.universalimageloader.core.DisplayImageOptions;
    import com.nostra13.universalimageloader.core.ImageLoader;
    import com.nostra13.universalimageloader.core.assist.FailReason;
    import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
    import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

    import java.io.IOException;
    import java.io.InputStream;
    import java.net.HttpURLConnection;
    import java.net.URL;
    import java.util.ArrayList;
    import java.util.Random;

    /**
     * Created by smara on 19-Aug-16.
     */
    public class ImageAdapter extends BaseAdapter {



//        private static final String[] IMAGE_URLS =new String[10];

        private LayoutInflater inflater;

        private DisplayImageOptions options;
        ArrayList<User> _userArrrayList = new ArrayList<User>();


        public ImageAdapter(Context context, ArrayList<User> arrayList) {
            inflater = LayoutInflater.from(context);
            _userArrrayList= arrayList;
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.wait)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true)
                    .resetViewBeforeLoading(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }

        @Override
        public int getCount() {
            return _userArrrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.item_grid_image, parent, false);
                holder = new ViewHolder();
                assert view != null;
                holder.imageView = (ImageView) view.findViewById(R.id.image);
                holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            ImageLoader.getInstance()
                    .displayImage(_userArrrayList.get(position).getUrl_full(), holder.imageView, options, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            holder.progressBar.setProgress(0);
                            holder.progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            holder.progressBar.setVisibility(View.GONE);
                        }
                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            Log.e("tag", "onLoadingCancelled");
                        }
                    }, new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String imageUri, View view, int current, int total) {
                            holder.progressBar.setProgress(Math.round(100.0f * current / total));
                        }
                    });

            return view;
        }
    }

    class ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
    }