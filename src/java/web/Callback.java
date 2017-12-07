package web;

import beans.TwitterUser;
import classes.TwitterConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class Callback extends HttpServlet {

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
        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
        String verifier = request.getParameter("oauth_verifier");
        try {
            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
            request.getSession().removeAttribute("requestToken");
            TwitterUser user = TwitterUser.findUserById(TwitterConnection.getConnection(), accessToken.getUserId());
            if(user == null) {
                user = new TwitterUser(accessToken.getUserId(), accessToken.getScreenName(),
                        accessToken.getToken(), accessToken.getTokenSecret());
                TwitterUser.addUser(TwitterConnection.getConnection(), user);
            } else {
                user.setAccessToken(accessToken.getToken());
                user.setAccessTokenSecret(accessToken.getTokenSecret());
                TwitterUser.update(TwitterConnection.getConnection(), user);
            }
            ServletOutputStream outputStream = response.getOutputStream();
            request.getSession().setAttribute("user", user);
            response.sendRedirect("home.jsp");
        } catch (TwitterException ex) {
            Logger.getLogger(Callback.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException(ex);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Callback.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException(ex);
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
