package com.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by 高峰 on 2017/2/11.
 */

public class JDBC {
    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;

    public Connection Jdbc() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String URL = "jdbc:mysql://www.hbhtfy.com.cn:6033/htfy";
            connection = DriverManager.getConnection(URL, "root", "123456");
            if (connection != null) {
                System.out.println("数据库连接成功");
                return connection;
            } else {
                System.out.println("数据库连接失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet loginCheck(Connection conn,String account) {
        if (conn != null) {
            try {
                ps =  conn.prepareStatement("select * from login where user_id=?");
                ps.setString(1, account);
                rs = ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return rs;
        }else{
            return null;
        }


    }
    public ResultSet getBaseInfo(Connection conn,int gdtm_id){
        if(conn!=null){
            try {
                ps = conn.prepareStatement("select user_name,user_address,location from base_info where gdtm_id=?");
                ps.setInt(1,gdtm_id);
                rs = ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return rs;
        } else{
            return null;
        }

    }

    public ResultSet getAlarmList(Connection conn,int gdtm_id){
        if(conn!=null){
            try {
                ps = conn.prepareStatement("select date,time,alarm_code,sub_code from alarm_list where gdtm_id=?");
                ps.setInt(1,gdtm_id);
                rs = ps.executeQuery();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return rs;
         } else{
            return  null;
        }

    }







}
