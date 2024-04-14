import sandbox.utils.LocalFileUtils;

import static java.lang.System.out;

public class TestIt {
    public static void main(String[] args) {
        out.println(LocalFileUtils.getHostIsDosLike());
        out.println(LocalFileUtils.standarizePath("C:\\"));
    }
}
