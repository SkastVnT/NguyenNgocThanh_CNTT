/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author PHT
 */
public class Helper {
    public static void showError(HttpServletRequest request, HttpServletResponse response, String errorMessage) 
            throws ServletException, IOException{
        request.setAttribute("errorMessage", errorMessage);
        //Hien trang /error/index
        request.setAttribute("controller", "error");
        request.setAttribute("action", "index");
        request.getRequestDispatcher("/WEB-INF/layouts/main.jsp").forward(request, response);
    }
}
