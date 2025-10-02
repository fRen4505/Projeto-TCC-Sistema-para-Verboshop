package projeto.Interfaces;

import java.sql.SQLException;
import java.util.UUID;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import projeto.System.AdminDAO;
import projeto.System.DAO;
import projeto.System.Models.User;

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

    private UUID id;
    private AdminDAO dao;

    public UserPane(){}

    public Pane painel(User insUsr, DAO insDao){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/UserPane.fxml"));
            Pane tela = loader.load();

            UserPane controller = loader.getController();

            controller.usrNom.setText(usrNom.getText() + insUsr.getNome());
            controller.usrMail.setText(usrMail.getText() + insUsr.getEmail());
            controller.usrFunc.setText(usrFunc.getText() + insUsr.getFunção().getPermissaoNome());
            controller.usrID.setText(usrID.getText() + insUsr.getID().toString());

            controller.id = UUID.fromString(insUsr.getID().toString());
            controller.dao = (AdminDAO) insDao;
            
            return tela;

        } catch (Exception e) {
            e.printStackTrace();        
        }

        return this.usrPane;
    }

    
    public void userDelete(ActionEvent e){
        try {
            this.dao.deletarUser(id);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public void userAlterar(ActionEvent e){

    }


}
