/**
 * Created By Admin on 2020/1/2
 * Describe:
 */
public class FrameWork {

    private volatile static FrameWork mFrameWork;

    private FrameWork() {

    }

    public static FrameWork getFrameWork() {
        if (mFrameWork == null) {
            synchronized (FrameWork.class) {
                if (mFrameWork == null)
                    mFrameWork = new FrameWork();
            }
        }
        return mFrameWork;
    }
}
