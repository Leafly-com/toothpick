package toothpick.configuration;

import java.util.List;

public class CyclicDependencyException extends RuntimeException {

  private static final int MARGIN_SIZE = 3;
  private static final String LINE_SEPARATOR = System.getProperty("line.separator");

  public CyclicDependencyException() {
  }

  public CyclicDependencyException(String message) {
    super(message);
  }

  public CyclicDependencyException(String message, Throwable cause) {
    super(message, cause);
  }

  public CyclicDependencyException(Throwable cause) {
    super(cause);
  }

  public CyclicDependencyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public CyclicDependencyException(List<Class> path, Class startClass) {
    this(String.format("Class %s creates a cycle:%n%s", startClass.getName(), format(path, startClass)));
  }

  private static String format(List<Class> path, Class startClass) {
    if (path.size() == 0) {
      throw new IllegalArgumentException();
    }

    int classPosition = Math.max(path.indexOf(startClass), 0);
    path = path.subList(classPosition, path.size());

    int maxWordLength = findLongestClassNameLength(path);
    int middleWordPos = maxWordLength / 2 + MARGIN_SIZE;
    int loopLinePosition = maxWordLength + 2 * MARGIN_SIZE;
    StringBuilder builder = new StringBuilder();

    addTopLines(builder, middleWordPos, loopLinePosition);
    for (Class clazz : path) {
      addLine(builder, clazz.getName(), middleWordPos, loopLinePosition);
      addLine(builder, "||", middleWordPos, loopLinePosition);
    }
    addHorizontalLine(builder, middleWordPos, loopLinePosition);

    return builder.toString();
  }

  private static void addTopLines(StringBuilder builder, int middleWordPos, int loopLinePosition) {
    builder.append(LINE_SEPARATOR);
    addHorizontalLine(builder, middleWordPos, loopLinePosition);
    addLine(builder, "||", middleWordPos, loopLinePosition);
    addLine(builder, "\\/", middleWordPos, loopLinePosition);
  }

  private static void addHorizontalLine(StringBuilder builder, int middleWordPos, int loopLinePosition) {
    builder.append(repeat(' ', middleWordPos));
    builder.append(repeat('=', loopLinePosition - middleWordPos + 1));
    builder.append(LINE_SEPARATOR);
  }

  private static void addLine(StringBuilder builder, String content, int middleWordPos, int loopLinePosition) {
    int leftMarginSize = middleWordPos - content.length() / 2;
    int rightMarginSize = loopLinePosition - leftMarginSize - content.length();
    builder.append(repeat(' ', leftMarginSize));
    builder.append(content);
    builder.append(repeat(' ', rightMarginSize));
    builder.append("||");
    builder.append(LINE_SEPARATOR);
  }

  private static int findLongestClassNameLength(List<Class> path) {
    int length, max = 0;
    for (Class clazz : path) {
      length = clazz.getName().length();
      if (length > max) {
        max = length;
      }
    }
    return max;
  }

  private static String repeat(char c, int n) {
    return new String(new char[n]).replace('\0', c);
  }
}
