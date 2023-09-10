package org.firstinspires.ftc.teamcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class PropDetector extends OpenCvPipeline {
    private boolean saveImage = false;
    Mat mat = new Mat();
    Mat inverted = new Mat();
    Mat highMat = new Mat();
    Mat lowMat = new Mat();
    static final Rect LEFT_ROI = new Rect(
            new Point(0, 0),
            new Point(0, 0)
    );

    static final Rect RIGHT_ROI = new Rect(
            new Point(0, 0),
            new Point(0, 0)
    );


    public PropDetector(boolean save){
        saveImage = save;
    }
    @Override
    public Mat processFrame(Mat input) {
        //0-10, 160-180
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);

        if(saveImage){
            saveMatToDisk(input, "TeamProp.png");
        }
        Scalar lowHSVRedLower = new Scalar(0, 100, 20);  //Beginning of Color Wheel
        Scalar lowHSVRedUpper = new Scalar(10, 255, 255);

        Scalar redHSVRedLower = new Scalar(160, 100, 20); //Wraps around Color Wheel
        Scalar highHSVRedUpper = new Scalar(180, 255, 255);

        Core.inRange(mat, lowHSVRedLower, lowHSVRedUpper, lowMat);
        Core.inRange(mat, redHSVRedLower, highHSVRedUpper, highMat);

        mat.release();


        double l1 = Core.sumElems(lowMat.submat(LEFT_ROI)).val[0];
        double r1 = Core.sumElems(lowMat.submat(RIGHT_ROI)).val[0];

        double l2 = Core.sumElems(highMat.submat(LEFT_ROI)).val[0];
        double r2 = Core.sumElems(highMat.submat(RIGHT_ROI)).val[0];

        double finalRightBox = r1 + r2;
        double finalLeftBox = l1 + l2;

        double averagedLeftBox = finalLeftBox / LEFT_ROI.area();
        double averagedRightBox = finalRightBox / RIGHT_ROI.area();

        lowMat.release();
        highMat.release();





        return null;
    }
}
