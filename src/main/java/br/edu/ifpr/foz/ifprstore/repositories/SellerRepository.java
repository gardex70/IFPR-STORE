package br.edu.ifpr.foz.ifprstore.repositories;

import br.edu.ifpr.foz.ifprstore.connection.ConnectionFactory;
import br.edu.ifpr.foz.ifprstore.exceptions.DatabaseException;
import br.edu.ifpr.foz.ifprstore.models.Department;
import br.edu.ifpr.foz.ifprstore.models.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerRepository {

    private Connection connection;

    public SellerRepository() {

        connection = ConnectionFactory.getConnection();

    }

    public List<Seller> getSellers() {

        List<Seller> sellers = new ArrayList<>();

        try {
            if(connection.isClosed() || connection == null) {
                connection = ConnectionFactory.getConnection();
            }

            Statement statement = connection.createStatement();
            String query = "SELECT s.*, d.Name AS DepName, d.Id AS DepartmentId " +
                           "FROM seller s " +
                           "JOIN department d on s.DepartmentId = d.Id";
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                Department department = instantiateDepartment(result);
                Seller seller = instantiateSeller(result, department);

                sellers.add(seller);

            }

            result.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            ConnectionFactory.closeConnection();
        }


        return sellers;
    }

    public Seller insert(Seller seller) {

        String sql = "INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                "VALUES(?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, seller.getName());
            statement.setString(2, seller.getEmail());
            statement.setDate(3, Date.valueOf(seller.getBirthDate()));
            statement.setDouble(4, seller.getBaseSalary());
            statement.setInt(5, 1);

            Integer rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {

                ResultSet id = statement.getGeneratedKeys();

                id.next();

                Integer sellerId = id.getInt(1);

                System.out.println("Rows inserted: " + rowsInserted);
                System.out.println("Id: " + sellerId);

                seller.setId(sellerId);

            }


        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }

        return seller;
    }

    public void updateSalary(Integer departmentId, Double bonus) {

        String sql = "UPDATE seller SET BaseSalary = BaseSalary + ? WHERE DepartmentId = ?";

        try {

            PreparedStatement statement = connection.prepareStatement(sql);//crl+alt+v
            statement.setDouble(1, bonus);
            statement.setInt(2, departmentId);

            Integer rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Rows updated: " + rowsUpdated);
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }

    }

    public void delete(Integer id) {

        String sql = "DELETE FROM seller WHERE Id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, id);
            Integer rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Rows deleted: " + rowsDeleted);
            }

        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    public Seller getById(Integer id) {

        Seller seller;
        Department department;

        String sql = "SELECT seller.*,department.Name as DepName " +
                "FROM seller " +
                "INNER JOIN department " +
                "ON seller.DepartmentId = department.Id " +
                "WHERE seller.Id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                department = this.instantiateDepartment(resultSet);
                seller = this.instantiateSeller(resultSet, department);

            } else {
                throw new DatabaseException("Vendedor não encontrado");
            }

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }

        return seller;
    }

    public void update(Seller seller) {

        String sql = "UPDATE seller SET " +
                "Name = ?, " +
                "Email = ?, " +
                "BirthDate = ?, " +
                "BaseSalary = ?, " +
                "DepartmentId = ? " +
                "WHERE (seller.Id = ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, seller.getName());
            statement.setString(2, seller.getEmail());
            statement.setDate(3, Date.valueOf(seller.getBirthDate()));
            statement.setDouble(4, seller.getBaseSalary());
            statement.setInt(5, seller.getDepartment().getId());
            statement.setInt(6, seller.getId());

            int rowsAffected = statement.executeUpdate();

            System.out.println("Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }

    }

    public List<Seller> findByDepartment(Integer id) {

        List<Seller> sellersList = new ArrayList<>();

        Seller seller;
        Department department;

        String sql = "SELECT seller.*,department.Name as DepName " +
                "FROM seller INNER JOIN department " +
                "ON seller.DepartmentId = department.Id " +
                "WHERE DepartmentId = ? " +
                "ORDER BY Name";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            Map<Integer, Department> map = new HashMap<>();

            while (resultSet.next()) {

                department = map.get(resultSet.getInt("DepartmentId"));

                if (department == null) {
                    department = instantiateDepartment(resultSet);
                    map.put(resultSet.getInt("DepartmentId"), department);
                }

                seller = this.instantiateSeller(resultSet, department);

                sellersList.add(seller);
            }

        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }

        return sellersList;

    }

    public Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {

        Seller seller = new Seller();

        seller.setId(resultSet.getInt("Id"));
        seller.setName(resultSet.getString("Name"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setBirthDate(resultSet.getDate("BirthDate").toLocalDate());
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
        seller.setDepartment(department);

        return seller;
    }

    public Department instantiateDepartment(ResultSet resultSet) throws SQLException {

        Department department = new Department();

        department.setId(resultSet.getInt("DepartmentId"));
        department.setName(resultSet.getString("DepName"));

        return department;
    }

}
