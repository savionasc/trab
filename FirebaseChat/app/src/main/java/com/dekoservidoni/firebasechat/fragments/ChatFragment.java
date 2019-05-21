package com.dekoservidoni.firebasechat.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dekoservidoni.firebasechat.ActivityCallback;
import com.dekoservidoni.firebasechat.R;
import com.dekoservidoni.firebasechat.adapters.ChatAdapter;
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
import java.util.Date;

import static android.support.constraint.Constraints.TAG;

public class ChatFragment extends Fragment {
    private Sala s;
    private long count = 0;
    private ActivityCallback mCallback;
    private DatabaseReference mReference;

    private EditText mChatInput;
    private ChatAdapter mAdapter;

    private String mUsername;
    private String mUserId;

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mUsername = Utils.getLocalUsername(getContext());
        mUserId = Utils.getLocalUserId(getContext());
        setupConnection();
    }
    private String sala = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);
        sala = getArguments().getString("sala");

        mChatInput = (EditText) root.findViewById(R.id.chat_input);
        mChatInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                ArrayList<ChatData> chat = s.getChatData();
                ChatData c = new ChatData();
                c.setMessage(mChatInput.getText().toString());
                c.setName(mUsername);
                if(chat != null){
                    chat.add(c);
                }else{
                    chat = new ArrayList<ChatData>();
                    chat.add(c);
                }
                s.setChatData(chat);
                mReference.child(sala).setValue(s);

                closeAndClean();
                return true;
            }
        });

        RecyclerView chat = (RecyclerView) root.findViewById(R.id.chat_message);
        chat.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new ChatAdapter();
        chat.setAdapter(mAdapter);

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

    private void closeAndClean() {
        Utils.closeKeyboard(getContext(), mChatInput);
        mChatInput.setText("");
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
        mAdapter.clearData();
        DataSnapshot item = dataSnapshot.child(sala);
        count = item.getChildrenCount()+1;
        s = item.getValue(Sala.class);
        if(s.getChatData() != null){
            for(ChatData data : s.getChatData()) {
                mAdapter.addData(data);
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
