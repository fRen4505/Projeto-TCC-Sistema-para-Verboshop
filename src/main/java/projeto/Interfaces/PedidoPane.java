package projeto.Interfaces;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import projeto.Sessao;
import projeto.System.PerfilDAO;
import projeto.System.Models.Livro;
import projeto.System.Models.Pedido;
import projeto.System.Models.valores.Dinheiro;
import projeto.System.Models.valores.Permissoes;

public class PedidoPane {
    
    @FXML
    private Pane pedPane = new Pane();

    @FXML
    private Button entregue = new Button("Entregar"), cancelar;
    
    @FXML
    private Label client = new Label("Cliente: ");
    @FXML
    private Label idPedi = new Label("ID do pedido: ");
    @FXML
    private Label creator = new Label("Atendente: ");
    @FXML
    private Label data = new Label("Data: ");
    @FXML
    private Label pagaMeto = new Label("Pagamento: ");
    @FXML
    private Label totalVal = new Label("Total: ");

    private PerfilDAO dao = Sessao.getDAO();
    private Pedido insPedido;
    private Pane painel;
    
    @FXML
    private ListView<String> encoList = new ListView<>();
    private HashMap<String, Integer> encoVals = new HashMap<>();

    public PedidoPane(Pedido insPed){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIs/PedidoPane.fxml"));
            loader.setController(this);
            Pane tela = loader.load();

            if (Sessao.getUser().getFunção() != Permissoes.ADMINISTRADOR) {
                creator.setVisible(false);
            }
            if (insPed.getEntregue() == 1) {
                entregue.setText("Entregue");
                entregue.setDisable(true);
                entregue.setStyle("-fx-background-color: #0fa600");
            }

            LocalDateTime dataCriacao = insPed.getDataCriação();

            client.setText(client.getText() + insPed.getCliente().getNome());
            idPedi.setText(idPedi.getText() + insPed.getIDpedido().toString());
            creator.setText(creator.getText() + insPed.getCriador().getNome());
            data.setText(data.getText() + 
                dataCriacao.getDayOfMonth() 
                + "/" + dataCriacao.getMonthValue() 
                + "/" + dataCriacao.getYear()
            );
            pagaMeto.setText(pagaMeto.getText() + insPed.getPagamento().name());

            Double precoGeral = 0.0;
            for (int l = 0; l < insPed.getEncomendas().size(); l++) {

                Livro liv = insPed.getEncomendas().get(l);
                
                precoGeral += liv.getPreço().getQuantiaDouble();

                if (encoVals.containsKey(liv.getTitulo())) {
                    encoVals.put(
                        liv.getTitulo(), 
                        (encoVals.get( liv.getTitulo() ) + 1)
                    );
                }else{
                    encoVals.putIfAbsent(liv.getTitulo(), 1);
                }
            }
            totalVal.setText(totalVal.getText() + new Dinheiro( precoGeral).valor());

            encoVals.forEach((liv, quan) -> {
                encoList.getItems().add( "Livros: " + liv + "\tQuantidades: " + quan );
            });   
            
            insPedido = insPed;

            this.painel = pedPane;

        }catch (IOException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro ao acessar dados \n motivo "+e1.getMessage(),
                "erro", 
                0
            );  
        }
    }

    public Pane painel(){
        if (painel != null) {
            return painel;
        } else {
            return null;
        }
    }

    public void pedidoEntregue(ActionEvent e){
        try {
            String[] vals = {"sim", "não"};
            String opt = (String)JOptionPane.showInputDialog(null, 
                "Deseja mesmo confirmar a entrega?",
                "Entregar", 
                2, 
                null, 
                vals,vals[1]
            );
            if(opt == "sim") {
                dao.entreguePedido(insPedido.getIDpedido());

                entregue.setText("Entregue");
                entregue.setDisable(true);
                entregue.setStyle("-fx-background-color: #0fa600");
            }
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro ao confirmar entrega, cancelado \nMotivo: "+e1.getMessage(),
                "Erro", 
                0
            );
        }
        

    }

    public void pedidoCancelar(ActionEvent e){
        try {
            String[] vals = {"sim", "não"};
            String opt = (String)JOptionPane.showInputDialog(null, 
                "Deseja mesmo cancelar o pedido?",
                "Cancelar", 
                2, 
                null, 
                vals,vals[1]
            );
            if(opt.equals("sim")) {
                this.dao.cancelarPedido(insPedido.getIDpedido());    
            }
            
        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(
                null, 
                "Erro, exclusão cancelada \n motivo: "+e1.getMessage(),
                "Erro", 
                0
            );}
        }      
    }
 

