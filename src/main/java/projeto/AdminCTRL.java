package projeto;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;
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
                
    private List<User> usuarios;
    private List<Livro> livros;
    private List<Pedido> pedidos;
    
    public AdminCTRL(User usrIns, Stage insTela){

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
         
            }catch (NullPointerException e) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Falha ao iniciar tela \nMotivo: "+e.getMessage(),
                    "Erro", 
                    0
                ); 
            }
        }else{
            this.stage.close();
        }

    }

    //===============================METODOS PARA GUI===============================

    public void initADMtela(ActionEvent e){
        
        try {
            this.loggedUser = Sessao.getUser();
            this.dao = (AdminDAO) Sessao.getDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/AdminGUI.fxml"));
            loader.setController(this);
            Parent tela = loader.load();                
                
            Stage currStage = (Stage)((Node) e.getSource()).getScene().getWindow();

            this.userName.setText(this.userName.getText() + loggedUser.getNome());
            this.stage.setTitle("Gerenciamento");

            Scene cena = new Scene(tela);
            currStage.setScene(cena);
            currStage.show();

        }catch (IOException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao iniciar tela \nMotivo: "+e1.getMessage(),
                "Erro", 
                0
            ); 
        }
    }

    public void sairTela(){
        Sessao.deLog();
        this.loggedUser = null;
        this.stage.close();
    }

    //===============================METODOS PARA USUARIOS===============================

    public void usersTela(ActionEvent e){
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/AdminUserGUI.fxml"));
            loader.setController(this);
            Parent tela = loader.load();

            this.stage.setTitle("Gerenciamento de usuarios");

            int adms = 0;
            int usrs = 0;
            int cliens = 0;

            usuarios = Sessao.getDAO().getUsers();
            for (User usr : usuarios) {
                if (usr.getFunção() == Permissoes.ADMINISTRADOR) {
                    adms++;
                }if (usr.getFunção() == Permissoes.USUARIO) {
                    usrs++;
                }if (usr.getFunção() == Permissoes.CLIENTE){
                    cliens++;
                }

                Pane usrPane = new UserPane().painel(usr);

                users.getChildren().add(usrPane);
            }

            total.setText(total.getText() + usuarios.size());
            admins.setText(admins.getText() + adms);
            usurs.setText(usurs.getText() + usrs);
            clientes.setText(clientes.getText() + cliens);

            Scene cena = new Scene(tela);
            this.stage = (Stage)((Node)e.getSource()).getScene().getWindow(); 
            this.stage.setScene(cena);
            this.stage.show();

        }catch (SQLException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao adquirir dados \nMotivo: "+e1.getMessage(),
                "Erro", 
                0
            ); 
        }catch (IOException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao iniciar tela \nMotivo: "+e1.getMessage(),
                "Erro", 
                0
            ); 
        }
    }

    public void userNovo(ActionEvent e){

        Optional<User> cadastrado = new CadastroUser().showAndWait();
        
        cadastrado.ifPresent(novo -> {
            try {
                Sessao.getDAO().adicionarUser(novo);
                this.usersTela(e);
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Falha a cadastrar usuario \nMotivo: "+e1.getMessage(),
                    "Erro", 
                    0
                ); 
            }catch (NullPointerException e1) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Falha a cadastrar usuario por falta de dados",
                    "Erro", 
                    0
                ); 
            }
        });
            
    }

    //===============================METODOS PARA LIVROS===============================

    public void livrosTela(ActionEvent e){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/AdminLivroGUI.fxml"));
            loader.setController(this);
            Parent tela = loader.load();

            this.stage.setTitle("Gerenciamento de livros");

            int val = 0;
            int total = 0;

            livros = Sessao.getDAO().getLivros();
            for (Livro liv : livros) {
                if(liv.getQuantidade() == 0){
                    val = val + 1;
                }
                total = total + liv.getQuantidade(); 

                Pane libPane = new LivroPane().painel(liv);

                livList.getChildren().add(libPane);
            }

            totalLivros.setText(totalLivros.getText() + total);
            livroQtnd.setText(livroQtnd.getText() + livros.size());
            livrosNone.setText(livrosNone.getText() + val);

            Scene cena = new Scene(tela);
            this.stage = (Stage)((Node)e.getSource()).getScene().getWindow(); 
            this.stage.setScene(cena);
            this.stage.show();

        }catch (SQLException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao adquirir dados \nMotivo: "+e1.getMessage(),
                "Erro", 
                0
            ); 
        }catch (IOException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao iniciar tela \nMotivo: "+e1.getMessage(),
                "Erro", 
                0
            ); 
        }
    }

    public void livroNovo(ActionEvent e){

        Optional<Livro> cadastro = new CadastrarLivro().showAndWait();

        cadastro.ifPresent(novo -> {
            try {
                this.dao.adicionarLivro(novo);
                this.livrosTela(e);
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Falha a cadastrar livro \nMotivo: "+e1.getMessage(),
                    "Erro", 
                    0
                ); 
            }catch (NullPointerException e1) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Falha a cadastrar livro por falta de dados",
                    "Erro", 
                    0
                ); 
            }catch (IllegalArgumentException e1) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Falha a cadastrar livro por dados errados \nMotivo: "+e1.getMessage(),
                    "Erro", 
                    0
                ); 
            }
        });
        
    }

    //===============================METODOS PARA PEDIDOS===============================

    public void pedidosTela(ActionEvent e){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/AdminPedidoGUI.fxml"));
            loader.setController(this);
            Parent tela = loader.load();

            this.stage.setTitle("Gerenciamento de pedidos");

            int pix = 0;
            int cartao = 0;
            int dinheiro = 0;

            //pedidoList.setSpacing(27.5);

            pedidos = Sessao.getDAO().getPedidos();
            for (Pedido ped : pedidos) {
                if (ped.getEntregue() == 1) {
                    Sessao.getDAO().entreguePedido(ped.getIDpedido());
                }else{
                    if (ped.getPagamento() == Pagamentos.DINHEIRO) {
                        dinheiro = dinheiro + 1;
                    }
                    if (ped.getPagamento() == Pagamentos.PIX) {
                        pix = pix + 1;
                    }
                    if (ped.getPagamento() == Pagamentos.CREDITO || ped.getPagamento() == Pagamentos.DEBITO) {
                        cartao = cartao + 1;
                    }
                    pedidoList.getChildren().add( new PedidoPane().painel(ped) );
                }
            }
            
            qtndPedido.setText(qtndPedido.getText()+pedidos.size());
            pedidoDinher.setText(pedidoDinher.getText()+dinheiro);
            pedidoPIX.setText(pedidoPIX.getText() + pix);
            pedidoCartao.setText(pedidoCartao.getText() + cartao);

            Scene cena = new Scene(tela);
            this.stage = (Stage)((Node)e.getSource()).getScene().getWindow(); 
            this.stage.setScene(cena);
            this.stage.show();

        }catch (SQLException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao adquirir dados \nMotivo: "+e1.getMessage(),
                "Erro", 
                0
            ); 
        }catch (IOException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao iniciar tela \nMotivo: "+e1.getMessage(),
                "Erro", 
                0
            ); 
        }

    }

    public void pedidoNovo(ActionEvent e){
        
        try {
            Optional<Pedido> cadastro = new CadastroPedido(
                Sessao.getDAO().getLivros(),
                Sessao.getDAO().getUsers()
            ).showAndWait();
            
            cadastro.ifPresent(novo ->{
                try {
                    Sessao.getDAO().criarPedido(novo);
                    this.pedidosTela(e);
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Falha a cadastrar pedido \nMotivo: "+e1.getMessage(),
                        "Erro", 
                        0
                    ); 
                }
            });
       
        } catch (SQLException e2) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha a iniciar cadastro de pedido por problema no banco \nMotivo: "+e2.getMessage(),
                "Erro", 
                0
            ); 
        }catch (NullPointerException e2) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha a iniciar cadastro de pedido por problema nos dados \nMotivo: "+e2.getMessage(),
                "Erro", 
                0
            ); 
        }

    }


}
