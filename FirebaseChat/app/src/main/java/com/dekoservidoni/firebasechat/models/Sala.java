package com.dekoservidoni.firebasechat.models;

import java.util.ArrayList;

public class Sala {
    String nome;
    String numUsuarios;
    ArrayList<ChatData> chatData;

    public Sala() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumUsuarios() {
        return numUsuarios;
    }

    public void setNumUsuarios(String numUsuarios) {
        this.numUsuarios = numUsuarios;
    }

    public ArrayList<ChatData> getChatData() { return chatData; }

    public void setChatData(ArrayList<ChatData> chatData) { this.chatData = chatData; }
}
