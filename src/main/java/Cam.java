
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Cam {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");


    //    public static boolean wrd_act = true;
    public static boolean wrd_init = false;

    public static void photoWarden(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        VideoCapture camera = new VideoCapture(0);
        camera.set(Videoio.CV_CAP_PROP_FRAME_WIDTH, 1280);
        camera.set(Videoio.CV_CAP_PROP_FRAME_HEIGHT, 720);

        if(!camera.isOpened()){
            System.out.println("Cm Error");
        }
        else {
            int sensivity = 30;
            double maxArea = 30;
            int index = 0;
            Mat frame = new Mat(720, 1280, CvType.CV_8UC3);
            Mat frame_current = new Mat(720, 1280, CvType.CV_8UC3);
            Mat frame_previous = new Mat(720, 1280, CvType.CV_8UC3);
            Mat frame_result = new Mat(720, 1280, CvType.CV_8UC3);
            Size size = new Size(3, 3);
            Mat v = new Mat();
            Scalar scalar1 = new Scalar(0, 0, 255);
            Scalar scalar2 = new Scalar(0, 255, 0);

            while(true){
                if (camera.read(frame) && wrd_init){
                    frame.copyTo(frame_current);

                    Imgproc.GaussianBlur(frame_current, frame_current, size, 0);

                    if (index > 1) {
                        Core.subtract(frame_previous, frame_current, frame_result);

                        Imgproc.cvtColor(frame_result, frame_result, Imgproc.COLOR_RGB2GRAY);

                        Imgproc.threshold(frame_result, frame_result, sensivity, 255, Imgproc.THRESH_BINARY);

                        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
                        Imgproc.findContours(frame_result, contours, v, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
                        v.release();


                        boolean found = false;
                        for(int idx = 0; idx < contours.size(); idx++) {
                            Mat contour = contours.get(idx);
                            double contourarea = Imgproc.contourArea(contour);
                            if(contourarea > maxArea) {
                                found = true;

                                Rect r = Imgproc.boundingRect(contours.get(idx));
                                Imgproc.drawContours(frame, contours, idx, scalar1);
                                Imgproc.rectangle(frame, r.br(), r.tl(), scalar2, 1);
                            }
                            contour.release();
                        }

                        if (found) {
                            Tlg.sendMessageTo("Есть движение!");
                            System.out.println("Moved");
                            String newtempname = "./phs/" + (sdf.format(new Date())) + ".jpg";
                            Imgcodecs.imwrite(newtempname, frame);
                            Tlg.sendPhotoTo(newtempname, "120988325");
                        }
                    }

                    index++;

                    frame_current.copyTo(frame_previous);
                    frame.release();
                    frame_result.release();
                    frame_current.release();

                    try {
                        Thread.currentThread().sleep(500);

                    } catch (InterruptedException e) {}
                }
            }
        }
        camera.release();
    }
}
