package com.example.application;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostAdapter extends ArrayAdapter<DataModel> {
    private ArrayList<DataModel> dataSet;
    private Context mContext;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference colRef = firestore.collection("saved");

    private static class ViewHolder {
        TextView userName;
        TextView publishTime;
        TextView readTime;
        TextView postContent;
        TextView postid;
        ImageButton save;
    }

    public PostAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.post_layout, data);
        this.dataSet = data;
        this.mContext = context;
    }

    private int lastPos = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataModel dataModel = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.post_layout, parent, false);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.userName);
            viewHolder.publishTime = (TextView) convertView.findViewById(R.id.publishTime);
            viewHolder.readTime = (TextView) convertView.findViewById(R.id.readTime);
            viewHolder.postContent = (TextView) convertView.findViewById(R.id.postContent);
            viewHolder.save = (ImageButton) convertView.findViewById(R.id.save);
            viewHolder.postid = (TextView)convertView.findViewById(R.id.postid);

            viewHolder.save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("postid", viewHolder.postid.getText().toString());
                    data.put("savedBy", user.getUid());

                    colRef.whereEqualTo("postid", viewHolder.postid.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult().isEmpty()) {
                                colRef.add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()) {
                                            if (viewHolder.save.getDrawable() != ContextCompat.getDrawable(getContext(), R.drawable.ic_baseline_post_add_24)) {
                                                viewHolder.save.setImageResource(R.drawable.ic_baseline_bookmark_24);
                                            }
                                        } else {
                                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            });

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.userName.setText(dataModel.getUserName());
        viewHolder.publishTime.setText(dataModel.getPublishTime());
        viewHolder.postContent.setText(dataModel.getPostContent());
        viewHolder.readTime.setText(dataModel.getReadTime());
        viewHolder.postid.setText(dataModel.getPostid());

        return convertView;
    }
}
