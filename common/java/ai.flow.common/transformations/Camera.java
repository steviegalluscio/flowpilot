package ai.flow.common.transformations;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import ai.flow.common.utils;

public class Camera {

    // Setup for LGG8 based on Phr00t fork
    public static float
        FocalX = 1600f,
        FocalY = 1600f,
        CenterX = 952.62915f,
        CenterY = 517.53534f;

    // Use wide cam for LGG8
    public static int UseCameraID = 2;

    // everything autocalculated below
    public static float actual_cam_focal_length = (FocalX + FocalY) * 0.5f;
    public static float digital_zoom_apply = actual_cam_focal_length / Model.MEDMODEL_FL;
    public static final int[] frameSize = new int[]{1920, 1080};
    public static float OffsetX = CenterX - (frameSize[0]*0.5f);
    public static float OffsetY = CenterY - (frameSize[1]*0.5f);

    public static float[] CameraIntrinsics = {
            FocalX, 0.0f, frameSize[0] * 0.5f + OffsetX * digital_zoom_apply,
            0.0f, FocalY, frameSize[1] * 0.5f + OffsetY * digital_zoom_apply,
            0.0f,   0.0f, 1.0f
    };

    // everything auto-generated from above
    public static final int CAMERA_TYPE_ROAD = 0;
    public static final int CAMERA_TYPE_WIDE = 1;
    public static final int CAMERA_TYPE_DRIVER = 2;
    public static INDArray cam_intrinsics = Nd4j.createFromArray(new float[][]{
            { CameraIntrinsics[0],  0.0f,  CameraIntrinsics[2]},
            {0.0f,  CameraIntrinsics[4],  CameraIntrinsics[5]},
            {0.0f,  0.0f,  1.0f}
    });

    public static final INDArray view_from_device = Nd4j.createFromArray(new float[][]{
            {0.0f,  1.0f,  0.0f},
            {0.0f,  0.0f,  1.0f},
            {1.0f,  0.0f,  0.0f}
    });

}
