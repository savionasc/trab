package com.dekoservidoni.firebasechat;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import com.dekoservidoni.firebasechat.fragments.ChatFragment;
import com.dekoservidoni.firebasechat.fragments.CreateAccountFragment;
import com.dekoservidoni.firebasechat.fragments.CriarSalaFragment;
import com.dekoservidoni.firebasechat.fragments.LoginFragment;
import com.dekoservidoni.firebasechat.fragments.PrincFragment;
import com.dekoservidoni.firebasechat.fragments.PrincipalFragment;

public class MainActivity extends AppCompatActivity implements ActivityCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, LoginFragment.newInstance())
                .commit();
    }


    @Override
    public void openChat() {
        replaceFragment(ChatFragment.newInstance());
    }

    @Override
    public void openCreateAccount() {
        replaceFragment(CreateAccountFragment.newInstance());
    }

    @Override
    public void abrirChat(String sala) {
        Bundle b = new Bundle();
        b.putString("sala", sala);
        ChatFragment chatSala = new ChatFragment();
        chatSala.setArguments(b);
        replaceFragment(chatSala);
    }

    @Override
    public void openPrincipal() { replaceFragment(PrincFragment.newInstance()); }

    @Override
    public void openCriarSalaFragment() { replaceFragment(CriarSalaFragment.newInstance()); }

    @Override
    public void logout() {
        replaceFragment(LoginFragment.newInstance());
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}