package com.backend.servlet;

import com.backend.utilities.ServerResponse;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


/* Questa servlet si occupa del logout che un utente può fare
e consiste nell'invalidare, se era già aperta, la sua sessione */
@WebServlet(name = "logout", value = "/logout")
public class Logout extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        System.out.println("Servlet called  : Logout");

        //setto HTTP response
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        /*
          Setting hashmap per i risultato che scriverl in json.
         */
        HashMap<String, Object> jsonResp = new HashMap<>();

        //recupero sessione
        HttpSession session = req.getSession(false);

        //vede se la ssione esiste
        if (session == null){
            System.out.println("There was no active session.");
            jsonResp.put("result", ServerResponse.NO_USER.toString());
        }
        else { //se non è null , quindi attiva , la invalido
            String username = (String) session.getAttribute("account");
            session.invalidate();
            jsonResp.put("result", ServerResponse.SUCCESS.toString());
            jsonResp.put("accountLoggedOut", username);
           // stampo id della sessione invalidata
            System.out.println("Id session (logout): " + session.getId());
        }

        String g = new Gson().toJson(jsonResp);
        out.println(g);
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        doGet(req,resp);
    }
}
