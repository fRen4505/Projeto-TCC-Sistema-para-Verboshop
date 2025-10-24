package projeto.Interfaces;

import java.sql.SQLException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import projeto.Sessao;
import projeto.System.AdminDAO;
import projeto.System.Models.User;
import projeto.System.Models.valores.Permissoes;

public class UserPane {
    
    @FXML
    private Pane usrPane;

    @FXML
    private Button alterar, deletar;

    @FXML
    private Label usrNom = new Label("Nome: ");
    @FXML
    private Label usrMail = new Label("Email: ");
    @FXML
    private Label usrFunc = new Label("Função: ");
    @FXML
    private Label usrID = new Label("ID: ");

    private AdminDAO dao = (AdminDAO)Sessao.getDAO();
    private User usr;

    public UserPane(){}

    public Pane painel(User insUsr){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/UserPane.fxml"));
            Pane tela = loader.load();

            UserPane controller = loader.getController();

            if (insUsr.getFunção() == Permissoes.CLIENTE) {
                controller.alterar.setVisible(false);
            }

            controller.usrNom.setText(usrNom.getText() + insUsr.getNome());
            controller.usrMail.setText(usrMail.getText() + insUsr.getEmail());
            controller.usrFunc.setText(usrFunc.getText() + insUsr.getFunção().getPermissaoNome());
            controller.usrID.setText(usrID.getText() + insUsr.getID().toString());

            controller.usr = insUsr;
            controller.dao = (AdminDAO) Sessao.getDAO();
            
            return tela;

        } catch (Exception e) {
            e.printStackTrace();    
            // TODO Auto-generated catch block
        }

        return this.usrPane;
    }

    
    public void userDelete(ActionEvent e){
        try {
            if(  this.usr.getID().compareTo(Sessao.getUser().getID()) != 0  ) {
                this.dao.deletarUser(this.usr.getID());
            }else{
                // TODO Auto-generated catch block
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
            // TODO Auto-generated catch block
        }
    }

    public void userAlterar(ActionEvent e){
        try {
            AlterarUser dialog = new AlterarUser(this.usr);
            Optional<User> alterado = dialog.showAndWait();

            try {
                this.dao.alterarUser(this.usr.getID(), alterado.get());
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            // TODO: handle exception
        }
    }


}
