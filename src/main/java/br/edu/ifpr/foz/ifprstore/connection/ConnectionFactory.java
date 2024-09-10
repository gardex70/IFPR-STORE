package br.edu.ifpr.foz.ifprstore.connection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static Connection connection;
    
    public static Connection getConnection(){

        String url = "jdbc:mysql://127.0.0.1:3306/ifpr_store";
        String user = "root";
        String pass = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, pass);

        } catch (SQLException e){
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {  // Verifica se a conexão não é null
            try {
                if (!connection.isClosed()) {  // Verifica se a conexão ainda está aberta
                    connection.close();
                    System.out.println("Conexão fechada com sucesso.");
                }
            } catch (SQLException e) {
                System.out.println("Não foi possível encerrar a conexão: " + e.getMessage());
            }
        } else {
            System.out.println("A conexão já estava fechada ou não foi inicializada.");
        }
    }

}
