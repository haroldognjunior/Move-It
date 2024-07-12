package com.crud;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@WebServlet(name = "moveit", urlPatterns = {"/moveit/*"})
public class moveit extends HttpServlet {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/moveit";
    private static final String USER = "root";
    private static final String PASS = "1234";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String path = request.getPathInfo();
        if (path == null) {
            path = "/";
        }

        switch (path) {
            case "/hello":
                handleHello(request, response);
                break;
            case "/data":
                handleData(request, response);
                break;
            case "/getUser":
                handleGetUser(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not Found");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String path = request.getPathInfo();
        if (path == null) {
            path = "/";
        }

        switch (path) {
            case "/addUser":
                handleAddUser(request, response);
                break;
            case "/validateUser":
                handleValidateUser(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not Found");
                break;
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String path = request.getPathInfo();
        if (path == null) {
            path = "/";
        }

        switch (path) {
            case "/updateUser":
                handleUpdateUser(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not Found");
                break;
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String path = request.getPathInfo();
        if (path == null) {
            path = "/";
        }

        switch (path) {
            case "/deleteUser":
                handleDeleteUser(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not Found");
                break;
        }
    }

    protected void handleHello(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("Hola mundo desde una ruta");
        }
    }

    protected void handleData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                stmt = conn.createStatement();
                String sql = "SELECT id, nombre, email FROM usuarios";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    String email = rs.getString("email");
                    out.println("ID: " + id + ", Nombre: " + nombre + ", Email: " + email);
                }
            } catch (Exception e) {
                e.printStackTrace();
                out.println("Error: " + e.getMessage());
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void handleGetUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing 'id' parameter");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid 'id' parameter");
            return;
        }

        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                String sql = "SELECT id, nombre, email FROM usuarios WHERE id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String email = rs.getString("email");
                    out.println("{\"id\": " + id + ", \"nombre\": \"" + nombre + "\", \"email\": \"" + email + "\"}");
                } else {
                    out.println("{}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                out.println("Error: " + e.getMessage());
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (pstmt != null) pstmt.close();
                    if (conn != null) conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void handleAddUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            sb.append(line);
        }
        JsonObject jsonObject = JsonParser.parseString(sb.toString()).getAsJsonObject();
        String nombre = jsonObject.get("nombre").getAsString();
        String email = jsonObject.get("email").getAsString();

        response.setContentType("text/plain;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                String sql = "INSERT INTO usuarios (nombre, email) VALUES (?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, nombre);
                pstmt.setString(2, email);
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    out.println("Usuario agregado exitosamente!");
                } else {
                    out.println("Error al agregar el usuario.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println("Error: " + e.getMessage());
            } finally {
                try {
                    if (pstmt != null) pstmt.close();
                    if (conn != null) conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void handleUpdateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            sb.append(line);
        }
        JsonObject jsonObject = JsonParser.parseString(sb.toString()).getAsJsonObject();
        int id = jsonObject.get("id").getAsInt();
        String nombre = jsonObject.get("nombre").getAsString();
        String email = jsonObject.get("email").getAsString();

        response.setContentType("text/plain;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                String sql = "UPDATE usuarios SET nombre = ?, email = ? WHERE id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, nombre);
                pstmt.setString(2, email);
                pstmt.setInt(3, id);
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    out.println("Usuario actualizado exitosamente!");
                } else {
                    out.println("Error al actualizar el usuario.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println("Error: " + e.getMessage());
            } finally {
                try {
                    if (pstmt != null) pstmt.close();
                    if (conn != null) conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void handleDeleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing 'id' parameter");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid 'id' parameter");
            return;
        }

        response.setContentType("text/plain;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                String sql = "DELETE FROM usuarios WHERE id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    out.println("Usuario eliminado exitosamente!");
                } else {
                    out.println("Error al eliminar el usuario.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println("Error: " + e.getMessage());
            } finally {
                try {
                    if (pstmt != null) pstmt.close();
                    if (conn != null) conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void handleValidateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            sb.append(line);
        }

        try {
            JsonObject jsonObject = JsonParser.parseString(sb.toString()).getAsJsonObject();
            String email = jsonObject.get("email").getAsString();

            response.setContentType("application/json;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                Connection conn = null;
                PreparedStatement pstmt = null;
                ResultSet rs = null;
                try {
                    Class.forName(JDBC_DRIVER);
                    conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    String sql = "SELECT id FROM usuarios WHERE email = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, email);
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        JsonObject responseJson = new JsonObject();
                        responseJson.addProperty("found", true);
                        out.println(responseJson.toString());
                    } else {
                        JsonObject responseJson = new JsonObject();
                        responseJson.addProperty("found", false);
                        out.println(responseJson.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    JsonObject responseJson = new JsonObject();
                    responseJson.addProperty("error", "Error: " + e.getMessage());
                    out.println(responseJson.toString());
                } finally {
                    try {
                        if (rs != null) rs.close();
                        if (pstmt != null) pstmt.close();
                        if (conn != null) conn.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("error", "Invalid JSON format: " + e.getMessage());
            response.getWriter().write(responseJson.toString());
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
