/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import cart.Cart;
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
@WebServlet(name = "CartController", urlPatterns = {"/cart/*"})
public class CartController extends HttpServlet {

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

        switch (action) {
            case "index":
                //cho hien main layout
                request.getRequestDispatcher("/WEB-INF/layouts/main.jsp").forward(request, response);
                break;
            case "add":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    //tao doi tuong session
                    HttpSession session = request.getSession();
                    //lay cart tu session
                    Cart cart = (Cart) session.getAttribute("cart");
                    //neu trong session chua co cart thi tao cart moi va luu cart
                    //vao session
                    if (cart == null) {
                        cart = new Cart();
                        session.setAttribute("cart", cart);
                    }
                    //them item vao cart
                    cart.add(id, 1);
                    //cho hien lai trang /home/index
                    request.getRequestDispatcher("/home/index").forward(request, response);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Helper.showError(request, response, ex.getMessage());
                }
                break;
            case "delete":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    //tao doi tuong session
                    HttpSession session = request.getSession();
                    //lay cart tu session
                    Cart cart = (Cart) session.getAttribute("cart");
                    //them item vao cart
                    cart.remove(id);
                    //cho hien lai trang /cart/index
                    request.getRequestDispatcher("/cart/index").forward(request, response);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Helper.showError(request, response, ex.getMessage());
                }
                break;
            case "update":
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    int quantity = Integer.parseInt(request.getParameter("quantity"));
                    //tao doi tuong session
                    HttpSession session = request.getSession();
                    //lay cart tu session
                    Cart cart = (Cart) session.getAttribute("cart");
                    //them item vao cart
                    cart.update(id, quantity);
                    //cho hien lai trang /cart/index
                    request.getRequestDispatcher("/cart/index").forward(request, response);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Helper.showError(request, response, ex.getMessage());
                }
                break;
            case "empty":
                try {
                    //tao doi tuong session
                    HttpSession session = request.getSession();
                    //lay cart tu session
                    Cart cart = (Cart) session.getAttribute("cart");
                    //empty cart
                    cart.empty();
                    //cho hien lai trang /cart/index
                    request.getRequestDispatcher("/cart/index").forward(request, response);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Helper.showError(request, response, ex.getMessage());
                }
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
