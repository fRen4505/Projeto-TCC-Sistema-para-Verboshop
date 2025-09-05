package projeto.System.Database;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import projeto.Main;

public class DataBase {
    
    private static DataBase INSTANCE;

    private Connection connection;

    private DataBase(){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite::resource:" + Main.class.getResource("/banco.db"));

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
        
            String SQLfile = SQLreader.carregarArquivo("/descricao.sql"); 
            statement.executeUpdate(SQLfile);
            
        } catch (Exception e) {
            System.out.println("erro na criação do banco");
            e.printStackTrace(System.err);
        }
    }

    public Connection getConnection(){
        return this.connection;
    }

    public void closeConnection(){
        try {
            this.connection.commit();
            this.connection.close();
        } catch (SQLException e) {
            System.out.println("erro para fechar banco");
            e.printStackTrace();
        }
    }

    public static DataBase getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new DataBase();
            return INSTANCE;
        } else {
            return INSTANCE;
        }
    }

}
