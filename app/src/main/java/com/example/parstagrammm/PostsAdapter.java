package com.example.parstagrammm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private TextView tvDescription;
        private ImageView ivImage;
        private ImageView ivProfileImage;
        private ImageView ivHeart;
        private ImageView ivComment;
        private ImageView ivDM;
        private ImageView ivSave;
        private TextView tvLikes;
        private TextView tvCreatedAt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivProfileImage = itemView.findViewById(R.id.ivProfilePic);
            ivHeart = itemView.findViewById(R.id.ivHeart);
            ivComment = itemView.findViewById(R.id.ivComment);
            ivDM = itemView.findViewById(R.id.ivDM);
            ivSave = itemView.findViewById(R.id.ivSave);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }

        public void bind(final Post post) {
            //bind the post data into the view elements
            tvUsername.setText(post.getUser().getUsername());
            tvDescription.setText(post.getDescription());

            tvCreatedAt.setText(post.getFormattedTimestamp());

            int numLikes = post.getLikes();
            if(numLikes == 0) {
                ivHeart.setImageResource(R.drawable.ufi_heart);
                tvLikes.setText(numLikes + " likes");
            }
            else {
                ivHeart.setImageResource(R.drawable.ufi_heart_active);
                if(numLikes > 1) {
                    tvLikes.setText(numLikes + " likes");
                }
                else {
                    tvLikes.setText(numLikes + " like");
                }
            }

            ParseFile image = post.getImage();
            if(image != null)
                Glide.with(context).load(post.getImage().getUrl()).into(ivImage);

            ParseFile profile_image = post.getUser().getParseFile("profile_image");
                if(profile_image != null)
                    Glide.with(context).load(post.getUser().getParseFile("profile_image").getUrl()).into(ivProfileImage);

                ivProfileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "go to profile", Toast.LENGTH_SHORT).show();
                        /*Intent i = new Intent(context, ProfileFragment.class);
                        i.putExtra("post", Parcels.wrap(post));
                        context.startActivity(i);*/
                    }
                });

                ivHeart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ivHeart.setImageResource(R.drawable.ufi_heart_active);

                        post.put("likes", post.getLikes() + 1);

                        post.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                post.put("likes", post.getLikes());
                            }
                        });
                        Log.i("PostAdapter", "liked");
                    }
                });

                ivComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "comment here", Toast.LENGTH_SHORT).show();
                    }
                });

                ivDM.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Send DM", Toast.LENGTH_SHORT).show();
                    }
                });

                ivSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "post saved", Toast.LENGTH_SHORT).show();
                    }
                });

        }

        //clean all elements of the recycler
        public void clear() {
            posts.clear();
            notifyDataSetChanged();
        }

        //add a list of posts
        public void addAll(List<Post> postList) {
            posts.addAll(postList);
            notifyDataSetChanged();
        }
    }
}
