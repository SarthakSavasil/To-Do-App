package com.sarthaksavasil.todoapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder> {
    String personalEmail;
    FirebaseFirestore db;
    DocumentReference docRef;
    Context context;
    String docID = "";
    private ArrayList<String> ourList;

    public TasksAdapter(Context context, ArrayList<String> ourList, String email) {
        this.context = context;
        this.ourList = ourList;
        personalEmail = email;
        getAllMetaData();
    }

    private void getAllMetaData() {

        db = FirebaseFirestore.getInstance();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView txt;

        public MyViewHolder(View view) {
            super(view);
            txt = view.findViewById(R.id.task_text);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Toast.makeText(context, ""+getAdapterPosition(), Toast.LENGTH_SHORT);
//                    ourList.remove(getAdapterPosition());
//                    notifyItemRemoved(getAdapterPosition());
//                    notifyItemRangeChanged(getAdapterPosition(),ourList.size());
//                }
//            });
        }


    }


    @Override
    public TasksAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_task, parent, false);
        return new TasksAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TasksAdapter.MyViewHolder holder, final int position) {
        final String listItem = ourList.get(position);
        Log.d("ABC", "onBindViewHolder: "+listItem);
        holder.txt.setText(listItem);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deletedFromDatabase(listItem);
                int pos = holder.getAdapterPosition();
                Toast.makeText(context, ""+pos, Toast.LENGTH_SHORT);
                ourList.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos,ourList.size());
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void deletedFromDatabase(String value) {

        CollectionReference collectionRef = db.collection(personalEmail);
        Query deliveryRef = db.collection(personalEmail);
        Query nameQuery = deliveryRef.whereEqualTo("Task", value);
        nameQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d(TAG, document.getId());
                        docID = document.getId();
                    }
                }
                deleteDocument();
                docID = "";
            }
            
        });
    }

    private void deleteDocument() {
        db.collection(personalEmail).document(docID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error deleting document", e);
                    }
                });

    }

    @Override
    public int getItemCount() {
        return ourList.size();
    }

}