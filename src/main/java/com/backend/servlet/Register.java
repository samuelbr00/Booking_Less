package com.backend.servlet;

import com.backend.model.User;
import com.backend.utilities.Crypt;
import com.backend.utilities.GenericUtils;
import com.backend.utilities.ServerResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



@WebServlet(name = "register", value = "/register")
    public class Register extends ConnectedServlet {

    Connection con ;
    PreparedStatement pst ;
    PreparedStatement pst1 ;
    protected void DoPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {


        try {
            resp.setContentType("text/html");
            HttpSession session = req.getSession();
            session.getId();
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:8889/lesson_booking","root","root");
            String account = req.getParameter("account");
            String password = Crypt.encryptMD5(req.getParameter("password"));
            String role = req.getParameter("role");
            PrintWriter out = resp.getWriter();
            if (account == "") {
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Il campo account non può essere vuoto, riprova.');");
                out.println("window.location = 'registration.html'");
                out.println("</script>");
            } else {

                pst = con.prepareStatement("insert into utente(account  , password , role)values(?,?,?) ");
                pst.setString(1, account);
                pst.setString(2, password);
                pst.setString(3, "utente");
                pst.executeUpdate();

                out.println("<script type=\"text/javascript\">");
                out.println("alert('Benvenuto nel nostro sito, accedi per essere reindirizzato alla homepage ');");
                out.println("window.location = 'index.html'");
                out.println("</script>");



            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
















    /*    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        System.out.println("Servlet called  : Register ");

        HttpSession session = req.getSession();
        String e=req.getParameter("username");
        String c=req.getParameter("password");
        String n=req.getParameter("role");
        try{

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:8889/lesson_booking","root","root");

            PreparedStatement ps=con.prepareStatement(
                    "insert into user values(?,?,user,)");

            ps.setString(1,e);
            ps.setString(2,c);
            ps.setString(3,n);




        int i=ps.executeUpdate();
        if(i>0)
            out.print("You are successfully registered...");


    }catch (Exception e2) {System.out.println(e2);}

out.close();
}



    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
            doPost(req, resp);
        } */



