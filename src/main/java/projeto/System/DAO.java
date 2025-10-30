package projeto.System;

import java.sql.Connection;
import projeto.System.Database.DataBase;

public abstract class DAO {
    
    private Connection conneccao;

    public DAO (){
        this.conneccao = DataBase.getInstance().getConnection();
    }
   
    public Connection getConneccao() {
        return conneccao;
    }

    public void closeConneccao(){
        DataBase.getInstance().closeConnection();
    }


}
