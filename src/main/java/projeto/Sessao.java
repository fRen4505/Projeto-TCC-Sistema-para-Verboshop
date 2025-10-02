package projeto;

import projeto.System.AdminDAO;
import projeto.System.DAO;
import projeto.System.UserDAO;
import projeto.System.Models.User;

public class Sessao {
    
    private static User loggado;
    private static DAO loggadoDAO;

    public static void setLoggado(User insUsr){
        loggado = insUsr;
    }

    public static DAO getDAO(){

        switch (loggado.getFunção()) {
            case ADMINISTRADOR:
                loggadoDAO = AdminDAO.getInstancia(loggado);
                return loggadoDAO;

            case USUARIO:
                loggadoDAO = UserDAO.getInstancia(loggado);
                return loggadoDAO;

            default:
                return null;
        }

    }

    public static User getUser(){
        return loggado;
    }

    public static void deLog(){
        loggado = null;
    }

}
