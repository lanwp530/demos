package com.lwp.ssh2.core;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;

import java.util.List;

/**
 * @Author lanwp
 * @Date 2019/3/28
 */
public interface GanymedSsh2ClientExecuteCallback {
    void callback(Connection connection, List<Ssh2Result> ssh2ResultList);
}
