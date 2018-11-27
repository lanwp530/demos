package per.lwp;

import java.io.Closeable;
import java.io.IOException;

/**
 * description:
 *
 * @author lanwp
 * @date 2018/11/26 13:25
 */
public class IOUtils {

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
