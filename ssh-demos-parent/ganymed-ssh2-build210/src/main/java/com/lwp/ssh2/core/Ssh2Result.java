package com.lwp.ssh2.core;

/**
 * @Author lanwp
 * @Date 2019/3/28
 */
public class Ssh2Result {
    /**
     * 执行成功信息
     */
    private String successMsg;
    /**
     * 执行失败时的错误信息
     */
    private String errorMsg;
    /**
     * 退出状态，执行命令后返回的状态 0 成功 其他失败
     */
    private Integer exitStatus;

    public boolean isSuccess() {
        return exitStatus == 0;
    }

    public String getSuccessMsg() {
        return successMsg;
    }

    public Ssh2Result setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public Ssh2Result setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public Integer getExitStatus() {
        return exitStatus;
    }

    public Ssh2Result setExitStatus(Integer exitStatus) {
        this.exitStatus = exitStatus;
        return this;
    }
}
