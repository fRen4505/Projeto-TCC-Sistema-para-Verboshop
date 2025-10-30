package projeto;

import projeto.System.AdminDAO;
import projeto.System.PerfilDAO;
import projeto.System.UserDAO;
import projeto.System.Models.User;

public class Sessao {
    
    private static User loggado;
    private static PerfilDAO loggadoDAO;

    public static void setLoggado(User insUsr){
        loggado = new User(
            insUsr.getNome(),
            insUsr.getEmail(),
            insUsr.getID().toString(),
            insUsr.getFunção()
        );
    }

    public static PerfilDAO getDAO(){

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
        loggadoDAO.closeConneccao();
        loggado = null;
    }

}
