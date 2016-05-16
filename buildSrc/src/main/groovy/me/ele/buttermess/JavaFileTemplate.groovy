package me.ele.buttermess

class JavaFileTemplate {

  String packageName
  Map<String, String> map;

  String fillMapper() {
    String code = ""
    map.each { entry ->
      String injectedClazz = entry.key
      String viewInjectorClazz = entry.value
      code += String.format("map.put(Class.forName(\"%s\"), new %s());\n", injectedClazz,
          viewInjectorClazz)
    }
    return code
  }

  def getContent() {
    return """
package ${packageName}.azdb.tt;

import java.util.HashMap;
import java.util.Map;
import butterknife.ButterKnife.Injector;

public class ${ButterMessPlugin.DB_FILE} {

  private static Map<Class<?>, Injector<Object>> map = new HashMap<>();

  static {
      try {
        ${fillMapper()}
      } catch (Exception e) {
        e.printStackTrace();
      }
  }

  public static Map<Class<?>, Injector<Object>> a() {
    return map;
  }
}
"""
  }
}