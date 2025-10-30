package projeto.System.Database;

import java.sql.Statement;
import java.io.IOException;
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

            statement.execute("PRAGMA foreign_keys = ON;");
            statement.setQueryTimeout(30);
        
            String SQLfile = SQLreader.carregarArquivo("/descricao.sql"); 
            statement.executeUpdate(SQLfile);
            
        } catch (SQLException e) {
            //TODO catch
            //throw new SQLException("erro na criação do banco");   
        } catch (IOException e) {
            //TODO catch
            //throw new IOException("erro ao ler arquivo");   
        }
    }

    public Connection getConnection(){
        return this.connection;
    }

    public void closeConnection(){
        try {
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
