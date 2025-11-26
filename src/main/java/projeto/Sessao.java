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

//Classe focada em definir a sessão atual e seu usuario loggado, afim de que todas as classes necessarias possam ter acesso a estes dados 
//e evitar as complicações dos ciclos de vida de alguns componentes e classes, especialmente os do JavaFX
public class Sessao {
    
    private static User loggado;

    //Tipo de DAO relacionado a perfil de usuario, somente utilizavel após usuario loggado existir (login bem sucedido)
    private static PerfilDAO loggadoDAO;

    private static final Logger log = LoggerFactory.getLogger(Sessao.class);

    //Metodo para a definição do usuario loggado da atual sessão, 
    //tambem servindo para a inserir os emails dos administradores no sistema de envio de relatorio por email
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

    //Metodo para acessar o DAO relacionado com o nivel do usuario loggado
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

    //Metodo para acessar o usuario loggado
    public static User getUser(){
        return loggado;
    }

    //Metodo para terminar sessão
    public static void deLog(){
        loggadoDAO.closeConneccao();
        loggado = null;
        log.error("Sistema fechado");
    }

}
