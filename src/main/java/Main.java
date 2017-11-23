import org.opencv.core.Core;

public class Main {

    static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    public static void main(String[] args) {
        Tlg.tlgInit();

        Cam.photoWarden();

    }

}


