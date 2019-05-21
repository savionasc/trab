package com.dekoservidoni.firebasechat;

/**
 * Class responsible to register all the callbacks necessary
 * for the application
 */
public interface ActivityCallback {
    void openCreateAccount();
    void abrirChat(String sala);
    void openPrincipal();
    void openCriarSalaFragment();
    void openChat();
    void logout();
}
