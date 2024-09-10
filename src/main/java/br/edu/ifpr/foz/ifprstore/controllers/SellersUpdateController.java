package br.edu.ifpr.foz.ifprstore.controllers;


import br.edu.ifpr.foz.ifprstore.models.Department;
import br.edu.ifpr.foz.ifprstore.models.Seller;
import br.edu.ifpr.foz.ifprstore.repositories.DepartmentRepository;
import br.edu.ifpr.foz.ifprstore.repositories.SellerRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/sellers/update")
public class SellersUpdateController extends HttpServlet {

    SellerRepository repository = new SellerRepository();
    DepartmentRepository departmentRepository = new DepartmentRepository();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Integer id =  Integer.valueOf(req.getParameter("id"));

        List<Department> departments = departmentRepository.getAll();
        req.setAttribute("departments", departments);

        Seller seller = repository.getById(id);
        req.setAttribute("seller", seller);

        RequestDispatcher dispatcher = req.getRequestDispatcher("/sellers-update.jsp");
        dispatcher.forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Integer id = Integer.valueOf(req.getParameter("field_id"));
        String name = req.getParameter("field_name");
        String email = req.getParameter("field_email");
        LocalDate birthDate = LocalDate.parse(req.getParameter("field_birthDate"));
        Double baseSalary = Double.valueOf(req.getParameter("field_baseSalary"));
        Integer departmentId = Integer.valueOf(req.getParameter("field_department"));

        Department department = new Department();
        department.setId(departmentId);

        Seller seller = new Seller();

        seller.setId(id);
        seller.setName(name);
        seller.setEmail(email);
        seller.setBirthDate(birthDate);
        seller.setBaseSalary(baseSalary);
        seller.setDepartment(department);

        repository.update(seller);

        resp.sendRedirect(req.getContextPath() + "/sellers");

    }
}