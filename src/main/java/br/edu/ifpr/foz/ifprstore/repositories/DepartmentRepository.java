package br.edu.ifpr.foz.ifprstore.repositories;

import br.edu.ifpr.foz.ifprstore.connection.ConnectionFactory;
import br.edu.ifpr.foz.ifprstore.exceptions.DatabaseException;
import br.edu.ifpr.foz.ifprstore.exceptions.DatabaseIntegrityException;
import br.edu.ifpr.foz.ifprstore.models.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentRepository {

    Connection connection;

    public DepartmentRepository(){
        connection = ConnectionFactory.getConnection();
    }

    public List<Department> getAll() {

        List<Department> departments = new ArrayList<>();

        String sql = "SELECT * FROM department";

        try {

            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {

                Department department = new Department();
                department.setId(result.getInt("Id"));
                department.setName(result.getString("Name"));
                departments.add(department);
            }


        }catch (SQLException e){
            throw new DatabaseException(e.getMessage());
        }

        return departments;

    }

    public void delete(int id){

        String sql = "DELETE FROM department WHERE Id = ?";

        try {

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();

            if(rowsDeleted > 0){
                System.out.println("Rows deleted: " + rowsDeleted);
            }

        } catch (Exception e){
            throw new DatabaseIntegrityException(e.getMessage());
        }

    }
}
