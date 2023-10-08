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

/**
 * <h2>Servlet che controlla se c'è una sessione attiva</h2>
 * Il risultato json darà not logged </b> o
 * <b>logged</b>, in base al caso .
 */
@WebServlet(name = "checkSession", value = "/checkSession")
public class CheckSession extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        System.out.println("Servlet called  : CheckSession");


        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        HashMap<String, Object> jsonResp = new HashMap<>();

        //Recupero sessione
        HttpSession session = req.getSession(false);

        //Controllo se la sessione esiste
        System.out.println("Checking session...");
        if (session == null){
            System.out.println("There was no active session.");
            jsonResp.put("result", ServerResponse.NOT_LOGGED.toString()); //non loggato
        }
        else {
            jsonResp.put("result", ServerResponse.LOGGED.toString()); //altrimenti loggato
            System.out.println("Id session: " + session.getId());
        }
        System.out.println();

        String g = new Gson().toJson(jsonResp);
        out.println(g);
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        doGet(req, resp);
    }
}
