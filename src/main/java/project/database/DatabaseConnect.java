package project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnect {

    private final String url = "jdbc:sqlite:C:\\Users\\ROG\\IdeaProjects\\group-project-luck-ou-talent\\src\\main\\java\\project\\database\\testingdb.db";
   public final Connection connection;
    public DatabaseConnect() throws SQLException {
        connection = DriverManager.getConnection(url);
    }
}

