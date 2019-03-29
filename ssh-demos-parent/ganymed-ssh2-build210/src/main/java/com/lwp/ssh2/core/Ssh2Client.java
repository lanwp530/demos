package com.lwp.ssh2.core;

import java.util.List;

/**
 * @Author lanwp
 * @Date 2019/3/28
 */
public interface Ssh2Client {

    String CHARSET_UTF8 = "UTF-8";

    List<Ssh2Result> execute(GanymedSsh2ClientExecuteCallback callback);

    /**
     * 推荐使用此命令 多个命令使用 &&
     * @param cmd
     * @return
     */
    List<Ssh2Result> execute(String cmd);

    List<Ssh2Result> execute(String... cmds);

    void downloadFile(String remoteFiles, String localTargetDirectory);

    void downloadFile(String[] remoteFiles, String localTargetDirectory);

    void uploadFile(String localFile, String remoteTargetDirectory);

    void uploadFile(String[] localFiles, String remoteTargetDirectory);
}
