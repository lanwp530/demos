package com.lwp.ssh2.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.lwp.ssh2.core.Ssh2Client;

import java.io.*;

/**
 * @Author lanwp
 * @Date 2019/3/28
 */
public class GanymedSshUtils {

    private final static String LINUX_LINE = "\n";

    /**
     * @Description 获取服务器输出流
     * @param:
     * @return:
     * @date: 2018/11/30 9:31
     */
    public static String processStdout(InputStream in, String charset) {
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

    /**
     *    [root@docker227 ~]# find /root/anaconda-ks.cfg
     *    /root/anaconda-ks.cfg
     *   [root@docker227 ~]# find /root/anaconda-ks.cfg123
     *   find: ‘/root/anaconda-ks.cfg123’: 没有那个文件或目录
     * @param connection
     * @param remoteFile
     * @return
     */
    public static boolean isExists(Connection connection, String remoteFile) {
        Session session = null;
        try {
            session = connection.openSession();
            session.execCommand("find " + remoteFile);
            String msg = GanymedSshUtils.processStdout(session.getStdout(), Ssh2Client.CHARSET_UTF8);
            String errorMsg = GanymedSshUtils.processStdout(session.getStderr(), Ssh2Client.CHARSET_UTF8);
            //  1 find: ‘/srv/kubernetes/ca11.crt’: 没有那个文件或目录
            System.out.println(session.getExitStatus() + " " + (session.getExitStatus() == 0));
            if (session.getExitStatus() == 0) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return false;
    }

    public static boolean isSuccess(int exitStatus) {
        return exitStatus == 0;
    }

    /**
     * 获取远端指定目录下所有文件和文件夹名
     * @param connection
     * @param remoteDir
     * @return
     */
    public static String[] listFiles(Connection connection, String remoteDir) {
        Session session = null;
        try {
            session = connection.openSession();
            session.execCommand("cd " + remoteDir + " && ls");

            String msg = GanymedSshUtils.processStdout(session.getStdout(), Ssh2Client.CHARSET_UTF8);
            String errorMsg = GanymedSshUtils.processStdout(session.getStderr(), Ssh2Client.CHARSET_UTF8);
            //  1 find: ‘/srv/kubernetes/ca11.crt’: 没有那个文件或目录
            System.out.println(session.getExitStatus() + " " + (session.getExitStatus() == 0));
            if (isSuccess(session.getExitStatus())) {
                // 正常列出文件
                String[] fileNames = msg.split(LINUX_LINE);
                return fileNames;
            }
            // 执行失败,打印错误信息
            System.out.println(errorMsg);
            return null;
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
