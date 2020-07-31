package com.example.arspapp_ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.Sensor
import android.hardware.SensorManager
import android.hardware.camera2.*
import android.hardware.camera2.CameraDevice.StateCallback
import android.hardware.camera2.CameraDevice.TEMPLATE_PREVIEW
import android.media.CamcorderProfile
import android.media.Image
import android.media.ImageReader
import android.media.ImageReader.OnImageAvailableListener
import android.media.MediaRecorder
import android.net.Uri
import android.os.*
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import org.tensorflow.lite.examples.posenet.lib.BodyPart
import org.tensorflow.lite.examples.posenet.lib.KeyPoint
import org.tensorflow.lite.examples.posenet.lib.Person
import org.tensorflow.lite.examples.posenet.lib.Posenet
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.PI
import kotlin.math.abs

class PosenetActivity :
        Fragment(),
        ActivityCompat.OnRequestPermissionsResultCallback {


    /** List of body joints that should be connected.    */
    private val bodyJoints = listOf(
            Pair(BodyPart.LEFT_HIP, BodyPart.RIGHT_HIP),
            Pair(BodyPart.LEFT_HIP, BodyPart.LEFT_KNEE),
            Pair(BodyPart.LEFT_KNEE, BodyPart.LEFT_ANKLE),
            Pair(BodyPart.RIGHT_HIP, BodyPart.RIGHT_KNEE),
            Pair(BodyPart.RIGHT_KNEE, BodyPart.RIGHT_ANKLE)
    )


    var posi =1
    var allkeyPoint= arrayOfNulls<KeyPoint>(10000)
    private var mNextVideoAbsolutePath: String? = null
    private val DETAIL_PATH = "DCIM/test1/"
    private var mediaRecorder: MediaRecorder? = null
    var Cachedir: File?=null
    /** Threshold for confidence score. */
    private val minConfidence = 0.5

    /** Radius of circle used to draw keypoints.  */
    private val circleRadius = 8.0f

    /** Paint class holds the style and color information to draw geometries,text and bitmaps. */
    private var paint = Paint()

    /** A shape for extracting frame data.   */
    private val PREVIEW_WIDTH = 640
    private val PREVIEW_HEIGHT = 480

    /** An object for the Posenet library.    */
    private lateinit var posenet: Posenet

    /** ID of the current [CameraDevice].   */
    private var cameraId: String? = null

    /** A [SurfaceView] for camera preview.   */
    private var surfaceView: SurfaceView? = null

    /** A [CameraCaptureSession] for camera preview.   */
    private var captureSession: CameraCaptureSession? = null

    /** A [CameraCaptureSession] for camera preview.   */
    private var previewSession: CameraCaptureSession? = null

    /** A reference to the opened [CameraDevice].    */
    private var cameraDevice: CameraDevice? = null

    /** The [android.util.Size] of camera preview.  */
    private var previewSize: Size? = null

    /** The [android.util.Size.getWidth] of camera preview. */
    private var previewWidth = 0

    /** The [android.util.Size.getHeight] of camera preview.  */
    private var previewHeight = 0

    /** A counter to keep count of total frames.  */
    private var frameCounter = 0

    /** An IntArray to save image data in ARGB8888 format  */
    private lateinit var rgbBytes: IntArray

    /** A ByteArray to save image data in YUV format  */
    private var yuvBytes = arrayOfNulls<ByteArray>(3)

    /** An additional thread for running tasks that shouldn't block the UI.   */
    private var backgroundThread: HandlerThread? = null

    /** A [Handler] for running tasks in the background.    */
    private var backgroundHandler: Handler? = null

    /** An [ImageReader] that handles preview frame capture.   */
    private var imageReader: ImageReader? = null

    /** [CaptureRequest.Builder] for the camera preview   */
    private var previewRequestBuilder: CaptureRequest.Builder? = null

    /** [CaptureRequest] generated by [.previewRequestBuilder   */
    private var previewRequest: CaptureRequest? = null

    /** A [Semaphore] to prevent the app from exiting before closing the camera.    */
    private val cameraOpenCloseLock = Semaphore(1)

    /** Whether the current camera device supports Flash or not.    */
    private var flashSupported = false

    /** Orientation of the camera sensor.   */
    private var sensorOrientation: Int? = null

    /** Abstract interface to someone holding a display surface.    */
    private var surfaceHolder: SurfaceHolder? = null

    private var recordingSurface: Surface?=null

    private var mRecorderSurface: Surface?=null

    private var deviceOrientation: DeviceOrientation? = null

    private lateinit var mSensorManager: SensorManager

    private lateinit var mAccelerometer: Sensor
    private lateinit var mMagnetometer: Sensor

    var pose:FragmentActivity?=null

    // var tracking = com.example.arspapp_ui.tracking()
    var test = com.example.arspapp_ui.tracking()
    var point = com.example.arspapp_ui.point()

    var save_Vec1:Float?=null
    var save_Vec2:Float?=null
    private var feedbackimg:String?=null
    private var Save_Bitmap:Bitmap?=null
    var key_list=java.util.ArrayList<Point>()
    var start_joint_list=java.util.ArrayList<Point>()
    var stop_joint_list=java.util.ArrayList<Point>()
    var startPoint:Point?=null
    var stopPoint:Point?=null

    /** [CameraDevice.StateCallback] is called when [CameraDevice] changes its state.   */
    private val stateCallback = object : StateCallback() {

        override fun onOpened(cameraDevice: CameraDevice) {
            cameraOpenCloseLock.release()
            this@PosenetActivity.cameraDevice = cameraDevice
            createCameraPreviewSession()
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            cameraOpenCloseLock.release()
            cameraDevice.close()
            this@PosenetActivity.cameraDevice = null
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            onDisconnected(cameraDevice)
            this@PosenetActivity.activity?.finish()
        }
    }

    /**
     * A [CameraCaptureSession.CaptureCallback] that handles events related to JPEG capture.
     */
    private val captureCallback = object : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureProgressed(
                session: CameraCaptureSession,
                request: CaptureRequest,
                partialResult: CaptureResult
        ) {
        }

        override fun onCaptureCompleted(
                session: CameraCaptureSession,
                request: CaptureRequest,
                result: TotalCaptureResult
        ) {
        }
    }

    /**
     * Shows a [Toast] on the UI thread.
     *
     * @param text The message to show
     */
    private fun showToast(text: String) {
        val activity = activity
        activity?.runOnUiThread { Toast.makeText(activity, text, Toast.LENGTH_SHORT).show() }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.activity_posenet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        surfaceView = view.findViewById(R.id.surfaceView)
        mSensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        initSensor()
        deviceOrientation = DeviceOrientation()
        surfaceHolder = surfaceView!!.holder

        Cachedir=requireContext()!!.cacheDir


    }

    override fun onResume() {
        super.onResume()
        startBackgroundThread()
        mSensorManager.registerListener(
                deviceOrientation!!.eventListener, mAccelerometer, SensorManager.SENSOR_DELAY_UI
        )
        mSensorManager.registerListener(
                deviceOrientation!!.eventListener, mMagnetometer, SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun onStart() {
        super.onStart()
        openCamera()

        posenet = Posenet(this.requireContext())
    }

    override fun onPause() {
        super.onPause()
        closeCamera()
        stopBackgroundThread()
        mSensorManager.unregisterListener(deviceOrientation!!.eventListener)
        onDestroy()
    }

    override fun onDestroy() {
        super.onDestroy()
        posenet.close()

    }

    private fun requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            ConfirmationDialog().show(childFragmentManager, FRAGMENT_DIALOG)
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (allPermissionsGranted(grantResults)) {
                ErrorDialog.newInstance(getString(R.string.tfe_pn_request_permission))
                        .show(childFragmentManager, FRAGMENT_DIALOG)
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun allPermissionsGranted(grantResults: IntArray) = grantResults.all {
        it == PackageManager.PERMISSION_GRANTED
    }

    private fun initSensor() {
        mSensorManager = this.requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    /**
     * Sets up member variables related to camera.
     */
    private fun setUpCameraOutputs() {

        val activity = activity
        val manager = this.requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            for (cameraId in manager.cameraIdList) {
                val characteristics = manager.getCameraCharacteristics(cameraId)

                // We don't use a front facing camera in this sample.
                val cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING)
                if (cameraDirection != null &&
                        cameraDirection == CameraCharacteristics.LENS_FACING_FRONT
                ) {
                    continue
                }

                previewSize = Size(PREVIEW_WIDTH, PREVIEW_HEIGHT)

                imageReader = ImageReader.newInstance(
                        PREVIEW_WIDTH, PREVIEW_HEIGHT,
                        ImageFormat.YUV_420_888, /*maxImages*/ 20
                )

                sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!

                previewHeight = previewSize!!.height
                previewWidth = previewSize!!.width

                // Initialize the storage bitmaps once when the resolution is known.
                rgbBytes = IntArray(previewWidth * previewHeight)

                // Check if the flash is supported.
                flashSupported =
                        characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true

                this.cameraId = cameraId

                // We've found a viable camera and finished setting up member variables,
                // so we don't need to iterate through other available cameras.
                return
            }
        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        } catch (e: NullPointerException) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
            ErrorDialog.newInstance(getString(R.string.tfe_pn_camera_error))
                    .show(childFragmentManager, FRAGMENT_DIALOG)
        }
    }

    /**
     * Opens the camera specified by [PosenetActivity.cameraId].
     */
    private fun openCamera() {
        val permissionCamera =
                ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission()
        }
        setUpCameraOutputs()
        val manager = requireActivity().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            // Wait for camera to open - 2.5 seconds is sufficient
            if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw RuntimeException("Time out waiting to lock camera opening.")
            }
            manager.openCamera(cameraId!!, stateCallback, backgroundHandler)
        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera opening.", e)
        }
    }

    /**
     * Closes the current [CameraDevice].
     */
     fun closeCamera() {
        if (captureSession == null) {
            return
        }

        try {
            cameraOpenCloseLock.acquire()
            captureSession!!.close()
            captureSession = null
            cameraDevice!!.close()
            cameraDevice = null
            imageReader!!.close()
            imageReader = null
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera closing.", e)
        } finally {
            cameraOpenCloseLock.release()
        }


    }

    /**
     * Starts a background thread and its [Handler].
     */
    private fun startBackgroundThread() {
        backgroundThread = HandlerThread("imageAvailableListener").also { it.start() }
        backgroundHandler = Handler(backgroundThread!!.looper)
        Log.i(TAG, "threading start")
    }

    /**
     * Stops the background thread and its [Handler].
     */
    private fun stopBackgroundThread() {
        backgroundThread?.quitSafely()
        try {
            backgroundThread?.join()
            backgroundThread = null
            backgroundHandler = null
        } catch (e: InterruptedException) {
            Log.e(TAG, e.toString())
        }
    }

    /** Fill the yuvBytes with data from image planes.   */
    private fun fillBytes(planes: Array<Image.Plane>, yuvBytes: Array<ByteArray?>) {
        // Row stride is the total number of bytes occupied in memory by a row of an image.
        // Because of the variable row stride it's not possible to know in
        // advance the actual necessary dimensions of the yuv planes.
        for (i in planes.indices) {
            val buffer = planes[i].buffer
            if (yuvBytes[i] == null) {
                yuvBytes[i] = ByteArray(buffer.capacity())
            }
            buffer.get(yuvBytes[i]!!)
        }
    }

    /** A [OnImageAvailableListener] to receive frames as they are available.  */
    private var imageAvailableListener = object : OnImageAvailableListener {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onImageAvailable(imageReader: ImageReader) {
            // We need wait until we have some size from onPreviewSizeChosen
            if (previewWidth == 0 || previewHeight == 0) {
                return
            }

            val image = imageReader.acquireLatestImage() ?: return
            fillBytes(image.planes, yuvBytes)

            ImageUtils.convertYUV420ToARGB8888(
                    yuvBytes[0]!!,
                    yuvBytes[1]!!,
                    yuvBytes[2]!!,
                    previewWidth,
                    previewHeight,
                    /*yRowStride=*/ image.planes[0].rowStride,
                    /*uvRowStride=*/ image.planes[1].rowStride,
                    /*uvPixelStride=*/ image.planes[1].pixelStride,
                    rgbBytes
            )

            // Create bitmap from int array
            val imageBitmap = Bitmap.createBitmap(
                    rgbBytes, previewWidth, previewHeight,
                    Bitmap.Config.ARGB_8888
            )

            // Create rotated version for portrait display
            val rotateMatrix = Matrix()
            rotateMatrix.postRotate(90.0f)

            val rotatedBitmap = Bitmap.createBitmap(
                    imageBitmap, 0, 0, previewWidth, previewHeight,
                    rotateMatrix, true
            )
            image.close()
            // Process an image for analysis in every 3 frames.


            processImage(imageBitmap)

        }
    }

    /** Crop Bitmap to maintain aspect ratio of model input.   */
    private fun cropBitmap(bitmap: Bitmap): Bitmap {
        val bitmapRatio = bitmap.height.toFloat() / bitmap.width
        val modelInputRatio = MODEL_HEIGHT.toFloat() / MODEL_WIDTH
        var croppedBitmap = bitmap

        // Acceptable difference between the modelInputRatio and bitmapRatio to skip cropping.
        val maxDifference = 1e-5

        // Checks if the bitmap has similar aspect ratio as the required model input.
        when {
            abs(modelInputRatio - bitmapRatio) < maxDifference -> return croppedBitmap
            modelInputRatio < bitmapRatio -> {
                // New image is taller so we are height constrained.
                val cropHeight = bitmap.height - (bitmap.width.toFloat() / modelInputRatio)
                croppedBitmap = Bitmap.createBitmap(
                        bitmap,
                        0,
                        (cropHeight / 2).toInt(),
                        bitmap.width,
                        (bitmap.height - cropHeight).toInt()
                )
            }
            else -> {
                val cropWidth = bitmap.width - (bitmap.height.toFloat() * modelInputRatio)
                croppedBitmap = Bitmap.createBitmap(
                        bitmap,
                        (cropWidth / 2).toInt(),
                        0,
                        (bitmap.width - cropWidth).toInt(),
                        bitmap.height
                )
            }
        }
        return croppedBitmap
    }

    /** Set the paint color and size.    */
    private fun setPaint() {
        paint.color = Color.RED
        paint.textSize = 80.0f
        paint.strokeWidth = 8.0f
    }

    private fun setPaint2() {
        paint.color = Color.BLUE
        paint.textSize = 80.0f
        paint.strokeWidth = 8.0f
    }
    private fun setPaint3() {
        paint.color = Color.WHITE
        paint.textSize = 80.0f
        paint.strokeWidth = 15.0f
    }
    private fun setPaint4() {
        paint.color = Color.GREEN
        paint.textSize = 80.0f
        paint.strokeWidth = 15.0f
    }

    /** Draw bitmap on Canvas.   */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun draw(canvas: Canvas, person: Person, bitmap: Bitmap) {
        if(frameCounter==60){

            stopRecording(true)

            mNextVideoAbsolutePath!!.let { tranfer_intant(it) }
        }
        frameCounter++
        Log.i("count",frameCounter.toString())

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        // Draw `bitmap` and `person` in square canvas.
        val screenWidth: Int
        val screenHeight: Int
        val left: Int
        val right: Int
        val top: Int
        val bottom: Int
        if (canvas.height > canvas.width) {
            screenWidth = canvas.width
            screenHeight = canvas.width
            left = 0
            top = (canvas.height - canvas.width) / 2
        } else {
            screenWidth = canvas.height
            screenHeight = canvas.height
            left = (canvas.width - canvas.height) / 2
            top = 0
        }
        right = left + screenWidth
        bottom = top + screenHeight

        setPaint()
        canvas.drawBitmap(
                bitmap,
                Rect(0, 0, bitmap.width, bitmap.height),
                Rect(left, top, right, bottom),
                paint
        )

        val widthRatio = screenWidth.toFloat() / MODEL_WIDTH
        val heightRatio = screenHeight.toFloat() / MODEL_HEIGHT
        var footkey=0
        // Draw key points over the image.
        for (keyPoint in person.keyPoints) {
            if(footkey==15) {
                setPaint2()
            }
            if (keyPoint.score > minConfidence) {
                val position = keyPoint.position
                if(footkey==15) {
                    setPaint2()
                    save_Vec1 = position.x.toFloat() * widthRatio + left
                    save_Vec2 = position.y.toFloat() * heightRatio + top
                }


                val adjustedX: Float = position.x.toFloat() * widthRatio + left
                val adjustedY: Float = position.y.toFloat() * heightRatio + top
                if(frameCounter==59){
                    key_list!!.add(footkey, Point(adjustedX.toInt(),adjustedY.toInt()))
                    Log.i("원",key_list.get(footkey).toString())
                    footkey++
                }
                canvas.drawCircle(adjustedX, adjustedY, circleRadius, paint)
            }
        }
        if(allkeyPoint.get(16)!=null) {
            val lsangle: Double = getAngle(allkeyPoint.get(5 * posi)!!, allkeyPoint.get(7 * posi)!!)
            System.out.println(" 왼쪽 어깨 각도 " + lsangle + " " + posi)
            val rsangle: Double = getAngle(allkeyPoint.get(6 * posi)!!, allkeyPoint.get(8 * posi)!!)
            System.out.println(" 오른쪽 어깨 각도 " + rsangle + " " + posi)
            val llangle: Double = getAngle(allkeyPoint.get(11 * posi)!!, allkeyPoint.get(13 * posi)!!)
            System.out.println(" 왼쪽 허벅지 각도 " + llangle + " " + posi)
            val rlangle: Double = getAngle(allkeyPoint.get(12 * posi)!!, allkeyPoint.get(14 * posi)!!)
            System.out.println(" 오른쪽 허벅지 각도 " + rlangle + " " + posi)
            val lhangle: Double = getAngle(allkeyPoint.get(5 * posi)!!, allkeyPoint.get(11 * posi)!!)
            System.out.println(" 왼쪽 허리 각도 " + lhangle + " " + posi)
            val rhangle: Double = getAngle(allkeyPoint.get(6 * posi)!!, allkeyPoint.get(12 * posi)!!)
            System.out.println(" 오른쪽 허리 각도 " + rhangle + " " + posi)
            val lfangle: Double = getAngle(allkeyPoint.get(13 * posi)!!, allkeyPoint.get(15 * posi)!!)
            System.out.println(" 왼쪽 정강이 각도 " + lfangle + " " + posi)
            val rfangle: Double = getAngle(allkeyPoint.get(14 * posi)!!, allkeyPoint.get(16 * posi)!!)
            System.out.println(" 오른쪽 정강이 각도 " + rfangle + " " + posi)
            posi++
        }
        setPaint()
        var bodykey=0
        for (line in bodyJoints) {

            if (
                    (person.keyPoints[line.first.ordinal].score > minConfidence) and
                    (person.keyPoints[line.second.ordinal].score > minConfidence)
            ) {
                if(frameCounter==59){
                    var startX=person.keyPoints[line.first.ordinal].position.x.toFloat() * widthRatio + left
                    var startY=person.keyPoints[line.first.ordinal].position.y.toFloat() * heightRatio + top
                    var stopX=person.keyPoints[line.second.ordinal].position.x.toFloat() * widthRatio + left
                    var stopY=person.keyPoints[line.second.ordinal].position.y.toFloat() * heightRatio + top
                    startPoint=Point(startX.toInt(),startY.toInt())
                    stopPoint=Point(stopX.toInt(),stopY.toInt())
                    start_joint_list!!.add(bodykey,startPoint!!)
                    stop_joint_list!!.add(bodykey,stopPoint!!)
                    bodykey++
                }
                canvas.drawLine(
                        person.keyPoints[line.first.ordinal].position.x.toFloat() * widthRatio + left,
                        person.keyPoints[line.first.ordinal].position.y.toFloat() * heightRatio + top,
                        person.keyPoints[line.second.ordinal].position.x.toFloat() * widthRatio + left,
                        person.keyPoints[line.second.ordinal].position.y.toFloat() * heightRatio + top,
                        paint
                )
            }
        }
        var resource=requireContext().resources
        var GoalpostImage = BitmapFactory.decodeResource(resource, R.drawable.goalpost1)
        canvas.drawBitmap(GoalpostImage,15.0f,120.0f,paint)
        setPaint4()
        canvas.drawLine(15.0f,180.0f,375.0f,180.0f,paint) //가로
        canvas.drawLine(15.0f,240.0f,375.0f,240.0f,paint)
        canvas.drawLine(135.0f,120.0f,135.0f,300.0f,paint)  //세로
        canvas.drawLine(255.0f,120.0f,255.0f,300.0f,paint)

        setPaint3()
        canvas.drawText(
                "거리: %.2f m".format(test.distance),
                (15.0f * widthRatio+right),
                (30.0f * heightRatio),
                paint
        )
        canvas.drawText(
                "디바이스: %s".format(posenet.device),
                (15.0f * widthRatio+right),
                (50.0f * heightRatio ),
                paint
        )
        canvas.drawText(
                "속도: %.2f ms".format(posenet.lastInferenceTimeNanos * 1.0f / 1_000_000),
                (15.0f * widthRatio+right),
                (70.0f * heightRatio),
                paint

        )
        setPaint3()

        if(frameCounter==59){
            point.givebitmap(bitmap,key_list,start_joint_list,stop_joint_list,Cachedir)

          /*  val time = System.currentTimeMillis() //시간 받기
            val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH.mm.ss")
            //포멧 변환  형식 만들기
            val dd = Date(time) //받은 시간을 Date 형식으로 바꾸기
            val dir: String = sdf.format(dd)+" (2)" //Data 정보를 포멧 변환하기
            feedbackimg = saveBitmapToJpeg(Save_Bitmap!!, dir,Cachedir!!)*/
        }

        // Draw!
        surfaceHolder!!.unlockCanvasAndPost(canvas)

    }


    /** Process image using Posenet library.   */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun processImage(bitmap: Bitmap) {
        // Crop bitmap.
        //val TrackingBitmap = tracking.trackingBall(bitmap)
        val croppedBitmap = cropBitmap(bitmap)

        // Created scaled version of bitmap for model input.
        val scaledBitmap = Bitmap.createScaledBitmap(croppedBitmap, MODEL_WIDTH, MODEL_HEIGHT, true)

        // Perform inference.
        val person = posenet.estimateSinglePose(scaledBitmap)
        var TrackingBitmap = test.trackingBall(scaledBitmap,Cachedir);
       // var TrackingGoal =test.trackingPost(TrackingBitmap)

        /*if((ballPoint.y-circlerat2)>100){

            TrackingBitmap=tracking.finishTrack(scaledBitmap);
        }*/
        val canvas: Canvas = surfaceHolder!!.lockCanvas()
        if(frameCounter==59){
            draw(canvas, person, scaledBitmap)
        }
        else draw(canvas, person, TrackingBitmap)
    }

    /**
     * Creates a new [CameraCaptureSession] for camera preview.
     */
    private fun createCameraPreviewSession() {
        try {

            if (mediaRecorder == null) {
                mediaRecorder = MediaRecorder()
            }

            mediaRecorder!!.setVideoEncodingBitRate(10000000)
            //mediaRecorder!!.setMaxDuration(10000) // 10 seconds
            mediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.SURFACE)

            mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder!!.setVideoFrameRate(20)

            val camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH)

            if (camcorderProfile.videoFrameWidth > previewSize!!.width
                    || camcorderProfile.videoFrameHeight > previewSize!!.height
            ) {
                camcorderProfile.videoFrameWidth = previewSize!!.width
                camcorderProfile.videoFrameHeight = previewSize!!.height
            }

            mediaRecorder!!.setVideoSize(
                    camcorderProfile.videoFrameWidth,
                    camcorderProfile.videoFrameHeight
            )

            mediaRecorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.H264)


            //mediaRecorder!!.setOrientationHint(90)
            if (mNextVideoAbsolutePath == null || mNextVideoAbsolutePath!!.isEmpty()) {
                mNextVideoAbsolutePath = getVideoFilePath()
            }
            mediaRecorder!!.setOutputFile(mNextVideoAbsolutePath)

            try {
                mediaRecorder!!.prepare()
            } catch (e: IOException) {
                e.printStackTrace()
                return
            }

            val surfaces: MutableList<Surface> = ArrayList()
            mRecorderSurface = mediaRecorder!!.surface
            // We capture images from preview in YUV format.
            imageReader = ImageReader.newInstance(
                    previewSize!!.width, previewSize!!.height, ImageFormat.YUV_420_888, 20
            )
            imageReader!!.setOnImageAvailableListener(imageAvailableListener, backgroundHandler)

            // This is the surface we need to record images for processing.
            recordingSurface = imageReader!!.surface

            // We set up a CaptureRequest.Builder with the output Surface.
            previewRequestBuilder = cameraDevice!!.createCaptureRequest(
                    TEMPLATE_PREVIEW
            )
            surfaces.add(recordingSurface!!)

            previewRequestBuilder!!.addTarget(recordingSurface)
            Log.i(TAG,"start")
            surfaces.add(mRecorderSurface!!)

            previewRequestBuilder!!.addTarget(mRecorderSurface)



            try {

                cameraDevice!!.createCaptureSession(
                        surfaces,
                        object : CameraCaptureSession.StateCallback() {
                            override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                                // The camera is already closed
                                if (cameraDevice == null) return

                                // When the session is ready, we start displaying the preview.
                                captureSession = cameraCaptureSession
                                try {
                                    // Auto focus should be continuous for camera preview.
                                    previewRequestBuilder!!.set(
                                            CaptureRequest.CONTROL_AF_MODE,
                                            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                                    )
                                    // Flash is automatically enabled when necessary.
                                    setAutoFlash(previewRequestBuilder!!)
                                    // Finally, we start displaying the camera preview.
                                    previewRequest = previewRequestBuilder!!.build()
                                    captureSession!!.setRepeatingRequest(
                                            previewRequest!!,
                                            null, null
                                    )

                                } catch (e: CameraAccessException) {
                                    Log.e(TAG, e.toString())
                                }
                            }

                            override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                                showToast("Failed")
                            }
                        },
                        null
                )
                mediaRecorder!!.start()

            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
            // Here, we create a CameraCaptureSession for camera preview.

        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        }
    }

    private fun setAutoFlash(requestBuilder: CaptureRequest.Builder) {
        if (flashSupported) {
            requestBuilder.set(
                    CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
            )
        }
    }




    private fun getVideoFilePath(): String? {
        val dir = Environment.getExternalStorageDirectory().absoluteFile
        val time = System.currentTimeMillis() //시간 받기

        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH.mm.ss")
        //포멧 변환  형식 만들기
        //포멧 변환  형식 만들기
        val dd = Date(time) //받은 시간을 Date 형식으로 바꾸기

        val strTime: String = sdf.format(dd) //Data 정보를 포멧 변환하기

        val path =
                dir.path + "/" + DETAIL_PATH
        val dst = File(path)
        if (!dst.exists()) dst.mkdirs()
        return path + strTime + ".mp4"
    }

    private fun stopRecording(showPreview: Boolean) {
        Log.i(TAG,"?щ줈2")


        if(showPreview){

            val file = File(mNextVideoAbsolutePath)

            if(!file.exists()){
                try {
                    Log.i("file ?놁쓬", "?뚯씪 ?곸쐞 ?붾젆?좊━ ?앹꽦")
                    file.mkdirs()
                } catch (e:Exception){
                    Log.e("path.mkdirs", e.toString())
                }
            }
            mediaRecorder!!.stop()
            mediaRecorder!!.reset()
            mediaRecorder!!.release()
            previewRequestBuilder=null
            mRecorderSurface!!.release()
            mRecorderSurface=null
            recordingSurface!!.release()
            recordingSurface=null
            previewRequest=null
            requireContext()!!.sendBroadcast(
                    Intent(
                        Intent(
                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.fromFile(file)
                        )
                    )
            )
            requireContext()!!.sendBroadcast(
                    Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.fromFile(file)
                    )
            )
            requireActivity().getApplicationContext().sendBroadcast(
                    Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.fromFile(file)
                    )
            )
            this@PosenetActivity.activity?.sendBroadcast(
                    Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.fromFile(file)
                    )
            )
            this@PosenetActivity.activity?.sendBroadcast(
                    Intent(
                        Intent(
                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.fromFile(file)
                        )
                    )
            )
            requireActivity().getApplicationContext().sendBroadcast( Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))

            closeCamera()
        }
    }

    private val captureStateCallback: CameraCaptureSession.StateCallback =
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    previewSession = session

                }
                override fun onConfigureFailed(session: CameraCaptureSession) {
                    showToast("Failed")
                }

            }

    fun getAngle(start: KeyPoint,end:KeyPoint):Double{
        val position = start.position
        val position2 = end.position

        val dx:Double=position.x.toDouble()-position2.x.toDouble()
        val dy:Double=position.y.toDouble()-position2.y.toDouble()
        val angle:Double=Math.atan2(dy,dx)*(180.0/ PI)
        return angle
    }





    /**
     * Shows an error message dialog.
     */
    class ErrorDialog : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
                AlertDialog.Builder(activity)
                        .setMessage(requireArguments().getString(ARG_MESSAGE))
                        .setPositiveButton(android.R.string.ok) { _, _ -> requireActivity().finish() }
                        .create()

        override fun show(childFragmentManager: FragmentManager, tag: String?) {

        }

        companion object {

            @JvmStatic
            private val ARG_MESSAGE = "message"

            @JvmStatic
            fun newInstance(message: String): ErrorDialog = ErrorDialog().apply {
                arguments = Bundle().apply { putString(ARG_MESSAGE, message) }
            }
        }
    }

    companion object {
        /**
         * Conversion from screen rotation to JPEG orientation.
         */
        private val ORIENTATIONS = SparseIntArray()
        private val FRAGMENT_DIALOG = "dialog"

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }

        /**
         * Tag for the [Log].
         */
        private const val TAG = "PosenetActivity"
        const val REQUEST_VIDEO_CAPTURE = 1
    }


    private fun saveBitmapToJpeg(bitmap: Bitmap, name: String, dir: File): String? {

        //내부저장소 캐시 경로를 받아옵니다.

        //저장할 파일 이름
        val fileName = "$name.jpg"
        var cachedir:String?=null
        //storage 에 파일 인스턴스를 생성합니다.
        val tempFile = File(dir, fileName)
        try {

            // 자동으로 빈 파일을 생성합니다.
            tempFile.createNewFile()

            // 파일을 쓸 수 있는 스트림을 준비합니다.
            val out = FileOutputStream(tempFile)

            // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)

            // 스트림 사용후 닫아줍니다.
            out.close()
            cachedir = tempFile.path
        } catch (e: FileNotFoundException) {
            Log.e("MyTag", "FileNotFoundException : " + e.message)
        } catch (e: IOException) {
            Log.e("MyTag", "IOException : " + e.message)
        }
        return cachedir
    }





    @SuppressLint("UseRequireInsteadOfGet")
    fun tranfer_intant(targetFilename: String) {
       // pose = this@PosenetActivity.requireActivity()
        val intent = Intent(context, shootingResult::class.java).apply {
        }
        intent.putExtra("key", targetFilename);
        intent.putExtra("trajectory",test.filename!!)
        intent.putExtra("feedback",point.filename!!)
        startActivity(intent)
        //this@PosenetActivity.activity?.finish()
        //requireActivity().finish()

    }

}