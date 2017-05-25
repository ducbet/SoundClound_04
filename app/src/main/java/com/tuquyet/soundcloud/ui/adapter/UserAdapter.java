package com.tuquyet.soundcloud.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tuquyet.soundcloud.R;
import com.tuquyet.soundcloud.data.model.UserModel;

import java.util.List;

/**
 * Created by tuquyet on 23/05/2017.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<UserModel> mUserItems;

    public UserAdapter(List<UserModel> userItems) {
        mUserItems = userItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_follow_user, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserModel userItem = mUserItems.get(position);
        holder.bindData(userItem);
    }

    @Override
    public int getItemCount() {
        return mUserItems != null ? mUserItems.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageUserAvatar;
        private TextView mTextUserName;
        private TextView mTextUserCity;
        private TextView mTextUserCountry;
        private Context mContext;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            initView();
            itemView.setOnClickListener(this);
        }

        private void initView() {
            mImageUserAvatar = (ImageView) itemView.findViewById(R.id.image_user_avatar);
            mTextUserName = (TextView) itemView.findViewById(R.id.text_user_name);
            mTextUserCity = (TextView) itemView.findViewById(R.id.text_user_city);
            mTextUserCountry = (TextView) itemView.findViewById(R.id.text_user_country);
        }

        public void bindData(UserModel userItem) {
            if (userItem != null) {
                loadAvatar(userItem);
                mTextUserName.setText(userItem.getUsername());
                mTextUserCity.setText(userItem.getCity());
                mTextUserCountry.setText(userItem.getCountry());
            }
        }

        @Override
        public void onClick(View v) {
            UserModel item = mUserItems.get(getAdapterPosition());
            Toast.makeText(v.getContext(), item.getCountry(), Toast.LENGTH_SHORT).show();
        }

        private void loadAvatar(UserModel userItem) {
            Glide.with(mContext)
                    .load(userItem.getAvatarUrl())
                    .centerCrop()
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(mImageUserAvatar);
        }
    }
}
