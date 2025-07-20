
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class OMRServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server started on port 8080");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> handleClient(clientSocket)).start();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream out = clientSocket.getOutputStream();

            while (in.ready()) in.readLine(); // skip headers

            File imageFile = new File("omr_sheet.png");
            FileOutputStream fos = new FileOutputStream(imageFile);
            int ch;
            while ((ch = in.read()) != -1) fos.write(ch);
            fos.close();

            ProcessBuilder pb = new ProcessBuilder("python", "backend/analyzer.py", "omr_sheet.png");
            Process process = pb.start();
            BufferedReader pyOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = pyOut.readLine()) != null) json.append(line);

            // Simulated DB insert
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/omr_db", "root", "");
            PreparedStatement ps = con.prepareStatement("INSERT INTO omr_results (name, roll_no, answers, score) VALUES (?, ?, ?, ?)");
            ps.setString(1, "Test User");
            ps.setString(2, "12345");
            ps.setString(3, json.toString());
            ps.setInt(4, 80);
            ps.executeUpdate();

            String httpResponse = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + json;
            out.write(httpResponse.getBytes());
            out.flush();
            out.close();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
