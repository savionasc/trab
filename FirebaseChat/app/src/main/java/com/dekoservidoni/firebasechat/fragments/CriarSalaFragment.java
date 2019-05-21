package com.dekoservidoni.firebasechat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dekoservidoni.firebasechat.ActivityCallback;
import com.dekoservidoni.firebasechat.R;
import com.dekoservidoni.firebasechat.models.ChatData;
import com.dekoservidoni.firebasechat.models.Sala;
import com.dekoservidoni.firebasechat.utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CriarSalaFragment extends Fragment {
    private EditText nomeSala;
    private ActivityCallback mCallback;

    public static CriarSalaFragment newInstance() {
        return new CriarSalaFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_criar_sala, container, false);

        nomeSala = (EditText) root.findViewById(R.id.criarsala_nomeSala);

        Button createButton = (Button) root.findViewById(R.id.criarsala_botao);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarSala();
            }
        });

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

    /// Private methods

    private void criarSala() {
        String nSala = nomeSala.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mReference = database.getReference(Constants.DATABASE_NAME);

        Sala sala = new Sala();
        sala.setNumUsuarios("5");
        sala.setChatData(new ArrayList<ChatData>());
        sala.setNome(nSala);
        mReference.child(sala.getNome()).setValue(sala);
        nomeSala.setText("");
        mCallback.logout();
    }

}
