package com.example.parstagrammm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parstagrammm.LoginActivity;
import com.example.parstagrammm.Post;
import com.example.parstagrammm.PostsAdapter;
import com.example.parstagrammm.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    public static final int PROFILE_IMAGE_ACTIVITY_REQUEST_CODE = 01;
    private String TAG = "ProfileFragment";

    private TextView tvUsername;
    private TextView tvPostCount;
    private TextView tvFollowers;
    private TextView tvFollowing;
    private Button btnAddProfileImage;
    private Button btnLogout;
    private RecyclerView rvProfile;

    private File profileImage;
    private String profileImageName = "profileimage.jpg";

    protected PostsAdapter adapter;
    protected List<Post> allPosts;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            tvUsername = view.findViewById(R.id.tvName);
            tvPostCount = view.findViewById(R.id.tvPostCount);
            tvFollowers = view.findViewById(R.id.tvFollowers);
            tvFollowing = view.findViewById(R.id.tvFollowing);
            btnAddProfileImage = view.findViewById(R.id.btnAddProfileImage);
            btnLogout = view.findViewById(R.id.btnLogout);
            rvProfile = view.findViewById(R.id.rvProfile);

            tvUsername.setText(ParseUser.getCurrentUser().getUsername());


            allPosts = new ArrayList<>();
            adapter = new PostsAdapter(getContext(), allPosts);

            tvPostCount.setText("0 \n Posts");
            tvFollowers.setText("0 \n Followers");
            tvFollowing.setText("0 \n Following");
            btnAddProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Attempting to set Profile Image", Toast.LENGTH_SHORT).show();
                }
            });

            btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                }
            });

            //set the adapter on the recycler view
            rvProfile.setAdapter(adapter);
            //set the layout manager on the recycler view
            rvProfile.setLayoutManager(new GridLayoutManager(getContext(), 3));
            queryPosts();
        }

    private void queryPosts() {
            ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

            query.include(Post.KEY_USER);
            query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
            query.setLimit(20);
            query.addDescendingOrder(Post.KEY_CREATED_AT);
            query.findInBackground(new FindCallback<Post>() {
                @Override
                public void done(List<Post> posts, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Error retrieving posts.", e);
                        return;
                    }
                    for(Post post : posts) {
                        Log.i(TAG, "Post Description: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                    }

                    allPosts.addAll(posts);
                    adapter.notifyDataSetChanged();
                }
            });
    }
}
