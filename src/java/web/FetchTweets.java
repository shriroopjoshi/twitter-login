package web;

import beans.TwitterUser;
import classes.Constants;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import twitter4j.IDs;
import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class FetchTweets extends HttpServlet {

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
        PrintWriter writer = response.getWriter();
        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        TwitterUser user = (TwitterUser) request.getSession().getAttribute("user");
        if (user == null || twitter == null) {
            response.sendRedirect("index.html");
        }
        long cursor = -1;
        IDs ids;
        ResponseList<User> followers;
        ResponseList<Location> locations;
        List<String> tweets = new ArrayList<>();
        try {
            do {
                writer.write("<b>Followers</b><br>");
                ids = twitter.getFollowersIDs(user.getUserName(), cursor);
                followers = twitter.lookupUsers(ids.getIDs());
                for (User u : followers) {
                    if (u.getStatus() != null) {
                        writer.write("@" + u.getScreenName() + " - [" + u.getStatus().getText() + "]<br>");
                        tweets.add(u.getStatus().getText());
                    } else {
                        writer.write("@" + u.getScreenName() + "<br>");
                    }
                }
            } while ((cursor = ids.getNextCursor()) != 0);
            writer.write("<br>");
            writer.write("<b>Trends</b>");
            locations = twitter.getAvailableTrends();
            for(Location location: locations) {
                writer.write(location.getName() + " (Placename: " + location.getPlaceName() + ")<br>");
            }
            writer.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.FILE_NAME));
            for(String str: tweets) {
                bw.write(str + "\n");
            }
            bw.close();
        } catch (TwitterException ex) {
            Logger.getLogger(FetchTweets.class.getName()).log(Level.SEVERE, null, ex);
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
