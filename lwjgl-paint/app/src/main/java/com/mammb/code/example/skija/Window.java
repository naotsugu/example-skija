package com.mammb.code.example.skija;

import io.github.humbleui.skija.BackendRenderTarget;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.ColorSpace;
import io.github.humbleui.skija.DirectContext;
import io.github.humbleui.skija.FramebufferFormat;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Surface;
import io.github.humbleui.skija.SurfaceColorFormat;
import io.github.humbleui.skija.SurfaceOrigin;
import io.github.humbleui.skija.impl.Library;
import io.github.humbleui.skija.impl.Stats;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.glfw.GLFW;

public class Window {

    int width = 320, height = 240;
    public long windowHandle;

    private DirectContext context;
    private BackendRenderTarget renderTarget;
    private Surface surface;
    private Canvas canvas;


    public void run() {
        createWindow();
        loop();

        Callbacks.glfwFreeCallbacks(windowHandle);
        GLFW.glfwDestroyWindow(windowHandle);
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    private void createWindow() {

        // optional, the current window hints are already the default
        GLFW.glfwDefaultWindowHints();
        // the window will stay hidden after creation
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        // the window will be resizable
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        windowHandle = GLFW.glfwCreateWindow(width, height, "Skija",
                MemoryUtil.NULL, MemoryUtil.NULL);
        if (windowHandle == MemoryUtil.NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        GLFW.glfwMakeContextCurrent(windowHandle);
        GLFW.glfwSwapInterval(1); // Enable v-sync
        GLFW.glfwShowWindow(windowHandle);
    }


    private void loop() {

        // Initialize OpenGL. Do once per app launch
        GL.createCapabilities();

        if ("false".equals(System.getProperty("skija.staticLoad")))
            Library.load();
        // Create Skia OpenGL context. Do once per app launch
        context = DirectContext.makeGL();

        initSkia();

        // Render loop
        while (!GLFW.glfwWindowShouldClose(windowHandle)) {

            // DRAW HERE!!!
            try (Paint paint = new Paint()) {
                paint.setColor(0xFF388E3C);
                canvas.drawCircle(50, 50, 30, paint);
            }
            // DRAW HERE!!!

            context.flush();
            GLFW.glfwSwapBuffers(windowHandle); // wait for v-sync
            GLFW.glfwPollEvents();
        }
    }


    private void initSkia() {
        Stats.enabled = true;
        if (surface != null) surface.close();
        if (renderTarget != null) renderTarget.close();

        // Create render target, surface and retrieve canvas from it
        // .close() and recreate on window resize
        int fbId = GL11.glGetInteger(0x8CA6); // GL_FRAMEBUFFER_BINDING
        renderTarget = BackendRenderTarget.makeGL(
                width,
                height,
                /*samples*/0,
                /*stencil*/8,
                fbId,
                FramebufferFormat.GR_GL_RGBA8);

        // .close() and recreate on window resize
        surface = Surface.makeFromBackendRenderTarget(
                context,
                renderTarget,
                SurfaceOrigin.BOTTOM_LEFT,
                SurfaceColorFormat.RGBA_8888,
                ColorSpace.getSRGB());

        // do not .close() - Surface manages its lifetime here
        canvas = surface.getCanvas();
    }


}
