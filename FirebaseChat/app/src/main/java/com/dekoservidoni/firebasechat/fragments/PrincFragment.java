package com.dekoservidoni.firebasechat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dekoservidoni.firebasechat.ActivityCallback;
import com.dekoservidoni.firebasechat.R;
import com.dekoservidoni.firebasechat.adapters.ChatAdapter;
import com.dekoservidoni.firebasechat.adapters.SalaAdapter;
import com.dekoservidoni.firebasechat.models.ChatData;
import com.dekoservidoni.firebasechat.models.Sala;
import com.dekoservidoni.firebasechat.utils.Constants;
import com.dekoservidoni.firebasechat.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class PrincFragment extends Fragment {

    private Sala s;
    private ActivityCallback mCallback;
    private DatabaseReference mReference;
    private SalaAdapter mAdapter;

    public static PrincFragment newInstance() {
        return new PrincFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupConnection();
    }
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private View mProgressView;
    private View mprincipalView;

    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mprincipalView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_principal, container, false);

        Button b = root.findViewById(R.id.criar_sala_botaox);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.openCreateAccount();
            }
        });
        //carregamento
        mprincipalView = root.findViewById(R.id.listaChats);
        mProgressView = root.findViewById(R.id.principal_progress);
        showProgress(true);

        RecyclerView salas = (RecyclerView) root.findViewById(R.id.listaChats);
        salas.setLayoutManager(new LinearLayoutManager(getContext()));
        //mAdapter = new SalaAdapter(root.getContext());
        FirebaseDatabase database;
        database = FirebaseDatabase.getInstance();
        mReference = database.getReference(Constants.DATABASE_NAME);

        //RecyclerViewAdapter adapter;
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(Constants.LOG_TAG, "SUCCESS!");
                long test = 0;
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    mNames.add(item.getValue(Sala.class).getNome());
                    Log.d(Constants.LOG_TAG, "S!" + test++);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(Constants.LOG_TAG,"ERROR: " + databaseError.getMessage());
                Toast.makeText(getContext(), R.string.chat_init_error, Toast.LENGTH_SHORT).show();
                mCallback.logout();
            }
        });
        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = root.findViewById(R.id.recyclerv_view);
        mAdapter = new SalaAdapter(getContext(), mNames, mImageUrls);
        salas.setAdapter(mAdapter);
        salas.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.notifyDataSetChanged();
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (ActivityCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            mCallback.logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupConnection() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mReference = database.getReference(Constants.DATABASE_NAME);

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(Constants.LOG_TAG,"SUCCESS!");
                handleReturn(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(Constants.LOG_TAG,"ERROR: " + databaseError.getMessage());
                Toast.makeText(getContext(), R.string.chat_init_error, Toast.LENGTH_SHORT).show();
                mCallback.logout();
            }
        });
    }

    private void handleReturn(DataSnapshot dataSnapshot) {
        mAdapter.clearSala();
        for (DataSnapshot item  : dataSnapshot.getChildren()) {
            mAdapter.addSala(item.getValue(Sala.class));
        }
        mAdapter.notifyDataSetChanged();
        showProgress(false);
    }
}