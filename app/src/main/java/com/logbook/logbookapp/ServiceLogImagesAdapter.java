package com.logbook.logbookapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 4/10/2016.
 */
public class ServiceLogImagesAdapter extends RecyclerView.Adapter<ServiceLogImagesAdapter.CustomViewHolder>{
    private View animatedView = null;
    private Context context;
    private List<Bitmap> imagesToShow = new ArrayList<>();

    public ServiceLogImagesAdapter(Context context,List<Bitmap> imagesToShow) {
        this.imagesToShow = imagesToShow;
        this.context = context;
    }

    @Override
    public ServiceLogImagesAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup,  int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.display_images_adapter, viewGroup,false);


        CustomViewHolder viewHolder = new CustomViewHolder(view);
    /*final Animation a = AnimationUtils.loadAnimation(mContext, R.anim.scale_up);*/
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // You can tweak with the effects here
                if (animatedView == null) {
                    animatedView = view;
                } else {
                    animatedView.setAnimation(null);
                    animatedView = view;
                }
                ScaleAnimation fade_in = new ScaleAnimation(1f, 1.3f, 1f, 1.3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                fade_in.setDuration(100);     // animation duration in milliseconds
                fade_in.setFillAfter(true);    // If fillAfter is true, the transformation that this animation performed will persist when it is finished.
                view.startAnimation(fade_in);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        //set image here
        holder.imageView.setImageBitmap(imagesToShow.get(position));
    }


    @Override
    public int getItemCount() {
        return imagesToShow.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.ServiceLogThumbNails);
        }
    }

}
