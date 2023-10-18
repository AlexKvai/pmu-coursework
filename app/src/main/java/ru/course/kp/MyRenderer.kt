package ru.course.kp

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import ru.course.kp.gl.GLObject
import ru.course.kp.gl.IGLObject
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class MyRenderer(ctx: Context) : GLSurfaceView.Renderer {
    private val objects: MutableList<IGLObject> = mutableListOf()
    private val bgColorR: Float = 0.0f
    private val bgColorG: Float = 0.0f
    private val bgColorB: Float = 0.0f
    private val bgColorA: Float = 1.0f

    init {
        val baseShader = Utils.inputStreamToString(ctx.assets.open("shaders/base_shader.vert"))
        val colorShader = Utils.inputStreamToString(ctx.assets.open("shaders/color_shader.frag"))
        val textureShader =
            Utils.inputStreamToString(ctx.assets.open("shaders/texture_shader.frag"))

        val table = GLObject.fromInputStream(
            baseShader,
            textureShader,
            ctx.assets.open("models/table.obj"),
            ctx.assets.open("models/tablecheck.jpeg")
        )
        table.setPosition(0f, -2f, -10f)
        table.setScale(2f, 2f, 2f)

        val candle = GLObject.fromInputStream(
            baseShader,
            colorShader,
            ctx.assets.open("models/candle.obj")
        )
        candle.setPosition(0.32f, -0.45f, -9f)
        candle.setScale(0.005f, 0.005f, 0.005f)

        val glass = GLObject.fromInputStream(
            baseShader,
            colorShader,
            ctx.assets.open("models/glass.obj")
        )
        glass.setPosition(0f, -0.5f, -10f)
        glass.setScale(0.1f, 0.1f, 0.1f)

        val apple = GLObject.fromInputStream(
            baseShader,
            textureShader,
            ctx.assets.open("models/apple.obj"),
            ctx.assets.open("models/apple.png")
        )
        apple.setPosition(0.7f, -0.5f, -10f)
        apple.setScale(0.5f, 0.5f, 0.5f)

        val pear = GLObject.fromInputStream(
            baseShader,
            textureShader,
            ctx.assets.open("models/pear.obj"),
            ctx.assets.open("models/pear.jpg")
        )
        pear.setPosition(1.2f, -0.22f, -9f)
        pear.setScale(0.05f, 0.05f, 0.05f)
        pear.setRotation(-80f, 0f, 0f)

        val banana = GLObject.fromInputStream(
            baseShader,
            colorShader,
            ctx.assets.open("models/banana.obj"),
        )
        banana.setPosition(-1.7f, -0.3f, -10f)
        banana.setScale(0.05f, 0.05f, 0.05f)
        banana.setColor(1f, 1f, 0f)
        banana.rotateX = 180f

        val avocado = GLObject.fromInputStream(
            baseShader,
            textureShader,
            ctx.assets.open("models/avocado.obj"),
            ctx.assets.open("models/avocado.png")
        )
        avocado.setPosition(-0.7f, -0.27f, -9f)
        avocado.setScale(0.04f, 0.04f, 0.04f)
        avocado.rotateX = 160f

        val orange = GLObject.fromInputStream(
            baseShader,
            colorShader,
            ctx.assets.open("models/lime.obj"),
        )
        orange.setPosition(1.8f, -0.35f, -10f)
        orange.setScale(0.05f, 0.05f, 0.05f)
        orange.setColor(3f, 2f, 0f)
        orange.rotateX = 90f

        objects.add(orange)
        objects.add(glass)
        objects.add(apple)
        objects.add(pear)
        objects.add(banana)
        objects.add(avocado)
        objects.add(candle)
        objects.add(table)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        glEnable(GL_DEPTH_TEST)
        glDepthFunc(GL_LEQUAL)

        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        for (obj in objects) {
            obj.onSurfaceCreated(gl, config)
        }
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        glViewport(0, 0, width, height)

        for (obj in objects) {
            obj.onSurfaceChanged(gl, width, height)
        }
    }

    override fun onDrawFrame(gl: GL10) {
        glClearColor(bgColorR, bgColorG, bgColorB, bgColorA)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        val candle = (objects[objects.size - 2] as GLObject)

        for (obj in objects) {
            //            (obj as GLObject).rotateY += 1f
            obj.setLightDirection(candle.x - obj.x, candle.y + 1f - obj.y, candle.z - obj.z)
            obj.onDrawFrame(gl)
        }
    }
}