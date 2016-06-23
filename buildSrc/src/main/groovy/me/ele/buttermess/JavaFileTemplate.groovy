package me.ele.buttermess

class JavaFileTemplate {

  String[] keyWords = ["do",
                       "if",
                       "for",
                       "int",
                       "new",
                       "try",
                       "byte",
                       "case",
                       "char",
                       "else",
                       "goto",
                       "long",
                       "this",
                       "void",
                       "break",
                       "catch",
                       "class",
                       "const",
                       "final",
                       "float",
                       "short",
                       "super",
                       "throw",
                       "while",
                       "double",
                       "import",
                       "native",
                       "public",
                       "return",
                       "static",
                       "switch",
                       "throws",
                       "boolean",
                       "default",
                       "extends",
                       "finally",
                       "package",
                       "private",
                       "abstract",
                       "continue",
                       "strictfp",
                       "volatile",
                       "interface",
                       "protected",
                       "transient",
                       "implements",
                       "instanceof",
                       "synchronized"]

  String packageName
  Map<String, String> map;

  String fillMapper() {
    String code = ""
    map.each { entry ->
      String injectedClazz = entry.key
      String viewInjectorClazz = entry.value
      // key must use reflection because there are some private or protected class
      String key = "Class.forName(\"${injectedClazz}\")"
      String value = containsKeyWord(viewInjectorClazz) ?
          "Class.forName(\"${viewInjectorClazz}\").newInstance()" : "${viewInjectorClazz}()"
      code += String.format("map.put(%s, new %s);\n", key, value)
    }
    return code
  }

  boolean containsKeyWord(String clazz) {
    String[] names = clazz.split("\\.")
    for (String name : names) {
      if (keyWords.contains(name)) {
        return true
      }
    }
    return false
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