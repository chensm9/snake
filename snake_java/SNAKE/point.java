package SNAKE;

public class point {
  public point(int a, int b, point f, point n) {
    x = a;
    y = b;
    front = f;
    next = n;
  }
  public int x;
  public int y;
  public point next;
  public point front;
  public String color;
}