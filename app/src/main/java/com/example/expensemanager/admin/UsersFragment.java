package com.example.expensemanager.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.admin.HomeActivity;
import com.example.expensemanager.Model.Data;
import com.example.expensemanager.Model.userid;
import com.example.expensemanager.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UsersFragment newInstance(String param1, String param2) {
        UsersFragment fragment = new UsersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //Firebase database

    private FirebaseAuth mAuth;
    private DatabaseReference mUsersDatabase;

    //Recyclerview

    private RecyclerView recyclerView;

    //TextView

    private TextView expenseSumResult;


    //Edit data item

    private EditText edtAmount;
    private EditText edtType;
    private EditText edtNote;

    private Button btnExpense;
    private Button btnIncome;

    //Data variable

    private String type;
    private String note;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview =  inflater.inflate(R.layout.fragment_users, container, false);

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        recyclerView = myview.findViewById(R.id.recycler_id_users);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mUsersDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot mysnapshot: snapshot.getChildren()){
                     String uid = mysnapshot.child("uid").getValue().toString();
                     userid usid=mysnapshot.getValue(userid.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return myview;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<userid, MyViewHolder>adapter = new FirebaseRecyclerAdapter<userid, MyViewHolder>(
                userid.class,
                R.layout.user_recycler_data,
                MyViewHolder.class,
                mUsersDatabase
        ) {
            @Override
            protected void populateViewHolder(MyViewHolder myViewHolder, userid data, int i) {
                myViewHolder.setId(data.getId());
                myViewHolder.setEmail(data.getEmail());
                myViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sharedPreferences= getContext().getSharedPreferences("mysharepref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor myEdit=sharedPreferences.edit();
                        myEdit.putString("id",data.getId());
                        //updateDataItem();
                        myEdit.commit();
                        myEdit.apply();
                        startActivity(new Intent(requireContext(), HomeActivity.class));
                    }}
                    );
            }
        };

        recyclerView.setAdapter(adapter);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        private void setId(String id){
            TextView mid = mView.findViewById(R.id.id);
         mid.setText(id);
        }
        private void setEmail(String email){
            TextView mid = mView.findViewById(R.id.email);
            mid.setText(email);
        }

    }

    private void updateDataItem(){

        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myview = inflater.inflate(R.layout.user_dash, null);
        mydialog.setView(myview);
        btnExpense= myview.findViewById(R.id.btnExpense);
        btnIncome = myview.findViewById(R.id.btnIncome);

        final AlertDialog dialog = mydialog.create();

        btnExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpenseFragment expenseFragment=new ExpenseFragment();
                setFragment(expenseFragment);
            }
        });

        btnExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IncomeFragment incomeFragment=new IncomeFragment();
                setFragment(incomeFragment);
            }
        });

        dialog.show();
    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}