package com.mammb.code.example.skija;

import io.github.humbleui.skija.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class App {

    public static void main(String[] args) throws Exception {

        Surface surface = Surface.makeRasterN32Premul(100, 100);
        Canvas canvas = surface.getCanvas();

        Paint paint = new Paint();
        paint.setColor(0xFFAA0000);
        canvas.drawCircle(50, 50, 30, paint);

        Image image = surface.makeImageSnapshot();
        Data pngData = image.encodeToData(EncodedImageFormat.PNG);

        try (ByteChannel channel = Files.newByteChannel(
                java.nio.file.Path.of("build/output.png"),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE);
             surface) {
            channel.write(pngData.toByteBuffer());
        }
    }
}
