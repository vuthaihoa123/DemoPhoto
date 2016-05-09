package framgia.vn.photoSketch.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import framgia.vn.photoSketch.R;
import framgia.vn.photoSketch.models.Video;

/**
 * Created by hoavt on 05/05/2016.
 */
public class ListVideoAdapter extends RecyclerView.Adapter<ListVideoAdapter.ViewHolder> {
    public static final String VIDEO_MIME_TYPE = "video/*";
    private VideoRequestHandler videoRequestHandler = null;
    private Picasso picassoInstance = null;
    private List<Video> mListVideo = new ArrayList<>();
    private Context mContext = null;

    public ListVideoAdapter(Context context, List<Video> listVideo) {
        mContext = context;
        mListVideo = listVideo;
        videoRequestHandler = new VideoRequestHandler();
        picassoInstance = new Picasso.Builder(mContext.getApplicationContext())
                .addRequestHandler(videoRequestHandler)
                .build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_photo, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Video video = mListVideo.get(position);
        final Uri uri = Uri.fromFile(new File(video.getPath()));
        picassoInstance.load(videoRequestHandler.SCHEME_VIEDEO + ":" + video.getPath()).into(holder.imageViewVideo);

//        Bitmap bmpVideo = ThumbnailUtils.createVideoThumbnail(video.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
//        holder.imageViewVideo.setImageBitmap(bmpVideo);
        holder.imageViewVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentViewer = new Intent();
                intentViewer.setAction(Intent.ACTION_VIEW);
                intentViewer.setDataAndType(uri, VIDEO_MIME_TYPE);
                mContext.startActivity(intentViewer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListVideo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewVideo;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewVideo = (ImageView) itemView.findViewById(R.id.image_item_photo);
        }
    }

    public class VideoRequestHandler extends RequestHandler {
        public static final String SCHEME_VIEDEO = "video";

        @Override
        public boolean canHandleRequest(Request data) {
            String scheme = data.uri.getScheme();
            return (SCHEME_VIEDEO.equals(scheme));
        }

        @Override
        public Result load(Request data, int arg1) throws IOException {
            Bitmap bm = ThumbnailUtils.createVideoThumbnail(data.uri.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
            return new Result(bm, Picasso.LoadedFrom.DISK);
        }
    }
}

