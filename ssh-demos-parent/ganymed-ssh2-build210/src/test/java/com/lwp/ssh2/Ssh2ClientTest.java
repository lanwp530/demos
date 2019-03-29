package com.lwp.ssh2;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import com.alibaba.fastjson.JSON;
import com.lwp.ssh2.core.GanymedSsh2Client;
import com.lwp.ssh2.core.GanymedSsh2ClientExecuteCallback;
import com.lwp.ssh2.core.Ssh2Client;
import com.lwp.ssh2.core.Ssh2Result;
import com.lwp.ssh2.util.GanymedSshUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Author lanwp
 * @Date 2019/3/28
 */
public class Ssh2ClientTest {

    String hostname = "192.168.14.227";
    String username = "root";
    String password = "1";
//    String hostname = "192.168.72.110";
//    String username = "root";
//    String password = "rootroot";

    final String DEFAULT_CHARSET = "UTF-8";

    @Test
    public void testHello(){
        Ssh2Client ganymedSsh2Client = this.getSsh2Client();
        List<Ssh2Result> resultList = ganymedSsh2Client.execute("echo abc");
        printResult(resultList);

        resultList = ganymedSsh2Client.execute("echo hello && whoami");
        printResult(resultList);

        resultList = ganymedSsh2Client.execute("echo hello", "df -h");
        printResult(resultList);
    }

    private Ssh2Client getSsh2Client() {
        return new GanymedSsh2Client(hostname, username, password);
    }

    @Test
    public void testDownloadFile() {
        GanymedSsh2Client ganymedSsh2Client = (GanymedSsh2Client) this.getSsh2Client();
        ganymedSsh2Client.execute(new GanymedSsh2ClientExecuteCallback() {
            @Override
            public void callback(Connection connection, List<Ssh2Result> ssh2ResultList) {
                SCPClient scpClient = new SCPClient(connection);
                try {
                    scpClient.get("remoteFile", "localDir");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void testDownloadAndUploadFile() {
        Ssh2Client ganymedSsh2Client = (GanymedSsh2Client) this.getSsh2Client();
//        ganymedSsh2Client.downloadFile("/srv/kubernetes/", "./");
        List<Ssh2Result> list = ganymedSsh2Client.execute(new GanymedSsh2ClientExecuteCallback() {
            @Override
            public void callback(Connection connection, List<Ssh2Result> ssh2ResultList) {
                SCPClient scpClient = new SCPClient(connection);
                Ssh2Result result = null;
                List<Ssh2Result> resultList = null;

                String certDirPath = "/srv/kubernetes";
                boolean isExists = GanymedSshUtils.isExists(connection, certDirPath + "/ca.crt");
                System.out.println("文件是否存在: " + isExists);


                String[] fileNames = GanymedSshUtils.listFiles(connection, certDirPath);
                System.out.println( "文件数" + fileNames.length);
                try {
                    File file = new File("./temp_certfiles");
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    for (int i = 0; i < fileNames.length; i++) {
                        System.out.println(fileNames[i]);
                        String remoteFile = certDirPath + "/" + fileNames[i];
                        scpClient.get(remoteFile, "./temp_certfiles");
                    }

                    new GanymedSsh2Client("192.168.14.226", username, password).execute(new GanymedSsh2ClientExecuteCallback() {
                        @Override
                        public void callback(Connection connection, List<Ssh2Result> ssh2ResultList) {
                            SCPClient scpClient2 = new SCPClient(connection);
                            Session session = null;
                            try {
                                session = connection.openSession();
                                // 远程目录如果文件夹不存在则创建
                                session.execCommand("mkdir -p " + certDirPath);
                                session.close();
                                for (int i = 0; i < fileNames.length; i++) {
                                    String remoteFile = certDirPath + "/" + fileNames[i];
                                    scpClient2.put("./temp_certfiles/" + fileNames[i], certDirPath);
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
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
        System.out.println(JSON.toJSONString(list));
    }

    public static void printResult(List<Ssh2Result> resultList) {
        if (resultList == null) { return; }
        for (int i = 0; i < resultList.size(); i++) {
            System.out.println(resultList.get(i).getSuccessMsg());
            System.out.println(JSON.toJSONString(resultList));
        }
    }

    private Ssh2Result sessionInvoke(Connection connection, String cmd){
        Session session = null;
        try {
            connection.openSession();
            session = connection.openSession();
            session.execCommand(cmd);
            return new Ssh2Result()
                    .setSuccessMsg(GanymedSshUtils.processStdout(session.getStdout(), DEFAULT_CHARSET))
                    .setErrorMsg(GanymedSshUtils.processStdout(session.getStderr(), DEFAULT_CHARSET))
                    .setExitStatus(session.getExitStatus());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }
}
