package SNAKE;

public class node {
  public node(int _x, int _y, int _step) {
    this.x = _x;
    this.y = _y;
    this.step = _step;
  }
  public boolean equals(node other) {
    if (x == other.x && y == other.y)
      return true;
    return false;
  }
  public int x;
  public int y;
  public int step;
}