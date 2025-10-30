package projeto.Interfaces;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import javax.swing.JOptionPane;

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
            loader.setController(this);
            Pane tela = loader.load();

            if (insUsr.getFunção() == Permissoes.CLIENTE) {
                alterar.setVisible(false);
            }

            usrNom.setText(usrNom.getText() + insUsr.getNome());
            usrMail.setText(usrMail.getText() + insUsr.getEmail());
            usrFunc.setText(usrFunc.getText() + insUsr.getFunção().getPermissaoNome());
            usrID.setText(usrID.getText() + insUsr.getID().toString());

            usr = insUsr;
            dao = (AdminDAO) Sessao.getDAO();
            
            return tela;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro ao acessar dados \n motivo "+e.getMessage(),
                "erro", 
                0
            );
            return this.usrPane;
        }
    }

    
    public void userDelete(ActionEvent e){
        try {
            if (this.usr.getID().compareTo(Sessao.getUser().getID()) != 0 ) {
                String[] vals = {"sim", "não"};
                String opt = (String)JOptionPane.showInputDialog(null, 
                    "Deseja mesmo excluir?",
                    "Deletar", 
                    2, 
                    null, 
                    vals,vals[1]
                );
                if( opt == "sim") {
                    this.dao.deletarUser(this.usr.getID());
                }
            }else{
                JOptionPane.showMessageDialog(
                    null, 
                    "Não é permitido um usuario excluir o proprio cadastro desta forma",
                    "Erro", 
                    2
                );
            }

        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro por utilização em pedido, exclusão cancelada \n motivo: "+e1.getMessage() ,
                "Erro", 
                0
            );
        }
    }

    public void userAlterar(ActionEvent e){
        try {
            if( this.usr.getID().compareTo(Sessao.getUser().getID()) != 0 ) {
                AlterarUser dialog = new AlterarUser(this.usr);
                Optional<User> alterado = dialog.showAndWait();

                this.dao.alterarUser(this.usr.getID(), alterado.get());
            }else{
                JOptionPane.showMessageDialog(
                    null, 
                    "Não é permitido um usuario excluir o proprio cadastro desta forma",
                    "Erro", 
                    2
                );
            }
        } catch (SQLException e2) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro por falta de dados, alteração cancelada \n motivo: "+e2.getMessage() ,
                "Erro", 
                0
            ); 
        }catch (NullPointerException e3) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro por falta de dados, alteração cancelada \n motivo: "+e3.getMessage() ,
                "Erro", 
                0
            ); 
        }
    }


}
