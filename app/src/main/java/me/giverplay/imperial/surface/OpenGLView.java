package me.giverplay.imperial.surface;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class OpenGLView extends GLSurfaceView {

  public OpenGLView(Context context) {
    super(context);
    initialize();
  }

  public OpenGLView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initialize();
  }

  private void initialize() {
    setEGLContextClientVersion(2);
    setPreserveEGLContextOnPause(true);
    setRenderer(new OpenGLRenderer());
  }
}
