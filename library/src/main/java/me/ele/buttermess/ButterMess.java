package me.ele.buttermess;

import butterknife.ButterKnife;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class ButterMess {
  public static void init() {
    try {
      Field field = ButterKnife.class.getDeclaredField("INJECTORS");
      field.setAccessible(true);
      Map<Class<?>, ButterKnife.Injector<Object>> map =
          (Map<Class<?>, ButterKnife.Injector<Object>>) field.get(null);
      map.clear();
      Class butterDb = Class.forName(Utils.getPackageName() + ".azdb.tt.bmdb");
      Method method = butterDb.getDeclaredMethod("a", null);
      map.putAll(
          (Map<? extends Class<?>, ? extends ButterKnife.Injector<Object>>) method.invoke(null, null));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
