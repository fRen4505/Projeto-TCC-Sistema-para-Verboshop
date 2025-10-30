package projeto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projeto.Interfaces.CadastroPedido;
import projeto.Interfaces.LivroPane;
import projeto.Interfaces.NovoClientePane;
import projeto.Interfaces.NovoLotePane;
import projeto.Interfaces.PedidoPane;
import projeto.System.UserDAO;
import projeto.System.Models.Livro;
import projeto.System.Models.Pedido;
import projeto.System.Models.User;
import projeto.System.Models.valores.Pagamentos;
import projeto.System.Models.valores.Permissoes;

public class UserCTRL {
    
    private Stage stage = new Stage();

    @FXML
    private Button livrosPage, pedidosPage, voltar, sair, novoPedido, novoCliente, novoLote;
    @FXML
    private RadioButton insAdmin, insUser, insClient;
    @FXML
    private Button alterar, deletar;
    @FXML
    private Label userName = new Label();
    @FXML
    private VBox livList, pedList; //= new VBox();

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

    @FXML
    private ListView<String> clientList;

    private User loggedUser;
    private UserDAO dao;

    private List<Livro> livros;
    private List<Pedido> pedidos;
    private List<User> usuarios;

    public UserCTRL(User usrIns, Stage insTela){

        if (usrIns == null) {
            usrIns = Sessao.getUser();
        }

        if (usrIns.getFunção() == Permissoes.USUARIO) {
            try {
                this.stage = insTela;
                this.loggedUser = usrIns;
                this.dao = (UserDAO) Sessao.getDAO();

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

    public void initUSRtela(ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/UserGUI.fxml"));
            loader.setController(this);
            Parent tela = loader.load();                
                
            Stage currStage = (Stage)((Node) e.getSource()).getScene().getWindow();

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

    //===============================METODOS PARA LIVROS===============================
    
    public void livrosTela(ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/UserLivroGUI.fxml"));
            loader.setController(this);
            Parent tela = loader.load();
            
            this.stage.setTitle("Gerenciamento de livros");
                        
            int val = 0;
            int total = 0;

            livros = Sessao.getDAO().getLivros();
            for (Livro livro : livros) {
                if(livro.getQuantidade() == 0){
                    val = val + 1;
                }
                total = total + livro.getQuantidade(); 

                Pane livPane = new LivroPane().painel(livro);

                livList.getChildren().add(livPane);
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

    public void novoLote(ActionEvent e){

        try {
            Optional<ArrayList<Livro>> lote = new NovoLotePane(
                this.dao.getLivros()
            ).showAndWait();

            lote.ifPresent(loteContent ->{
                for (Livro loteLivro : loteContent) {
                    try {
                        this.dao.alterarLivro(loteLivro.getISBN(), loteLivro);
                        this.livrosTela(e);
                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(
                            null, 
                            "Falha a cadastrar novo lote \nMotivo: "+e1.getMessage(),
                            "Erro", 
                            0
                        ); 
                    }
                }
            });

        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Falha ao iniciar cadastro de lote \nMotivo: "+e1.getMessage(),
                "Erro", 
                0
            ); 
        }

    }

    //===============================METODOS PARA PEDIDOS===============================

    public void pedidosTela(ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/UserPedidoGUI.fxml"));
            loader.setController(this);
            Parent tela = loader.load();
            
            this.stage.setTitle("Gerenciamento de pedidos");
            
            int pix = 0;
            int cartao = 0;
            int dinheiro = 0;

            pedList.setSpacing(27.5);

            pedidos = Sessao.getDAO().getPedidos();
            for (Pedido pedido : pedidos) {
                if (pedido.getPagamento() == Pagamentos.DINHEIRO) {
                    dinheiro = dinheiro + 1;
                }
                if (pedido.getPagamento() == Pagamentos.PIX) {
                    pix = pix + 1;
                }
                if (pedido.getPagamento() == Pagamentos.CREDITO || pedido.getPagamento() == Pagamentos.DEBITO) {
                    cartao = cartao + 1;
                }

                Pane pedPane = new PedidoPane().painel(pedido);

                pedList.getChildren().add(pedPane);
            }

            qtndPedido.setText(qtndPedido.getText()+pedidos.size());
            pedidoDinher.setText(pedidoDinher.getText()+dinheiro);
            pedidoPIX.setText(pedidoPIX.getText() + pix);
            pedidoCartao.setText(pedidoCartao.getText() + cartao);

            usuarios = Sessao.getDAO().getUsers();
            for (User user : usuarios) {
                if (user.getFunção() == Permissoes.CLIENTE) {
                    clientList.getItems().add("Nome: " + user.getNome() );
                }
            }

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
                this.dao.getLivros(),
                this.dao.getUsers()
            ).showAndWait();
            
            cadastro.ifPresent(novo ->{
                try {
                    this.dao.criarPedido(novo);
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
                "Falha a iniciar cadastro de pedido \nMotivo: "+e2.getMessage(),
                "Erro", 
                0
            ); 
        }
    }

    public void clienteNovo(ActionEvent e){

        Optional<User> cadastrado = new NovoClientePane().showAndWait();
        
        cadastrado.ifPresent(novo -> {
            try {
                this.dao.adicionarUser(novo);
                this.pedidosTela(e);
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Falha a cadastrar cliente \nMotivo: "+e1.getMessage(),
                    "Erro", 
                    0
                ); 
            }
        });
        
    }
    
}
