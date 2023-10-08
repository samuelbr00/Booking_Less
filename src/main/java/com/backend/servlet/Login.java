package com.backend.servlet;

import com.backend.model.User;
import com.backend.utilities.Crypt;
import com.backend.utilities.ServerResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/*
Questa servlet si occupa del login/accesso di un utente in modalità normale o modalità guest,
 e creare una relativa sessione
Se un utente entra da guest
può svolgere azioni limitate, altrimenti
 può essere amministratore oppure utente.
Se una persona ha già effettuato
il login standard, non si possono effettuare altri login,
mentre se un utente entra come
ospite può ovviamente entrare con un nuovo login
 */
@WebServlet(name = "login", value = "/login")
public class Login extends ConnectedServlet {

    private static void setGuest(HttpSession session){
        Object[] credentials = new Object[]{session.getAttribute("account"),
                session.getAttribute("role")};

        if (!session.isNew() && !Arrays.stream(credentials).allMatch(Objects::isNull)) {
            jsonResp.put("result", ServerResponse.ALREADY_LOGGED.toString()); //già collegato
        }
        else{
            // creo sessione
            session.setMaxInactiveInterval(100);
            System.out.println("Id session (login): " + session.getId());
            session.setAttribute("role", "guest");
            jsonResp.put("result", ServerResponse.SUCCESS.toString()); //log avvenuto con successo
            jsonResp.put("user", new User("ospite"));

            System.out.println("Guest login succesful");
        }
    }

    private static void setUser(HttpSession session, HttpServletRequest req){
        //Recupero dati dal login
        String account = req.getParameter("account");
        String password = req.getParameter("password");
        System.out.println("Credentials: " + account + "(account) "
                + password + "(password)");
        //verifico se una sessione (o un altro user) esiste già
        String role = (String) session.getAttribute("role");
        if (!session.isNew() && role != null && !role.equals("guest")){
            jsonResp.put("result", ServerResponse.ALREADY_LOGGED.toString());
        }
       //  controllo check credenziali
        else if ( account == null || password == null ||
                account.equals("") || password.equals("")){
            session.invalidate();
            jsonResp.put("result", ServerResponse.ILLEGAL_CREDENTIALS.toString());
        }
        //Faccio la run della query per il login qundi select dati da tabella User
        else {

            List<?> l = dao.exc_select("select account,role " +
                            "from utente where account = ? " +
                            "and password = ?", User.class, account,
                    Crypt.encryptMD5(password).toUpperCase());

            if (l == null || l.isEmpty()){
                session.invalidate();
                jsonResp.put("result", ServerResponse.INVALID_CREDENTIALS.toString());
            }
            else {
                User userLogged = (User) l.get(0);
                session.setMaxInactiveInterval(100);
                jsonResp.put("result", ServerResponse.SUCCESS.toString());
                jsonResp.put("user", userLogged);

                //creo la Sessione
                System.out.println("Id session (login): " + session.getId());
                session.setAttribute("account", account);
                session.setAttribute("role", userLogged.getRole());

                System.out.println("Login done with user: " + userLogged);
            }
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        System.out.println("Servlet called  : Login ");
        //recupera oggetto sessione
        HttpSession session = req.getSession();

        //Hashmap per rappresentare la risposta json
        jsonResp = new HashMap<>();

        // azione (auth/ospte)
        String action = req.getParameter("action");
        System.out.println("Type of login action: " + action);

        if (action == null ||
                !Arrays.asList(new String[]{"guest", "auth"}).contains(action)) {
            session.invalidate();
            System.out.println("Invalid action");
            jsonResp.put("result", ServerResponse.INVALID_ACTION.toString());
        }
        //Guest login caso
        else if (action.equals("guest")){
            setGuest(session);
        }
        //User login caso
        else{
            setUser(session, req);
        }

        //Log stampa e invia json risposta
        System.out.println(
                jsonResp.get("result").equals(ServerResponse.SUCCESS.toString()) ?
                "Login succesful! (Role: " + session.getAttribute("role") + ")"
                : "Invalid login, something went wrong.");
        System.out.println();
        sendJson(resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        doPost(req, resp);
    }
}
