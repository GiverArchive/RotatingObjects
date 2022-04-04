package me.giverplay.imperial.surface;

import static android.opengl.GLES20.*;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import me.giverplay.imperial.objects.Cube;

public class OpenGLRenderer implements GLSurfaceView.Renderer {

  private static final float rotationIncrement = 0.7f;

  private final float[] mvpMatrix;
  private final float[] projectionMatrix;
  private final float[] viewMatrix;
  private final float[] rotationMatrix;
  private final float[] finalMvpMatrix;

  private float objectRotation;
  private long lastUpdate;

  private Cube cube;

  public OpenGLRenderer() {
    mvpMatrix = new float[16];
    projectionMatrix = new float[16];
    viewMatrix = new float[16];
    rotationMatrix = new float[16];
    finalMvpMatrix = new float[16];

    Matrix.setLookAtM(viewMatrix, 0,
      0.0f, 0.0f, -4.0f,
      0.0f, 0.0f, 0.0f,
      0.0f, 1.0f, 0.0f);
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    glClearDepthf(1.0f);
    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LEQUAL);

    cube = new Cube();
  }

  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height) {
    glViewport(0, 0, width, height);

    float ratio = (float) width / height;

    Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 3.0f, 7.0f);
    Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
  }

  @Override
  public void onDrawFrame(GL10 gl) {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    Matrix.setRotateM(rotationMatrix, 0, objectRotation, 1.0f, 1.0f, 1.0f);
    Matrix.multiplyMM(finalMvpMatrix, 0, mvpMatrix, 0, rotationMatrix, 0);

    cube.draw(finalMvpMatrix);
    rotate();
  }

  private void rotate() {
    if(lastUpdate != 0) {
      float delta = (SystemClock.elapsedRealtime() - lastUpdate) / 25.0f;
      objectRotation += rotationIncrement * delta;
    }

    lastUpdate = SystemClock.elapsedRealtime();
  }
}
