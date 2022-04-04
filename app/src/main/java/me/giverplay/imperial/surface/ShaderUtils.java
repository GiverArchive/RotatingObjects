package me.giverplay.imperial.surface;

import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glShaderSource;

public final class ShaderUtils {
  private ShaderUtils() { }

  public static int loadShader(int type, String shaderCode){
    int shader = glCreateShader(type);

    glShaderSource(shader, shaderCode);
    glCompileShader(shader);

    return shader;
  }
}
