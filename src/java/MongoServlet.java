import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/MongoServlet")
public class MongoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/java?useSSL=false&allowPublicKeyRetrieval=true"; // Update with your database name
    private static final String DB_USER = "root";  // Update with your MySQL username
    private static final String DB_PASSWORD = "admin";  // Update with your MySQL password

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form parameters
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        // Set up response content type
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // HTML response header
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Response</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; background-color: #f0f8ff; text-align: center; }");
        out.println(".container { margin-top: 50px; padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); background-color: #ffffff; display: inline-block; }");
        out.println("h3 { color: #333; }");
        out.println(".success { color: #28a745; }");
        out.println(".error { color: #dc3545; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        // Database connection and SQL insertion
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Prepare SQL insert statement
            ps = conn.prepareStatement("INSERT INTO users (name, email) VALUES (?, ?)");
            ps.setString(1, name);
            ps.setString(2, email);

            // Execute the query
            int result = ps.executeUpdate();

            // Send a response to the user
            out.println("<div class='container'>");
            if (result > 0) {
                out.println("<h3 class='success'>Data inserted successfully!</h3>");
            } else {
                out.println("<h3 class='error'>Data insertion failed.</h3>");
            }
            out.println("</div>");

        } catch (ClassNotFoundException e) {
            out.println("<div class='container'><h3 class='error'>Error: Unable to load database driver.</h3></div>");
            e.printStackTrace(); // For debugging
        } catch (SQLException e) {
            out.println("<div class='container'><h3 class='error'>Error: " + e.getMessage() + "</h3></div>");
            e.printStackTrace(); // For debugging
        } finally {
            // Close resources
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                out.println("<div class='container'><h3 class='error'>Error closing resources: " + e.getMessage() + "</h3></div>");
            }
        }

        // HTML response footer
        out.println("</body>");
        out.println("</html>");
    }
}
