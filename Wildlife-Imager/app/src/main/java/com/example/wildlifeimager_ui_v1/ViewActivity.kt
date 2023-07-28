package com.example.wildlifeimager_ui_v1

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.sqrt


class ViewActivity : AppCompatActivity() {

    private lateinit var glSurfaceView: GLSurfaceView
    private lateinit var renderer: MyGLRenderer
    private lateinit var originalModelMatrix: FloatArray


    //    private val pointCloud = intent.getFloatArrayExtra("floatArray")
    private lateinit var backBtn: Button
    private lateinit var resetViewBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        backBtn = findViewById(R.id.backBtn)
        resetViewBtn = findViewById(R.id.resetViewBtn)

        val pointCloudIntent = intent.getFloatArrayExtra("floatArray")

        glSurfaceView = findViewById(R.id.glsurfaceview)
        glSurfaceView.setEGLContextClientVersion(2)

        renderer = pointCloudIntent?.let { MyGLRenderer(it) }!!
        glSurfaceView.setRenderer(renderer)

        backBtn.setOnClickListener {
            onBackPressed()
        }

        resetViewBtn.setOnClickListener {
            renderer.resetModelMatrix()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                renderer.setTouchDown(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                renderer.rotateModel(event.x, event.y)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                renderer.setZoomStartDistance(event)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                renderer.setZoomEndDistance(event)
            }
        }
        return true
    }
}

class MyGLRenderer(private val pointCloud: FloatArray) : GLSurfaceView.Renderer {

    private val COORDS_PER_VERTEX = 3

    private lateinit var vertexBuffer: FloatBuffer

    private val originalModelMatrix = FloatArray(16)

    init {
        val vertexArray = pointCloud

        if (vertexArray != null) {
            val buffer = ByteBuffer.allocateDirect(vertexArray.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexArray)
            buffer.position(0)

            vertexBuffer = buffer
        }
    }

    fun resetModelMatrix() {
        Matrix.setIdentityM(modelMatrix, 0)
    }


    private val vertexShaderCode = """
        attribute vec4 vPosition;
        uniform mat4 uMVPMatrix;
        void main() {
            gl_Position = uMVPMatrix * vPosition;
            gl_PointSize = 7.0;
        }
    """.trimIndent()

    private val fragmentShaderCode = """
        precision mediump float;
        void main() {
            gl_FragColor = vec4(34.0, 247.0, 34.0, 1.0);
        }
    """.trimIndent()

    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val mvpMatrix = FloatArray(16)
    private var programId = 0
    private var mPositionHandle = 0
    private var mMVPMatrixHandle = 0
    private var previousX = 0f
    private var previousY = 0f
    private var scaleFactor = 1f
    private var zoomStartDistance = 0f
    private var zoomEndDistance = 0f

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        GLES20.glClearColor(0f, 0f, 0f, 0.1f)
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        programId = GLES20.glCreateProgram()
        GLES20.glAttachShader(programId, vertexShader)
        GLES20.glAttachShader(programId, fragmentShader)
        GLES20.glLinkProgram(programId)
        GLES20.glUseProgram(programId)

        Matrix.setIdentityM(originalModelMatrix, 0)

        mPositionHandle = GLES20.glGetAttribLocation(programId, "vPosition")
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programId, "uMVPMatrix")
        Matrix.setIdentityM(modelMatrix, 0)
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()
        val near = 1.0f
        val far = 100.0f
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, near, far)
    }

    override fun onDrawFrame(unused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        GLES20.glUseProgram(programId)
        GLES20.glVertexAttribPointer(
            mPositionHandle,
            COORDS_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            0,
            vertexBuffer
        )
        GLES20.glEnableVertexAttribArray(mPositionHandle)
        Matrix.setLookAtM(viewMatrix, 0, 20f, 20f, 0f, 0f, 0f, 0f, 0f, 0f, 1.0f)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, modelMatrix, 0)
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 896)
        //todo change the above line to match num points
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }

    fun setTouchDown(x: Float, y: Float) {
        previousX = x
        previousY = y
    }

    fun rotateModel(x: Float, y: Float) {
        val deltaX = x - previousX
        val deltaY = y - previousY

        Matrix.rotateM(modelMatrix, 0, deltaX, 0f, 1f, 0f)
        Matrix.rotateM(modelMatrix, 0, deltaY, 1f, 0f, 0f)

        previousX = x
        previousY = y
    }

    fun setZoomStartDistance(event: MotionEvent) {
        val x1 = event.getX(0)
        val y1 = event.getY(0)
        val x2 = event.getX(1)
        val y2 = event.getY(1)
        zoomStartDistance = calculateDistance(x1, y1, x2, y2)
    }

    fun setZoomEndDistance(event: MotionEvent) {
        val x1 = event.getX(0)
        val y1 = event.getY(0)
        val x2 = event.getX(1)
        val y2 = event.getY(1)
        zoomEndDistance = calculateDistance(x1, y1, x2, y2)
        val scaleFactorDiff = zoomEndDistance / zoomStartDistance
        scaleFactor *= scaleFactorDiff
        zoomStartDistance = 0f
        zoomEndDistance = 0f
        Matrix.scaleM(modelMatrix, 0, scaleFactorDiff, scaleFactorDiff, scaleFactorDiff)
    }

    private fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val dx = x2 - x1
        val dy = y2 - y1
        return sqrt(dx * dx + dy * dy)
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        return shader
    }
}
