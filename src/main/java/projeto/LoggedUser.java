package projeto;

import projeto.System.Models.User;

public class LoggedUser {
    
    private static User loggado;

    public static void setLoggado(User insUsr){
        loggado = insUsr;
    }

    public static User getUser(){
        return loggado;
    }

    public static void deLog(){
        loggado = null;
    }

}
