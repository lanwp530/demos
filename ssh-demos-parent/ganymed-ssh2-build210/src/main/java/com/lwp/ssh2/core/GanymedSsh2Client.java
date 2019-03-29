package com.lwp.ssh2.core;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import com.lwp.ssh2.util.GanymedSshUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author lanwp
 * @Date 2019/3/28
 */
public class GanymedSsh2Client implements Ssh2Client {

    private String hostname;
    private String username;
    private String password;

    public GanymedSsh2Client(String hostname, String username, String password) {
        this.hostname = hostname;
        this.username = username;
        this.password = password;
    }

    @Override
    public List<Ssh2Result> execute(GanymedSsh2ClientExecuteCallback callback) {

        List<Ssh2Result> ssh2ResultList = new ArrayList<>();
        Connection connection = new Connection(hostname);
        try {
//            ConnectionInfo connectionInfo = connection.connect();
//            System.out.println(JSON.toJSONString(connectionInfo));
            connection.connect();
            boolean flag = connection.authenticateWithPassword(username, password);
            if(flag){
//                SCPClient client = new SCPClient(connection);
                callback.callback(connection, ssh2ResultList);
            } else{
//                errorMsg="连接服务器失败，用户名或密码错误";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return ssh2ResultList;
    }

    private List<Ssh2Result> executeCmd(String charset, String... cmds) {
        return this.execute(new GanymedSsh2ClientExecuteCallback() {
            @Override
            public void callback(Connection connection, List<Ssh2Result> ssh2ResultList) {
                Session session = null;
                try {
                    for (int i = 0; i < cmds.length; i++) {
                        Ssh2Result result = new Ssh2Result();
                        session = connection.openSession();
                        session.execCommand(cmds[i]);
                        String errorMsg = GanymedSshUtils.processStdout(session.getStderr(), charset);
                        String successMsg = GanymedSshUtils.processStdout(session.getStdout(), charset);
                        int exitStatus = session.getExitStatus();
                        result.setSuccessMsg(successMsg)
                                .setErrorMsg(errorMsg)
                                .setExitStatus(exitStatus);
                        session.close();
                        ssh2ResultList.add(result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (session != null) {
                        session.close();
                    }
                }
            }
        });
    }

    private List<Ssh2Result> executeCmd(String... cmds) {
        return this.executeCmd(Ssh2Client.CHARSET_UTF8, cmds);
    }

    @Override
    public List<Ssh2Result> execute(String cmd) {
        return this.executeCmd(new String[]{cmd});
    }

    @Override
    public List<Ssh2Result> execute(String... cmds) {
        return this.executeCmd(cmds);
    }

    @Override
    public void downloadFile(String remoteFile, String localTargetDirectory) {
        this.execute(new GanymedSsh2ClientExecuteCallback() {
            @Override
            public void callback(Connection connection, List<Ssh2Result> ssh2ResultList) {
                SCPClient scpClient = new SCPClient(connection);
                try {
                    scpClient.get(remoteFile, localTargetDirectory);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void downloadFile(String[] remoteFiles, String localTargetDirectory) {
        this.execute(new GanymedSsh2ClientExecuteCallback() {
            @Override
            public void callback(Connection connection, List<Ssh2Result> ssh2ResultList) {
                SCPClient scpClient = new SCPClient(connection);
                try {
                    scpClient.get(remoteFiles, localTargetDirectory);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void uploadFile(String localFile, String remoteTargetDirectory) {
        this.execute(new GanymedSsh2ClientExecuteCallback() {
            @Override
            public void callback(Connection connection, List<Ssh2Result> ssh2ResultList) {
                SCPClient scpClient = new SCPClient(connection);
                try {
                    scpClient.put(localFile, remoteTargetDirectory);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void uploadFile(String[] localFiles, String remoteTargetDirectory) {
        this.execute(new GanymedSsh2ClientExecuteCallback() {
            @Override
            public void callback(Connection connection, List<Ssh2Result> ssh2ResultList) {
                SCPClient scpClient = new SCPClient(connection);
                try {
                    scpClient.put(localFiles, remoteTargetDirectory);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
