package me.ele.buttermessdemo;

import android.app.Application;
import me.ele.buttermess.ButterMess;

public class App extends Application {

  @Override public void onCreate() {
    super.onCreate();
    ButterMess.init();
  }
}
