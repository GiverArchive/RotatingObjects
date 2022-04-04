package me.giverplay.imperial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import me.giverplay.imperial.surface.OpenGLView;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(new OpenGLView(this));
  }
}