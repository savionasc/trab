package com.dekoservidoni.firebasechat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dekoservidoni.firebasechat.ActivityCallback;
import com.dekoservidoni.firebasechat.R;
import com.dekoservidoni.firebasechat.models.Sala;
import com.dekoservidoni.firebasechat.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PrincipalFragment extends Fragment {
    //private EditText nomeSala;
    private long count = 0;
    private ActivityCallback mCallback;
    private DatabaseReference mReference;
    //private SalaAdapter mAdapter;

    public static PrincipalFragment newInstance() {
        return new PrincipalFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
        //setupConnection();
    }

    //Recycle
    private static final String TAG = "MainActivity";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    FirebaseDatabase database;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_main, container, false);
        //View root = inflater.inflate(R.layout.fragment_principal, container, false);
        //nomeSala = (EditText) root.findViewById(R.id.criarsala_nomeSala);
        //Button createButton = (Button) root.findViewById(R.id.criarsala_botao);
        /*createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarSala();
            }
        });*/

        ////////////////////////////////descomentar isso
        /*RecyclerView sala = (RecyclerView) root.findViewById(R.id.listaChats);
        sala.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new SalaAdapter();
        sala.setAdapter(mAdapter);*/
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        /*mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
        mNames.add("Havasu Falls");

        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNames.add("Trondheim");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNames.add("Portugal");

        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        mNames.add("Rocky Mountain National Park");


        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mNames.add("Mahahual");

        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        mNames.add("Frozen Lake");


        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
        mNames.add("White Sands Desert");

        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
        mNames.add("Austrailia");

        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
        mNames.add("Washington");*/

        database = FirebaseDatabase.getInstance();
        mReference = database.getReference(Constants.DATABASE_NAME);

        RecyclerViewAdapter adapter;
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(Constants.LOG_TAG,"SUCCESS!");
                long test = 0;
                //mAdapter.clearSala();
                //for(DataSnapshot item : dataSnapshot.getChildren()) {
                //DataSnapshot item = dataSnapshot.child("desvende");
                //count = item.getChildrenCount()+1;
                //Aqui que eu tenho que transformar o desvende em um objeto que tem atributos e um chat embutido
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    mNames.add(item.getValue(Sala.class).getNome());
                    Log.d(Constants.LOG_TAG,"S!"+test++);
                }
                Toast.makeText(getContext(), "Saiu", Toast.LENGTH_SHORT).show();
                //mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(Constants.LOG_TAG,"ERROR: " + databaseError.getMessage());
                Toast.makeText(getContext(), R.string.chat_init_error, Toast.LENGTH_SHORT).show();
                mCallback.logout();
            }
        });

        mImageUrls.add("https://firebasestorage.googleapis.com/v0/b/chatfirebase-15a58.appspot.com/o/ZcLLrkY_d.jpg?alt=media&token=1b398d1f-f641-4e74-9ae0-718bdbdf8a8f2");
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = root.findViewById(R.id.recyclerv_view);
        adapter = new RecyclerViewAdapter(getContext(), mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.notifyDataSetChanged();
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
}