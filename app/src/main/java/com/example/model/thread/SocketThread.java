package com.example.model.thread;

import android.util.Log;

import com.example.model.impl.LoginModelImpl;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SocketThread extends Thread {
    private static SocketThread instance = new SocketThread();


    private SocketThread() {

    }

    public static SocketThread getInstance() {

        return instance;
    }


    public class gdtm_t {
        public gdtm_t next;
        public gdtm_t prev;
        public String data;
        public gdtm_t detail;

        public gdtm_t() {
            this.detail = null;
            this.data = null;
            this.prev = null;
            this.next = null;
        }
    }

    public int socket_mode;         //当前socket线程运行模式
    // = 1 登陆并取机房信息
    // = 2 登陆并取回由 gdtm_sel 指定的机房数据
    public int socket_state;        //当前socket线程运行状态
    // 0x80000000   socket发送错误
    // 0x40000000   socket接收错误
    // 0x08000000   用户登陆过程
    // 0x04000000   用户登出过程
    // 0x02000000   gdtm登陆过程
    // 0x01000000   gdtm登出过程
    // 0x00800000   取gdtm信息过程
    // 0x00400000   取gdtm数据过程
    // 0x00200000   socket连接过程
    // 0x00100000   socket断开过程
    // 0x00080000   校验版本过程
    // 0x00000008   过程异常，过程内部定义
    // 0x00000004   同上
    // 0x00000002   同上
    // 0x00000001   同上
    public String user_name;        //用户名
    public String user_pass;        //用户密码
    public String[] gdtm_info;      //机房详细信息
    public String[] gdtm_id;        //用户gdtm_id
    public int gdtm_sel;            //要获取数据的gdtm，此值为下标，不是机房id
    public int gdtm_max;            //用户下挂的机房数量
    public String gdtm_start_date;  //要取数据的起始日期
    public String gdtm_end_date;    //要取数据的结束日期
    public gdtm_t gdtm_data;        //数据链


    private Socket socket;
    private InputStream in;
    private OutputStream out;

    //初始化socket，并连接服务器
    private int socketConnect() {
        socket_state = 0x00200000;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("114.115.218.206", 8631), 5000);
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (SocketTimeoutException aa) {
            socket_state += 0x1;
            return socket_state;
        } catch (IOException e) {
            socket_state += 0x2;
            return socket_state;
        }
        return 0;
    }

    //通信完成，关闭连接
    private int socketClose() {
        socket_state = 0x00100000;
        try {
            in.close();
            out.close();
            socket.close();
        } catch (SocketTimeoutException aa) {
            socket_state += 0x1;
            return socket_state;
        } catch (IOException e) {
            socket_state += 0x2;
            return socket_state;
        }
        return 0;
    }

    //发送一次数据
    private int socketSend(String str) {
        byte buff[] = new byte[32];
        try {
            buff = str.getBytes();
            out.write(buff);
            out.flush();
        } catch (SocketTimeoutException aa) {
            socket_state += 0x80000000;
            return socket_state;
        } catch (IOException e) {
            socket_state += 0x80000000;
            return socket_state;
        }
        return 0;
    }

    //接收指定长度数据
    private int socketRecv(byte data[], int size) {
        int ret;
        try {
            ret = in.read(data, 0, size);
        } catch (SocketTimeoutException aa) {
            socket_state += 0x40000000;
            return socket_state;
        } catch (IOException e) {
            socket_state += 0x40000000;
            return socket_state;
        }
        return 0;
    }

    //校验协议版本
    private int socketVer() {
        int ret;
        byte[] buff;

        socket_state = 0x00080000;
        buff = new byte[16];
        ret = socketSend("V50");
        if (ret != 0) {
            socket_state += 0x1;
            return socket_state;
        }
        ret = socketRecv(buff, 2);
        if (ret != 0) {
            socket_state += 0x1;
            return socket_state;
        }
        if ((buff[0] != 0x23) || (buff[1] != 0x30)) {
            socket_state += 0x2;
            return socket_state;
        }
        return 0;
    }

    //用户登出
    private int socketUserLogout() {
        String cmd;
        byte buff[];
        int ret;

        socket_state = 0x04000000;
        buff = new byte[8];
        cmd = "CQ";
        ret = socketSend(cmd);
        if (ret != 0) {
            socket_state += 0x1;
            return socket_state;
        }
        ret = socketRecv(buff, 2);
        if (ret != 0) {
            socket_state += 0x1;
            return socket_state;
        }
        if ((buff[0] != 0x23) || (buff[1] != 0x30)) {     //用户登出失败
            socket_state += 0x2;
            return socket_state;
        }
        return 0;
    }

    //用户登陆
    private int socketUserLogin() {
        String cmd;
        int len;
        int ret;
        byte buff[];


        socket_state = 0x08000000;
        buff = new byte[5];
        cmd = String.format("CA%02d%s|%02d%s", user_name.length(), user_name, user_pass.length(), user_pass);
        ret = socketSend(cmd);
        if (ret != 0) {
            socket_state += 0x1;
            return socket_state;
        }
        ret = socketRecv(buff, 2);
        if (ret != 0) {
            socket_state += 0x1;
            return socket_state;
        }
        //检测用户登陆结果
        if (buff[0] == 0x23) {
            switch (buff[1]) {
                case 0x46:      //'F'用户名未找到
                    socket_state += 0x2;
                    return socket_state;
                case 0x45:      //'E'密码错误
                    socket_state += 0x4;
                    return socket_state;
                case 0x40:      //'@'正确，开始接收gdtm_id
                    break;
                default:
                    socket_state += 0x8;
                    return socket_state;
            }
        }
        //接收用户下挂的gdtm数量
        ret = socketRecv(buff, 2);
        if (ret != 0) {
            socket_state += 0x1;
            return socket_state;
        }
        gdtm_max = (buff[0] - 0x30) * 10 + (buff[1] - 0x30);
        gdtm_id = new String[gdtm_max];
        gdtm_info = new String[gdtm_max];
        //接收用户下挂的gdtm_id
        for (int i = 0; i < gdtm_max; i++) {
            ret = socketRecv(buff, 1);
            if (buff[0] == 0x24) {
                //找到同步头了
                ret = socketRecv(buff, 5);
                if (ret != 0) {
                    socket_state += 0x2;
                    return socket_state;
                }
                gdtm_id[i] = new String(buff);
            }
        }
        return 0;
    }

    //获取用户信息
    private int socketGetUserInfo(int sel) {
        int ret, size;
        String cmd;
        byte[] buff;
        byte[] info;

        socket_state = 0x00800000;
        buff = new byte[8];
        cmd = "CB" + gdtm_id[sel];
        ret = socketSend(cmd);
        if (ret != 0) {
            socket_state += 0x1;
            return socket_state;
        }
        ret = socketRecv(buff, 2);
        if (ret != 0) {
            socket_state += 0x1;
            return socket_state;
        }
        if ((buff[0] != 0x23) || (buff[1] != 0x40)) {
            socket_state += 0x2;
            return socket_state;
        }
        ret = socketRecv(buff, 3);
        if (ret != 0) {
            socket_state += 0x1;
            return socket_state;
        }
        size = (((buff[0] - 0x30) * 100) + ((buff[1] - 0x30) * 10) + (buff[2] - 0x30));
        //查找同步头
        ret = socketRecv(buff, 1);
        if (ret != 0) {
            socket_state += 0x1;
            return socket_state;
        }
        if (buff[0] != 0x24) {
            socket_state += 0x4;
            return socket_state;
        }
        size -= 1;
        info = new byte[size];
        ret = socketRecv(info, size);
        if (ret != 0) {
            socket_state += 0x1;
            return socket_state;
        }
        try {
            gdtm_info[sel] = new String(info, "UTF8");
        } catch (UnsupportedEncodingException e) {
            socket_state += 0x8;
            return socket_state;
        }
        return 0;
    }

    //获得GDTM的机房数据
    private int socketGetGdtmData() {
        String cmd;
        int ret, size;
        byte[] buff;
        byte[] info;
        gdtm_t p1, p2, p3;  //p1永远是主链的最新位置，p2永远是详细数据分支链的最新位置，p3作为临时轮转用指针
        String              date_curr_str;
        String              tmp_str;
        SimpleDateFormat    sdf;
        Date                date_end;
        Date                date_curr;

        buff = new byte[32];
        //登陆GDTM
        socket_state = 0x00800000;
        cmd = "C9" + gdtm_id[gdtm_sel];
        ret = socketSend(cmd);
        if (ret != 0) { socket_state += 0x1;   return socket_state; }
        ret = socketRecv(buff, 0x2);
        if (ret != 0) { socket_state += 0x1;   return socket_state; }
        if ((buff[0] != 0x23) || (buff[1] != 0x30)) {    //登陆gdtm失败
            socket_state += 0x2;
            return socket_state;
        }
        //开始取数据的相关信息
        socket_state = 0x00400000;
        sdf = new SimpleDateFormat("yyyyMMddHHmm");
        try {
            date_end = sdf.parse(gdtm_end_date);
        } catch (Exception e) {
            socket_state += 0x1;
            return socket_state;
        }
        //初始化数据存放结构，链头是特殊块，没有数据内容
        gdtm_data = new gdtm_t();
        gdtm_data.data = new String("START");
        cmd = "C5$" + gdtm_start_date;
        ret = socketSend(cmd);
        if (ret != 0) { socket_state += 0x1;   return socket_state; }
        cmd = "CC";     //只有第一次需要发C5，后面都发CC，所以发完C5之后，cmd就替换成了CC
        p1 = gdtm_data;
        p2 = null;
        p3 = null;
        //开始接收循环
        do {
            //接收命令返回值
            ret = socketRecv(buff, 2);
            if (ret != 0) { socket_state += 0x1;   return socket_state; }
            if (buff[0] != 0x23) { socket_state += 0x2; return socket_state; }     //判断#
            if (buff[1] == 0x45) { socket_state += 0x4; return socket_state; }     //#E 未找到数据
            if (buff[1] != 0x40) { socket_state += 0x8; return socket_state; }     //不是#@，也是错误
            //接收有效数据长度
            ret = socketRecv(buff, 2);
            if (ret != 0) { socket_state += 0x1;   return socket_state; }
            size = ((buff[0] - 0x30) * 10) + (buff[1] - 0x30);
            info = new byte[size];
            ret = socketRecv(info, size);
            if (ret != 0) { socket_state += 0x1;   return socket_state; }
            //检测日期，超出停止日期则抛弃返回
            tmp_str = new String(info);
            date_curr_str = tmp_str.substring(1, 13);
            try {
                date_curr = sdf.parse(date_curr_str);
            } catch (Exception e) {
                socket_state += 0x10;
                return socket_state;
            }
            if (date_curr.getTime() > date_end.getTime())
                break;
            //开始解析命令并插入到数据链上
            if ((info[14] == 0x43) && (info[15] == 0x30)) {
                //收到的是C0，开始制水，p2指向null，表示此时没有详细链
                p3 = new gdtm_t();
                p3.prev = p1;
                p1.next = p3;
                p3.data = new String(info);
                p1 = p3;
                p2 = null;
                date_curr_str = p1.data.substring(1, 13);
            } else if ((info[14] == 0x43) && (info[15] == 0x31)) {
                //收到的是C1，结束制水，关闭详细参数的分支
                p3 = new gdtm_t();
                p3.prev = p1;
                p1.next = p3;
                p3.data = new String(info);
                if (p2 != null) p2.next = p3;
                p2 = null;
                p1 = p3;
                date_curr_str = p1.data.substring(1, 13);
            } else if ((info[14] == 0x43) && (info[15] == 0x32)) {
                //收到的是C2，制水参数，将数据添加到详细参数分支上去
                p3 = new gdtm_t();
                if (p2 == null) {
                    p3.prev = p1;
                    p1.detail = p3;
                } else {
                    p3.prev = p2;
                    p2.next = p3;
                }
                p3.data = new String(info);
                p2 = p3;
                date_curr_str = p2.data.substring(1, 13);
            } else {
                //其他数据，挂到主链上
                p3 = new gdtm_t();
                p3.prev = p1;
                p1.next = p3;
                if (p2 != null) {
                    p2.next = p3;
                    p2 = null;
                }
                p3.data = new String(info);
                p1 = p3;
                date_curr_str = p1.data.substring(1, 13);
            }
            //还没到结束日期，再发一次CC
            ret = socketSend(cmd);
            if (ret != 0) { socket_state += 0x1;   return socket_state; }
        } while (true);
        //读取数据完成，登出gdtm
        socket_state = 0x01000000;
        cmd = "CZ";
        ret = socketSend(cmd);
        if (ret != 0) { socket_state += 0x1;   return socket_state; }
        ret = socketRecv(buff, 0x2);
        if (ret != 0) { socket_state += 0x1;   return socket_state; }
        if ((buff[0] != 0x23) || (buff[1] != 0x30)) {     //登出gdtm失败
            socket_state += 0x2;
            return socket_state;
        }
        return 0;
    }

    //主线程处理
    @Override
    public void run() {
        Socket socket = null;
        int ret;

        socket_state = 0x0;
        if (socketConnect() != 0)
            return;
        //一次完整的通讯过程
        do {
            if (socketVer() != 0) break;
            if (socketUserLogin() != 0) break;
            ret = 0;
            switch (socket_mode) {
                case 0x1:           //取用户信息
                    for (int sel = 0x0; sel < gdtm_max; sel++) {
                        ret = socketGetUserInfo(sel);
                        if (ret != 0) break;
                    }
                    break;
                case 0x2:           //取指定用户数据
                    ret = socketGetGdtmData();
                    break;
                default:
                    socket_state = 0x1;
                    ret = socket_state;
                    break;
            }
            if (ret != 0x0) {
                break;
            }
        } while (false);
        if (socketUserLogout() != 0x0)
            return;
        if (socketClose() != 0x0)
            return;
        socket_state = 0x0;
        EventBus.getDefault().postSticky(LoginModelImpl.flag);
        return;
    }
}