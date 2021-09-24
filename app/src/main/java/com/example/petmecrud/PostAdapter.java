package com.example.petmecrud;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostAdapter extends FirebaseRecyclerAdapter<PostModel,PostAdapter.ViewHolder> {

    private Context context;
    public PostAdapter(@NonNull FirebaseRecyclerOptions<PostModel> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull PostModel model) {

            holder.tvcourseID.setText(model.getCourseId());
            holder.tvcourseNM.setText(model.getCourseName());
            holder.tvcourseFee.setText(model.getCourseFee());
            holder.tvcourseDuration.setText(model.getCourseDuration());

            String imageUri=model.getImage();

            Picasso.get().load(imageUri).into(holder.imageAdd);//from this line we can get the image from database


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference().child("Languages")
                            .child(getRef(position).getKey())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
            }
        });








            holder.update.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    DialogPlus dialog = DialogPlus.newDialog(context)
                            .setGravity(Gravity.CENTER)
                            .setMargin(50,0,50,0)
                            .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.content))
                            .setExpanded(false)
                            .create();
                                View holderView = dialog.getHolderView();

                    EditText id = holderView.findViewById(R.id.update_id);
                    EditText name = holderView.findViewById(R.id.update_name);
                    EditText fee = holderView.findViewById(R.id.update_fee);
                    EditText duration = holderView.findViewById(R.id.update_duration);
                    Button btnUpdate=holderView.findViewById(R.id.btnupdate);



                    id.setText(model.getCourseId());
                    name.setText(model.getCourseName());
                    fee.setText(model.getCourseFee());
                    duration.setText(model.getCourseDuration());

                        btnUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Map<String, Object> map= new HashMap<>();
                                map.put("courseId",id.getText().toString());
                                map.put("courseName",name.getText().toString());
                                map.put("courseFee",fee.getText().toString());
                                map.put("courseDuration",duration.getText().toString());

                                FirebaseDatabase.getInstance().getReference().child("Languages")
                                        .child(getRef(position).getKey())
                                        .updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });

                    dialog.show();
                }
            });




    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.desgin_row_for_recyclerview, parent,false);

       return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvcourseID,tvcourseNM,tvcourseFee,tvcourseDuration;
        ImageView imageAdd,update,delete;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvcourseID = itemView.findViewById(R.id.tv_courseIdRetrive);
            tvcourseNM = itemView.findViewById(R.id.tv_courseNameRetrive);
            tvcourseFee = itemView.findViewById(R.id.tv_courseFeeRetrive);
            tvcourseDuration = itemView.findViewById(R.id.tv_courseDurationRetrive);
            imageAdd = itemView.findViewById(R.id.image_ViewCourse);
            update=itemView.findViewById(R.id.image_edit);
            delete=itemView.findViewById(R.id.image_delete);



        }
    }
}
