package me.giverplay.imperial.objects;

import static android.opengl.GLES20.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import me.giverplay.imperial.surface.ShaderUtils;

public class Triangle {

  private static final float[] coordinates = {
    0.0f, 0.5f, 0.0f,
    0.5f, 0.0f, 0.0f,
    -0.5f, 0.0f, 0.0f,
  };

  private static final float[] colors = {
    1.0f, 0.0f, 0.0f, 1.0f,
  };

  private static final int coordinatePoints = 3;
  private static final int vertexCount = coordinates.length / coordinatePoints;
  private static final int vertexStride = coordinatePoints * 4;

  private static final String vertexShaderSource =
    "attribute vec4 vPosition; void main() { gl_Position = vPosition; }";

  private static final String fragmentShaderSource =
    "precision mediump float; uniform vec4 vColor; void main() { gl_FragColor = vColor; }";

  private final FloatBuffer vertexBuffer;

  private final int shaderProgram;

  public Triangle() {
    ByteBuffer buffer = ByteBuffer.allocateDirect(coordinates.length * 4);
    buffer.order(ByteOrder.nativeOrder());

    vertexBuffer = buffer.asFloatBuffer();
    vertexBuffer.put(coordinates);
    vertexBuffer.position(0);

    int vertexShader = ShaderUtils.loadShader(GL_VERTEX_SHADER, vertexShaderSource);
    int fragmentShader = ShaderUtils.loadShader(GL_FRAGMENT_SHADER, fragmentShaderSource);

    shaderProgram = glCreateProgram();
    glAttachShader(shaderProgram, vertexShader);
    glAttachShader(shaderProgram, fragmentShader);
    glLinkProgram(shaderProgram);
  }

  public void draw() {
    glUseProgram(shaderProgram);

    int positionHandle = glGetAttribLocation(shaderProgram, "vPosition");
    glEnableVertexAttribArray(positionHandle);
    glVertexAttribPointer(positionHandle, coordinatePoints, GL_FLOAT, false, vertexStride, vertexBuffer);

    int colorHandle = glGetUniformLocation(shaderProgram, "vColor");
    glUniform4fv(colorHandle, 1, colors, 0);

    glDrawArrays(GL_TRIANGLES, 0, vertexCount);
    glDisableVertexAttribArray(positionHandle);
  }
}
