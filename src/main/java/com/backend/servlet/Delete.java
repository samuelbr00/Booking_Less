package com.backend.servlet;

import com.backend.model.LJT;
import com.backend.utilities.GenericUtils;
import com.backend.utilities.ServerResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * <h2>Servlet che cancella righe dalla tabella .</h2>
 */
@WebServlet(name = "delete", value = "/delete")
public class Delete extends ConnectedServlet{

    private static boolean checkNotInLesson(String conditions, Object... values){


        String checkQuery = "select * from ripetizione where status = 'attiva' " +
                "and " + conditions;
        System.out.println("Query: " + checkQuery);

        //Run query
        List<Object> checks = dao.exc_select(checkQuery, LJT.class, values);
        System.out.println("Lesson found for him: " + checks);

        if (checks == null) {
            return true;
        }
        return checks.isEmpty();
    }

    private static void deleteRow(String sql, Object... params){

        //Check valori null
        if (Arrays.stream(params).anyMatch(param ->
                (param == null) || param.equals(""))){
            jsonResp.put("result", "params_null");
            System.out.println("The following params are null " +
                    "or empty: " + Arrays.toString(params));
        }
        else{
            //Check risultato query
            int res = dao.exc_delete(sql,params);
            System.out.println("Delete query result: " + res);
            if(res != 1){
                jsonResp.put("result", ServerResponse.QUERY_FAILED.toString());
            }
            else{
                jsonResp.put("result", ServerResponse.SUCCESS.toString());
                jsonResp.put("objDeleted", params);
                System.out.println("Delete successful with query :" + sql + " and params ");
                GenericUtils.print(params);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws  IOException {
        System.out.println("Servlet called  : Delete");
        //Init the HashMap for the json response
        jsonResp = new HashMap<>();

        //Retrieving session to check privileges
        HttpSession session = req.getSession(false);
        String objType = checkPrivileges(req, session,
                new String[]{"utente", "corso", "docente", "affiliazione"},
                new String[]{"utente", "corso", "docente", "affiliazione"},
                false);

        String teacher, course;
        System.out.println("Objtype to be deleted: " + objType);
        switch (objType) {

            case "utente":
                String account = req.getParameter("account");
                deleteRow("delete from utente where account=?", account);
                break;

            case "corso":
                course = req.getParameter("title");
                if (checkNotInLesson("course=?", course)) {
                    deleteRow("delete from corso where title=?", course);
                }
                else {
                    System.out.println("Course still in an active lesson");
                    jsonResp.put("result", ServerResponse.STILL_IN_LESSONS.toString());
                }
                break;

            case "docente":
                teacher = req.getParameter("id_number");
                if (checkNotInLesson("teacher=?", teacher)) {
                    deleteRow("delete from docente where id_number=?", teacher);
                }
                else {
                    System.out.println("Teacher still in an active lesson");
                    jsonResp.put("result", ServerResponse.STILL_IN_LESSONS.toString());
                }
                break;

            case "affiliazione":
                teacher = req.getParameter("teacher");
                course = req.getParameter("course");

                if (checkNotInLesson("teacher=? and course=?",
                        teacher, course)) {
                    deleteRow("delete from affiliazione where course_title" +
                            "=? and teacher_id=?", course, teacher);
                }
                else {
                    System.out.println("Affiliation still in an active lesson");
                    jsonResp.put("result", ServerResponse.STILL_IN_LESSONS.toString());
                }
                break;

            default:
                System.out.println("Invalid objType, or it's null");
        }

        System.out.println();
        sendJson(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        doGet(req, resp);
    }
}
