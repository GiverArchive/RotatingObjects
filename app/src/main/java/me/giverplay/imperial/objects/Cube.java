package me.giverplay.imperial.objects;

import static android.opengl.GLES20.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import me.giverplay.imperial.surface.ShaderUtils;

public class Cube {

  private static final float VERTICES[] = {
    -0.5f, -0.5f, -0.5f,
    0.5f, -0.5f, -0.5f,
    0.5f, 0.5f, -0.5f,
    -0.5f, 0.5f, -0.5f,
    -0.5f, -0.5f, 0.5f,
    0.5f, -0.5f, 0.5f,
    0.5f, 0.5f, 0.5f,
    -0.5f, 0.5f, 0.5f
  };

  private static final float COLORS[] = {
    0.0f, 1.0f, 1.0f, 1.0f,
    1.0f, 0.0f, 0.0f, 1.0f,
    1.0f, 1.0f, 0.0f, 1.0f,
    0.0f, 1.0f, 0.0f, 1.0f,
    0.0f, 0.0f, 1.0f, 1.0f,
    1.0f, 0.0f, 1.0f, 1.0f,
    1.0f, 1.0f, 1.0f, 1.0f,
    0.0f, 1.0f, 1.0f, 1.0f,
  };

  private static final byte INDICES[] = {
    0, 1, 3, 3, 1, 2,
    0, 1, 4, 4, 5, 1,
    1, 2, 5, 5, 6, 2,
    2, 3, 6, 6, 7, 3,
    3, 7, 4, 4, 3, 0,
    4, 5, 7, 7, 6, 5,
  };

  private static final int COORDS_PER_VERTEX = 3;
  private static final int VALUES_PER_COLOR = 4;

  private final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
  private final int COLOR_STRIDE = VALUES_PER_COLOR * 4;

  private static final String VERTEX_SHADER_SOURCE =
    "uniform mat4 uMVPMatrix;" +
      "attribute vec4 vPosition;" +
      "attribute vec4 vColor;" +
      "varying vec4 _vColor;" +
      "void main() {" +
      "  _vColor = vColor;" +
      "  gl_Position = uMVPMatrix * vPosition;" +
      "}";

  private static final String FRAGMENT_SHADER_SOURCE =
    "precision mediump float;" +
      "varying vec4 _vColor;" +
      "void main() {" +
      "  gl_FragColor = _vColor;" +
      "}";

  private final FloatBuffer vertexBuffer;
  private final FloatBuffer colorBuffer;
  private final ByteBuffer indexBuffer;

  private final int shaderProgram;
  private final int positionHandle;
  private final int colorHandle;
  private final int mvpMatrixHandle;

  public Cube() {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(VERTICES.length * 4);

    byteBuffer.order(ByteOrder.nativeOrder());
    vertexBuffer = byteBuffer.asFloatBuffer();
    vertexBuffer.put(VERTICES);
    vertexBuffer.position(0);

    byteBuffer = ByteBuffer.allocateDirect(COLORS.length * 4);
    byteBuffer.order(ByteOrder.nativeOrder());
    colorBuffer = byteBuffer.asFloatBuffer();
    colorBuffer.put(COLORS);
    colorBuffer.position(0);

    indexBuffer = ByteBuffer.allocateDirect(INDICES.length);
    indexBuffer.put(INDICES);
    indexBuffer.position(0);

    shaderProgram = glCreateProgram();
    glAttachShader(shaderProgram, ShaderUtils.loadShader(GL_VERTEX_SHADER, VERTEX_SHADER_SOURCE));
    glAttachShader(shaderProgram, ShaderUtils.loadShader(GL_FRAGMENT_SHADER, FRAGMENT_SHADER_SOURCE));
    glLinkProgram(shaderProgram);

    positionHandle = glGetAttribLocation(shaderProgram, "vPosition");
    colorHandle = glGetAttribLocation(shaderProgram, "vColor");
    mvpMatrixHandle = glGetUniformLocation(shaderProgram, "uMVPMatrix");
  }

  public void draw(float[] mvpMatrix) {
    glUseProgram(shaderProgram);

    glEnableVertexAttribArray(positionHandle);
    glVertexAttribPointer(positionHandle, 3, GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);

    glEnableVertexAttribArray(colorHandle);
    glVertexAttribPointer(colorHandle, 4, GL_FLOAT, false, COLOR_STRIDE, colorBuffer);

    glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

    glDrawElements(GL_TRIANGLES, INDICES.length, GL_UNSIGNED_BYTE, indexBuffer);

    glDisableVertexAttribArray(positionHandle);
    glDisableVertexAttribArray(colorHandle);
  }
}
