/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import hashing.Hasher;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author PHT
 */
public class AccountFacade {
    public Account login(String email, String password) throws SQLException, NoSuchAlgorithmException{
        Account account = null;
        //Tạo connection để kết nối vào DBMS
        Connection con = DBContext.getConnection();
        //Tạo đối tượng statement
        PreparedStatement stm = con.prepareStatement("select * from account where email = ? and password = ?");
        //Cung cap value cho cac tham so
        stm.setString(1, email);
        stm.setString(2, Hasher.hash(password));
        //Thực thi lệnh SELECT
        ResultSet rs = stm.executeQuery();
        while (rs.next()) {
            //Doc mau tin hien hanh de vao doi tuong account
            account = new Account();
            account.setId(rs.getInt("id"));
            account.setEmail(rs.getString("email"));
            account.setFullName(rs.getString("fullName"));
            account.setRoleId(rs.getString("roleId"));
            account.setPassword(rs.getString("password"));
        }
        con.close();
        return account;
    }
    
    public void register(Account account) throws SQLException, NoSuchAlgorithmException{
        //Tạo connection để kết nối vào DBMS
        Connection con = DBContext.getConnection();
        //Tạo đối tượng statement
        PreparedStatement stm = con.prepareStatement("insert into Account values(?,?,?,?)");
        //Cung cap value cho cac tham so
        stm.setString(1, account.getEmail());
        stm.setString(2, account.getFullName());
        stm.setString(3, account.getRoleId());
        stm.setString(4, Hasher.hash(account.getPassword()));
        //Thực thi lệnh INSERT
        stm.executeUpdate();        
        con.close();
    }
}
