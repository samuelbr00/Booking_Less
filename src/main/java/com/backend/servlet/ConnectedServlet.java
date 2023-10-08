package com.backend.servlet;

import com.backend.DAO.DAO;
import com.backend.utilities.ServerResponse;
import com.google.gson.Gson;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;


/**
 * <h2>
 *     Abstract class for all the servlet that requires to
 *     connect to the database.
 * </h2>
 */
abstract class ConnectedServlet extends HttpServlet {

    //DAO object
    protected static DAO dao;
    protected static HashMap<String, Object> jsonResp;

    public void init(ServletConfig conf) throws ServletException {
        super.init(conf);

        //Setting DAO
        ServletContext ctx = conf.getServletContext();
        String url = ctx.getInitParameter("url");
        String user = ctx.getInitParameter("user");
        String pass = ctx.getInitParameter("password");
        dao = new DAO(url, user, pass);
    }

    /**
     * controllo privilegi , admin , utente , guest
     *
     */
    protected String checkPrivileges(HttpServletRequest req, HttpSession session,
                                     String[] allowedObj, String[] adminObj,
                                     boolean guestAllowed){

        String objType = "";


        if (session == null){
            System.out.println("There's no user.");
            jsonResp.put("result", ServerResponse.NO_USER.toString());
        }
        else {
            //recupera il ruolo dell'utente dalla sessione
            String sessionRole = (String) session.getAttribute("role");
            //recupera oggeto che vogliamo inserire
            objType = req.getParameter("objType");

            System.out.println("Role of the user that request the action: " + sessionRole);
            //Check correct obj name
            if (!Arrays.asList(allowedObj).contains(objType)){
                jsonResp.put("result", ServerResponse.INVALID_OBJECT.toString());
                objType = "";
            }
            //Check if this method can be done by a guest session
            else if (!guestAllowed && sessionRole.equals("guest")){
                jsonResp.put("result", ServerResponse.NOT_ALLOWED.toString());
                objType = "";
            }
            //For user/course/teacher/affiliation insertion, check role priviligies
            else if (Arrays.asList(adminObj).contains(objType) &&
                    !sessionRole.equals("amministratore")){
                jsonResp.put("result", ServerResponse.NOT_ALLOWED.toString());
                objType = "";
            }
        }

        return objType;
    }


    protected void sendJson(HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");

        out.println(new Gson().toJson(jsonResp));
        out.flush();
        out.close();
    }
}
