package projeto;

import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.util.List;
import projeto.System.AdminDAO;
import projeto.System.Models.User;
import projeto.System.Models.valores.Permissoes;
import projeto.System.Models.Livro;
import projeto.System.Models.Pedido;

public class AdminCTRL {

    private Stage stage;

    @FXML
    private Button usersPage, livrosPage, pedidosPage, novoUser, novoLivro, voltar, sair;
    @FXML
    private TextField insNome, insMail, insTitulo, insAutor, insEdit, insCode, insPreco  ;
    @FXML
    private RadioButton insAdmin, insUser, insClient;
    @FXML
    private Button alterar, deletar;
    @FXML
    private Label userName = new Label();


    private User loggedUser;
    private AdminDAO dao;

    private List<User> usuarios;
    private List<Livro> livros;
    private List<Pedido> pedidos;
    
    public AdminCTRL(){}

    //public void setLoggedUser(User insLog) {
    //    this.loggedUser = insLog;
    //}

    public void tela(User usrIns, Stage insTela ){

        if (usrIns == null) {
            usrIns = LoggedUser.getUser();
        }

        if (usrIns.getFunção() == Permissoes.ADMINISTRADOR) {

            try {
                this.stage = insTela;
                this.loggedUser = usrIns;
                this.dao = AdminDAO.getInstancia(this.loggedUser);

                this.userName.setText(this.userName.getText() + loggedUser.getNome());

                //this.stage.setScene(cena);
                //this.stage.setTitle("Gerenciamento");

                //FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/AdminGUI.fxml"));
                //Parent tela = loader.load();                
                
                //AdminCTRL control = loader.getController();
                //control.setLoggedUser(usrIns);

                //Scene cena = new Scene(tela);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }if (insTela == null) {
            System.out.println("tela é nula");
        }else{
            this.stage.close();
        }

    }

    //===============================METODOS PARA GUI===============================

    public void initADMtela(ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/AdminGUI.fxml"));
            Parent tela = loader.load();                
                
            AdminCTRL control = loader.getController();

            Stage currStage = (Stage)((Node) e.getSource()).getScene().getWindow();

            control.tela(LoggedUser.getUser(), currStage);

            Scene cena = new Scene(tela);
            currStage.setScene(cena);
            currStage.show();
            //this.stage = (Stage)((Node)e.getSource()).getScene().getWindow(); 
            //this.stage.setScene(cena);
            //this.stage.show();

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void usersTela(ActionEvent e){
        try {
            Parent tela = FXMLLoader.load(getClass().getResource("/GUIs/AdminUserGUI.fxml"));
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
            Parent tela = FXMLLoader.load(getClass().getResource("/GUIs/AdminLivroGUI.fxml"));
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
        LoggedUser.deLog();
        this.loggedUser = null;
        this.stage.close();
    }

    public void controle(){

        /*
        try {
                
            dao = AdminDAO.getInstancia(loggedUser);

            String input = "";

            while (input != "s") {
                
                System.out.println("\t \t ->>Insira o que deseja realizar<<- \n \tl.gerenciar Livros \tu.gerenciar Usuarios \tp.gerenciar Pedidos");
                input = scan.nextLine();

                switch (input) {
                    case "l":

                        String livInput = "";
                        
                        List<Livro> livrosCadastrados = dao.getLivros();

                        System.out.println("Livros: \n=================================================================================");
                        for (Livro livro : livrosCadastrados) {
                            if (livro != null) {
                                int i = 0;
                                System.out.println(
                                    "Numero: " + i +
                                    "\nLivro: " + livro.getTitulo() + " | Editora: "+ livro.getEditora()+ " | Autor" +livro.getAutor()+
                                    "\nISBN: " + livro.getISBN() + " | Quantidade: "+ livro.getQuantidade() + " | Preço: " + livro.getPreço()+
                                    "\n================================================================================="
                                );
                                i = i + 1;   
                            }if (livrosCadastrados.isEmpty()) {
                                System.out.println("Não há livros");
                                break;
                            }
                        }

                        int ins;
                        System.out.println("\t \t ->>Digite qual opcao deseja<<- \n \td.Deletar livro \tm.Modificar livro \ta.Adicionar livro");
                        livInput = scan.nextLine();
                        switch (livInput) {
                            case "d":
                                System.out.println("digite o numero do livro que se deseja deletar:");
                                ins = scan.nextInt();

                                dao.deletarLivro(livrosCadastrados.get(ins).getISBN());
                                break;
                        
                            case "m":
                                System.out.println("digite o numero do livro que se deseja alterar:");
                                ins = scan.nextInt();

                                System.out.println("insira novo titulo: ");

                                System.out.println("digite novo autor: ");

                                System.out.println("");


                                break;

                            case "a":

                                break;
                        }

                        break;
                    case "u":

                        break;
                    case "p":

                        break;
                    default:
                        break;
                }


            }

        */
    
    }

    //===============================METODOS PARA USUARIOS===============================

    public void userDelete(ActionEvent e){

    }

    public void userAlterar(ActionEvent e){

    }

    public void userNovo(ActionEvent e){

    }

    //===============================METODOS PARA LIVROS===============================

    public void livroDelete(ActionEvent e){

    }

    public void livroAlterar(ActionEvent e){

    }

    public void livroNovo(ActionEvent e){
        
    }

    //===============================METODOS PARA PEDIDOS===============================

    public void pedidoNovo(ActionEvent e){

    }

    public void pedidoDeletar(ActionEvent e){

    }

    public void pedidoAlterar(ActionEvent e){

    }


}
