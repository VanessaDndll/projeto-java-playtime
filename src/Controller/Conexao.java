/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.sql.*;

/**
 *
 * @author Adriano
 */
public class Conexao {
    public Connection getConnection()throws ClassNotFoundException,SQLException{
        Connection conexao=DriverManager.getConnection("jdbc:mysql://localhost/playtime", "root", "");
        Class.forName("com.mysql.cj.jdbc.Driver");
        return conexao;
    }
}
