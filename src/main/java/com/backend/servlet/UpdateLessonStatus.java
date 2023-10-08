package com.backend.servlet;

import com.backend.model.Lesson;
import com.backend.utilities.GenericUtils;
import com.backend.utilities.ServerResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

@WebServlet(name = "updateLessonStatus", value = "/updateLessonStatus")
public class UpdateLessonStatus extends ConnectedServlet{

    private static void doUpdateLessonStatus(Lesson l, String newStatus){

        String[] params = new String[]{l.getTeacher(), l.getT_slot(), l.getDay(), l.getUser()};
        System.out.println("Lesson to be updated: " + Arrays.toString(params));

        if (Arrays.stream(params).anyMatch(attr -> attr == null ||
                attr.equals(""))) {
            jsonResp.put("result", ServerResponse.ILLEGAL_PARAMS.toString());
        }
        else{

            int updateResult = dao.exc_update("update ripetizione set status = ? " +
                    "where teacher = ? and t_slot = ? and day = ? and user = ?", newStatus,
                    l.getTeacher(), l.getT_slot(), l.getDay(), l.getUser());

            if (updateResult != 1){
                jsonResp.put("result", ServerResponse.QUERY_FAILED.toString());
            }
            else {
                jsonResp.put("result", ServerResponse.SUCCESS.toString());
                jsonResp.put("lessonUpdated", l);
                jsonResp.put("newStatus", newStatus);
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        System.out.println("Servlet called  : updateLessonStatus ");
        //Init the HashMap for the json response
        jsonResp = new HashMap<>();

        //Retrieving session to check privileges
        HttpSession session = req.getSession(false);
        String objType = checkPrivileges(req, session,
                new String[]{"ripetizione"},
                new String[]{},
                false);

        if (!objType.equals("")) {
            //Getting the new status and check it
            String newStatus = req.getParameter("newStatus");
            if (!GenericUtils.getLessonStatus().
                    contains(newStatus)){
                jsonResp.put("result", ServerResponse.INVALID_NEW_STATUS.toString());
            }
            else{
                //Do the lesson update status
                Lesson l = new Lesson(req.getParameter("teacher"), req.getParameter("t_slot"),
                        req.getParameter("day"), req.getParameter("user"));
                doUpdateLessonStatus(l, newStatus);
            }

        }

        //Log print and send json response
        System.out.println(
                jsonResp.get("result").equals(ServerResponse.SUCCESS.toString()) ?
                "Lesson updated with status: " + req.getParameter("newStatus")
                : "Lesson update went wrong.");
        System.out.println();
        sendJson(resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        doPost(req, resp);
    }
}
