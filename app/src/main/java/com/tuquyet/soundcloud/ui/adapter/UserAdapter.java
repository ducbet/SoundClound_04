package com.tuquyet.soundcloud.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tuquyet.soundcloud.R;
import com.tuquyet.soundcloud.ui.item.UserItem;

import java.util.List;

/**
 * Created by tuquyet on 23/05/2017.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<UserItem> mUserItems;

    public UserAdapter(List<UserItem> userItems) {
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
        UserItem userItem = mUserItems.get(position);
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

        public ViewHolder(View itemView) {
            super(itemView);
            initView();
            itemView.setOnClickListener(this);
        }

        private void initView() {
            mImageUserAvatar = (ImageView) itemView.findViewById(R.id.image_user_avatar);
            mTextUserName = (TextView) itemView.findViewById(R.id.text_user_name);
            mTextUserCity = (TextView) itemView.findViewById(R.id.text_user_city);
            mTextUserCountry = (TextView) itemView.findViewById(R.id.text_user_country);
        }

        public void bindData(UserItem userItem) {
            mImageUserAvatar.setImageResource(userItem.getUserAvatar());
            mTextUserName.setText(userItem.getUserName());
            mTextUserCity.setText(userItem.getUserCity());
            mTextUserCountry.setText(userItem.getUserCountry());
        }

        @Override
        public void onClick(View v) {
            UserItem item = mUserItems.get(getAdapterPosition());
            Toast.makeText(v.getContext(), item.getUserCountry(), Toast.LENGTH_SHORT).show();
        }
    }
}
