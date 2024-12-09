/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import db.Account;
import db.AccountFacade;
import db.Product;
import db.ProductFacade;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author PHT
 */
@WebServlet(name = "AccountController", urlPatterns = {"/account/*"})
public class AccountController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String servletPath = request.getServletPath();
        String url = request.getRequestURI();

        String controller = servletPath.substring(1);
        int n1 = url.lastIndexOf("/") + 1;
        int n2 = url.lastIndexOf(";");
        String action = n2 == -1 ? url.substring(n1) : url.substring(n1, n2);
        //luu controller va action vao request
        request.setAttribute("controller", controller);
        request.setAttribute("action", action);
        
        switch(action){
            case "login":
                //hien form login
                request.getRequestDispatcher("/WEB-INF/layouts/main.jsp").forward(request, response);
                break;
            case "login_handler":
                //xu ly form login
                try {
                    String email = request.getParameter("email");
                    String password = request.getParameter("password");
                    AccountFacade af = new AccountFacade();
                    Account account = af.login(email, password);
                    if(account == null){
                        //neu login khong thanh cong
                        request.setAttribute("errorMessage", "Please check your email or password!");
                        //cho hien lai trang login
                        request.getRequestDispatcher("/account/login").forward(request, response);
                    }else{
                        //neu login thanh cong
                        //luu account vao session
                        HttpSession session = request.getSession();
                        session.setAttribute("account", account);
                        //cho hien trang home
                        request.getRequestDispatcher("/home/index").forward(request, response);
                    }                    
                }catch(Exception ex){
                    ex.printStackTrace();
                    Helper.showError(request, response, ex.getMessage());
                }                
                break;
            case "logout":
                //xoa session
                HttpSession session = request.getSession();
                session.invalidate();
                //cho hien home page
                request.getRequestDispatcher("/home/index").forward(request, response);
                break;
            case "register":
                //hien form register
                request.getRequestDispatcher("/WEB-INF/layouts/main.jsp").forward(request, response);
                break;
            case "register_handler":
                //xu ly form register
                break;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
