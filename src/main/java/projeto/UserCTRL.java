package projeto;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;
import projeto.System.AdminDAO;
import projeto.System.UserDAO;
import projeto.System.Models.Livro;
import projeto.System.Models.Pedido;
import projeto.System.Models.User;
import projeto.System.Models.valores.Permissoes;

public class UserCTRL {
    
    private Stage stage = new Stage();

    @FXML
    private Button livrosPage, pedidosPage, voltar, sair;
    @FXML
    private RadioButton insAdmin, insUser, insClient;
    @FXML
    private Button alterar, deletar;
    @FXML
    private Label userName;

    
    private User loggedUser;
    private UserDAO dao;

    private List<Livro> livros;
    private List<Pedido> pedidos;

    public UserCTRL(){}

    public void tela(User usrIns, Stage insTela){

        if (usrIns.getFunção() == Permissoes.USUARIO) {
            try {
                this.stage = insTela;
                this.loggedUser = usrIns;
                this.dao = UserDAO.getInstancia(loggedUser);

                this.userName.setText(this.userName.getText() + loggedUser.getNome());
                this.stage.setTitle("Gerenciamento");
                Parent tela = FXMLLoader.load(getClass().getResource("/GUIs/UserGUI.fxml"));
                Scene cena = new Scene(tela);
                this.stage.setScene(cena);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            this.stage.close();
        }
    }

    
    //===============================METODOS PARA GUI===============================

    public void initUSRtela(ActionEvent e){
        try {
            Parent tela = FXMLLoader.load(getClass().getResource("/GUIs/UserGUI.fxml"));
            Scene cena = new Scene(tela);
            this.stage = (Stage)((Node)e.getSource()).getScene().getWindow(); 
            this.stage.setScene(cena);
            this.stage.show();

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    
    public void livrosTela(ActionEvent e){
        try {
            Parent tela = FXMLLoader.load(getClass().getResource("/GUIs/UserLivroGUI.fxml"));
            Scene cena = new Scene(tela);
            this.stage = (Stage)((Node)e.getSource()).getScene().getWindow(); 
            this.stage.setScene(cena);
            this.stage.show();

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void pedidosTela(ActionEvent e){

    }

    public void sairTela(){
        //this.stage.close();
        this.loggedUser = null;
    }


    //===============================METODOS PARA PEDIDOS===============================

    public void pedidoNovo(ActionEvent e){

    }

    public void pedidoDeletar(ActionEvent e){

    }

    public void pedidoAlterar(ActionEvent e){

    }
    
}
