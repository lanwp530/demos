package per.lwp;

/**
 * description:
 *
 * @author lanwp
 * @date 2018/11/27 9:47
 */
public class ArrayUtils {

    public static boolean isEmpty(Object[] arr) {
        return (arr == null || arr.length == 0);
    }

    public static boolean isNotEmpty(Object[] arr) {
        return !isEmpty(arr);
    }
}
