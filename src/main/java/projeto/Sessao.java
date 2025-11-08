package projeto;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import projeto.System.AdminDAO;
import projeto.System.PerfilDAO;
import projeto.System.UserDAO;
import projeto.System.Models.User;
import projeto.System.Models.valores.Permissoes;

public class Sessao {
    
    private static User loggado;
    private static PerfilDAO loggadoDAO;
    private static final Logger log = LoggerFactory.getLogger(Sessao.class);

    public static void setLoggado(User insUsr){
        loggado = new User(
            insUsr.getNome(),
            insUsr.getEmail(),
            insUsr.getID().toString(),
            insUsr.getFunção()
        );

        try {
            for (User admin : getDAO().getUsers()) {
                if (admin.getFunção() == Permissoes.ADMINISTRADOR) {
                    MDC.put("email", admin.getEmail());
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        log.info("Usuario: "+ loggado.getNome() +" entrou");

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
        log.error("Sistema fechado");
    }

}
