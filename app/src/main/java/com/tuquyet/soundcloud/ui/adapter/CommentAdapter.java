package com.tuquyet.soundcloud.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuquyet.soundcloud.R;
import com.tuquyet.soundcloud.data.model.CommentModel;
import com.tuquyet.soundcloud.util.widget.SquareImageView;

import java.util.List;

/**
 * Created by tmd on 16/05/2017.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Context mContext;
    private List<CommentModel> mListComments;

    public CommentAdapter(Context context, List<CommentModel> listComments) {
        mContext = context;
        mListComments = listComments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommentModel item = mListComments.get(position);
        holder.blinData(item);
    }

    @Override
    public int getItemCount() {
        return mListComments != null ? mListComments.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private SquareImageView mSquareImageView;
        private TextView mTxtUsernameComment, mTxtBodyComment, mTxtTimeCreatedComment;

        public ViewHolder(View itemView) {
            super(itemView);
            mSquareImageView =
                    (SquareImageView) itemView.findViewById(R.id.square_image_view_avartar);
            mTxtUsernameComment = (TextView) itemView.findViewById(R.id.text_view_username_comment);
            mTxtBodyComment = (TextView) itemView.findViewById(R.id.text_view_body_comment);
            mTxtTimeCreatedComment =
                    (TextView) itemView.findViewById(R.id.text_view_time_created_comment);
        }

        public void blinData(CommentModel item) {
            if (item != null) {
                loadAvatar(item);
                mTxtUsernameComment.setText(item.getUser().getUsername());
                mTxtBodyComment.setText(item.getBody());
                mTxtTimeCreatedComment.setText(item.getCreatedAt());
            }
        }

        private void loadAvatar(CommentModel item) {
            Glide.with(mContext)
                    .load(item.getUser().getAvatarUrl())
                    .centerCrop()
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(mSquareImageView);
        }
    }
}
