package per.lwp.thread.pc;

/**
 * 商店
 * created by lawnp on 2018/12/19.
 */
public class Store2 {
    /**
     * 最大存量
     */
    private int maxCount = 6;

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    /**
     * 当前存量
     */
    private volatile int curCount = 0;

    public int getCurCount() {
        return curCount;
    }

    public void setCurCount(int curCount) {
        this.curCount = curCount;
    }

    /**
     * 进货
     */
    public synchronized void buy() {
        while ( curCount == maxCount) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.curCount = this.curCount + 1;
        System.out.println(Thread.currentThread().getName() + " 商店进货,当前数量: " + this.curCount);
        this.notify();
    }

    /**
     * 出货
     */
    public synchronized void sell() {
        while (curCount == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        this.curCount = this.curCount - 1;
        System.out.println(Thread.currentThread().getName() + " 商店卖出1件货物,当前数量: " + this.curCount);
        this.notify();
    }
}
