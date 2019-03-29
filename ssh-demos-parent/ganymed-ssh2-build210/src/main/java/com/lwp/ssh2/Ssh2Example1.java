package com.lwp.ssh2;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.alibaba.fastjson.JSON;
import com.lwp.ssh2.core.GanymedSsh2Client;
import com.lwp.ssh2.core.Ssh2Result;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author lanwp
 * @Date 2019/3/28
 */
public class Ssh2Example1 {
    public static void main(String[] args) {
        String hostname = "192.168.14.227";
        String username = "root";
        String password = "1";
        GanymedSsh2Client ganymedSsh2Client = new GanymedSsh2Client(hostname, username, password);
        List<Ssh2Result> resultList = ganymedSsh2Client.execute("echo abc");
        printResult(resultList);

        resultList = ganymedSsh2Client.execute("echo hello && whoami");
        printResult(resultList);

        resultList = ganymedSsh2Client.execute("echo hello", "df -h");
        printResult(resultList);

        printResult(Ssh2Example1.executeCmd(hostname, username, password, "utf-8", "df"));
    }

    public static void printResult(List<Ssh2Result> resultList) {
        if (resultList == null) { return; }
        for (int i = 0; i < resultList.size(); i++) {
            System.out.println(resultList.get(i).getSuccessMsg());
            System.out.println(JSON.toJSONString(resultList));
        }
    }

    private static List<Ssh2Result> executeCmd(String hostname, String username, String password, String charset, String... cmds) {

        List<Ssh2Result> ssh2ResultList = new ArrayList<>();
        Connection connection = new Connection(hostname);
        Session session = null;
        try {
//            ConnectionInfo connectionInfo = connection.connect();
//            System.out.println(JSON.toJSONString(connectionInfo));
            connection.connect();
            boolean flag = connection.authenticateWithPassword(username, password);
            if(flag){
                SCPClient client = new SCPClient(connection);
                for (int i = 0; i < cmds.length; i++) {
                    Ssh2Result result = new Ssh2Result();
                    session= connection.openSession();
                    session.execCommand(cmds[i]);
                    String errorMsg = processStdout(session.getStderr(), charset);
                    String successMsg = processStdout(session.getStdout(), charset);
                    int exitStatus = session.getExitStatus();
                    result.setSuccessMsg(successMsg).setErrorMsg(errorMsg).setExitStatus(exitStatus);
                    session.close();
                    ssh2ResultList.add(result);
                }
            } else{
//                errorMsg="连接服务器失败，用户名或密码错误";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null!=session) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return ssh2ResultList;
    }

    /**
     * @Description 获取服务器输出流
     * @param:
     * @return:
     * @date: 2018/11/30 9:31
     */
    private static String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuilder buffer = new StringBuilder(128);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line).append("\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stdout != null) {
                try {
                    stdout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }
}
