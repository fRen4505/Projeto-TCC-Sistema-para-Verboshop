package projeto;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import projeto.Interfaces.CadastrarLivro;
import projeto.Interfaces.CadastroPedido;
import projeto.Interfaces.CadastroUser;
import projeto.Interfaces.LivroPane;
import projeto.Interfaces.PedidoPane;
import projeto.Interfaces.UserPane;
import projeto.System.AdminDAO;
import projeto.System.Models.User;
import projeto.System.Models.valores.Pagamentos;
import projeto.System.Models.valores.Permissoes;
import projeto.System.Models.Livro;
import projeto.System.Models.Pedido;

public class AdminCTRL {

    private Stage stage;

    @FXML
    private Button usersPage, livrosPage, pedidosPage, novoUser, novoLivro, novoPedido, voltar, sair;
    @FXML
    private Label userName = new Label();
    @FXML
    private VBox users, livList;
    @FXML
    private VBox pedidoList;

    @FXML
    private Label total = new Label("Total perfis: ");
    @FXML
    private Label admins = new Label("Administradores: ");
    @FXML
    private Label usurs = new Label("Usuarios: ");
    @FXML
    private Label clientes = new Label("Clientes: ");

    @FXML
    private Label livroQtnd = new Label("Livros: ");
    @FXML
    private Label totalLivros = new Label("Total de livros: ");
    @FXML
    private Label livrosNone = new Label("Livros sem quantidade no estoque: ");

    @FXML
    private Label pedidoCartao = new Label("Pedidos com cartão: ");
    @FXML
    private Label pedidoDinher = new Label("Pedidos com dinheiro: ");
    @FXML
    private Label pedidoPIX = new Label("Pedidos com PIX: ");
    @FXML
    private Label qtndPedido = new Label("Pedidos: ");

    
    private User loggedUser;
    private AdminDAO dao;

    private Alert alert = new Alert(Alert.AlertType.ERROR);
                
    private List<User> usuarios;
    private List<Livro> livros;
    private List<Pedido> pedidos;
    
    public AdminCTRL(){}

    public void tela(User usrIns, Stage insTela){

        if (usrIns == null) {
            usrIns = Sessao.getUser();
        }

        if (usrIns.getFunção() == Permissoes.ADMINISTRADOR) {

            try {
                this.stage = insTela;
                this.loggedUser = usrIns;
                this.dao = (AdminDAO) Sessao.getDAO();

                this.userName.setText(this.userName.getText() + loggedUser.getNome());
                this.stage.setTitle("Gerenciamento");
         
            } catch (Exception e) {
                e.printStackTrace();
                // TODO Auto-generated catch block
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

            control.tela(Sessao.getUser(), currStage);
            this.dao = (AdminDAO) Sessao.getDAO();

            Scene cena = new Scene(tela);
            currStage.setScene(cena);
            currStage.show();

        } catch (Exception e1) {
            e1.printStackTrace();
            // TODO Auto-generated catch block
        }
    }

    public void sairTela(){
        Sessao.deLog();
        //usuarios.clear();
        //livros.clear();
        //pedidos.clear();
        this.loggedUser = null;
        this.stage.close();
    }

    //===============================METODOS PARA USUARIOS===============================

    public void usersTela(ActionEvent e){
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/AdminUserGUI.fxml"));
            Parent tela = loader.load();
            AdminCTRL ctrl = loader.getController();
            this.stage.setTitle("Gerenciamento de usuarios");

            Stage currStage = (Stage)((Node) e.getSource()).getScene().getWindow();
            
            ctrl.tela(Sessao.getUser(), currStage);
            this.dao = (AdminDAO) Sessao.getDAO();

            int adms = 0;
            int usrs = 0;
            int cliens = 0;

            usuarios = ctrl.dao.getUsers();
            for (User usr : usuarios) {
                if (usr.getFunção() == Permissoes.ADMINISTRADOR) {
                    adms++;
                }if (usr.getFunção() == Permissoes.USUARIO) {
                    usrs++;
                }if (usr.getFunção() == Permissoes.CLIENTE){
                    cliens++;
                }

                UserPane pane = new UserPane();
                Pane usrPane = pane.painel(usr);

                ctrl.users.getChildren().add(usrPane);
            }

            ctrl.total.setText(ctrl.total.getText() + usuarios.size());
            ctrl.admins.setText(ctrl.admins.getText() + adms);
            ctrl.usurs.setText(ctrl.usurs.getText() + usrs);
            ctrl.clientes.setText(ctrl.clientes.getText() + cliens);

            Scene cena = new Scene(tela);
            ctrl.stage = (Stage)((Node)e.getSource()).getScene().getWindow(); 
            ctrl.stage.setScene(cena);
            ctrl.stage.show();

        } catch (Exception e1) {
            e1.printStackTrace();
            // TODO Auto-generated catch block
        }
    }


    public void userNovo(ActionEvent e){

        CadastroUser dialog = new CadastroUser();
        Optional<User> cadastrado = dialog.showAndWait();
        
        cadastrado.ifPresent(novo -> {
            try {
                this.dao.adicionarUser(novo);
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
            
    }

    //===============================METODOS PARA LIVROS===============================

    public void livrosTela(ActionEvent e){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/AdminLivroGUI.fxml"));
            Parent tela = loader.load();
            AdminCTRL ctrl = loader.getController();
            this.stage.setTitle("Gerenciamento de livros");

            Stage currStage = (Stage)((Node) e.getSource()).getScene().getWindow();
            
            ctrl.tela(Sessao.getUser(), currStage);
            this.dao = (AdminDAO) Sessao.getDAO();

            int val = 0;
            int total = 0;

            livros = ctrl.dao.getLivros();
            for (Livro liv : livros) {
                if(liv.getQuantidade() == 0){
                    val = val + 1;
                }
                total = total + liv.getQuantidade(); 

                LivroPane pane = new LivroPane();
                Pane libPane = pane.painel(liv);

                ctrl.livList.getChildren().add(libPane);
            }

            ctrl.totalLivros.setText(ctrl.totalLivros.getText() + total);
            ctrl.livroQtnd.setText(ctrl.livroQtnd.getText() + livros.size());
            ctrl.livrosNone.setText(ctrl.livrosNone.getText() + val);

            Scene cena = new Scene(tela);
            ctrl.stage = (Stage)((Node)e.getSource()).getScene().getWindow(); 
            ctrl.stage.setScene(cena);
            ctrl.stage.show();

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void livroNovo(ActionEvent e){

        CadastrarLivro dialog = new CadastrarLivro();
        Optional<Livro> cadastro = dialog.showAndWait();

        cadastro.ifPresent(novo -> {
            try {
                this.dao.adicionarLivro(novo);
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        
    }

    //===============================METODOS PARA PEDIDOS===============================

    public void pedidosTela(ActionEvent e){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/AdminPedidoGUI.fxml"));
            Parent tela = loader.load();
            AdminCTRL ctrl = loader.getController();

            this.stage.setTitle("Gerenciamento de pedidos");

            Stage currStage = (Stage)((Node) e.getSource()).getScene().getWindow();
            
            ctrl.tela(Sessao.getUser(), currStage);
            this.dao = (AdminDAO) Sessao.getDAO();

            int pix = 0;
            int cartao = 0;
            int dinheiro = 0;

            ctrl.pedidoList.setSpacing(27.5);

            pedidos = ctrl.dao.getPedidos();
            for (Pedido ped : pedidos) {
                if (ped.getPagamento() == Pagamentos.DINHEIRO) {
                    dinheiro = dinheiro + 1;
                }
                if (ped.getPagamento() == Pagamentos.PIX) {
                    pix = pix + 1;
                }
                if (ped.getPagamento() == Pagamentos.CREDITO || ped.getPagamento() == Pagamentos.DEBITO) {
                    cartao = cartao + 1;
                }
                PedidoPane pediInfo = new PedidoPane();
                Pane pedidoPane = pediInfo.painel(ped);

                ctrl.pedidoList.getChildren().add(pedidoPane);
            }
            ctrl.qtndPedido.setText(ctrl.qtndPedido.getText()+pedidos.size());
            ctrl.pedidoDinher.setText(ctrl.pedidoDinher.getText()+dinheiro);
            ctrl.pedidoPIX.setText(ctrl.pedidoPIX.getText() + pix);
            ctrl.pedidoCartao.setText(ctrl.pedidoCartao.getText() + cartao);

            Scene cena = new Scene(tela);
            ctrl.stage = (Stage)((Node)e.getSource()).getScene().getWindow(); 
            ctrl.stage.setScene(cena);
            ctrl.stage.show();

        } catch (Exception e2) {
            e2.printStackTrace();
            // TODO: handle exception
        }

    }

    public void pedidoNovo(ActionEvent e){
        
        try {
            Optional<Pedido> cadastro = new CadastroPedido(
                this.dao.getLivros(),
                this.dao.getUsers()
            ).showAndWait();
            
            cadastro.ifPresent(novo ->{
                try {
                    this.dao.criarPedido(novo);
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            });
       
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }


}
