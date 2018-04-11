import SNAKE.*;
import java.util.Random;
import java.util.LinkedList;
import java.util.Queue;

public class snake {

  public enum direction { up, down, right, left };

  public static void main(String[] argv) {
    try{
      initial();
      clear();
      while (true) {
        goto_0_0();
        find_dir();
        if (move())
          break;
        printMap();
        Thread.sleep(15);
      }
    }
    catch(Exception e){
      System.out.println("error");
    }
  }

  public static void initial() {
    Random random = new Random();
    tail = new point(1, 1, null, null);
    snake = new point(1, 2, null, null);
    tail.color =  ""+(random.nextInt(7) + 41);
    snake.color =  ""+(random.nextInt(7) + 41);
    map = new char[N][M];
    for (int i = 0; i < N; i++)
      for (int j = 0; j < M; j++)
        if (i == 0||j == 0 || i == N-1||j == M-1)
          map[i][j] = Wall;
        else
          map[i][j] = Empty;

    snake.next = tail;
    tail.front = snake;
    map[1][1] = body;
    map[1][2] = head;
    creatFood();
    dir = direction.right;
    score = 0;
  }

  public static void resume() {
    for (int i = 0; i < N; i++)
      for (int j = 0; j < M; j++)
        if (map[i][j] == '#')
          map[i][j] = Empty;
    map[food_x][food_y] = food;
    map[tail.x][tail.y] = body;
  }

  public static void printMap(){
    System.out.println("score: " + score);
    map[tail.x][tail.y] = Empty;
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < M; j++){
        if(map[i][j] == body||map[i][j] == head){
          point k = snake;
          while(k.x != i||k.y != j){
            k = k.next;
          }
          System.out.print("\033[31;"+ k.color+"m \033[0m");
        }
        else if(map[i][j] == Wall){
          System.out.print("\033[31;47m \033[0m");
        }
        else
          System.out.print("" + map[i][j]);
      }
      System.out.print("\n");
    }
    map[tail.x][tail.y] = body;
  }

  public static void creatFood() {
    score++;
	  Random random = new Random();
	  while (map[food_x][food_y] != Empty) {
		  food_x = random.nextInt(N-1) + 1;
		  food_y = random.nextInt(N-1) + 1;
	  }
	  map[food_x][food_y] = food;
  }

  public static boolean move() {
    Random random = new Random();
    point q = new point(snake.x, snake.y, null, snake);
    // q.color = ""+(random.nextInt(7) + 41);
    point q2 = q;
    while(q2.next != null){
      q2.color = q2.next.color;
      q2 = q2.next;
    }
    map[tail.x][tail.y] = Empty;

    if (dir == direction.right)
      q.y++;
    else if (dir == direction.left)
      q.y--;
    else if (dir == direction.up)
      q.x--;
    else if (dir == direction.down)
      q.x++;

    if (map[q.x][q.y] == body || map[q.x][q.y] == Wall) {
      return true;
    }

    if (q.x == food_x && q.y == food_y) {
      map[q.x][q.y] = head;
      map[snake.x][snake.y] = body;
      snake.front = q;
      snake = q;
      map[tail.x][tail.y] = body;
      tail.color = ""+(random.nextInt(7) + 41);
      creatFood();
      return false;
    } else {
      map[q.x][q.y] = head;
      map[snake.x][snake.y] = body;
      snake.front = q;
      snake = q;
      point p = tail;
      tail = tail.front;
      tail.next = null;
      return false;
    }
  }

  public static void find_dir() {
    if (dir == direction.left || dir == direction.right) {
      direction[] d = {dir, direction.up, direction.down};
      int[] a = {find_food(dir), find_food(direction.up),
                 find_food(direction.down)};
      for (int i = 0; i < 2; i++)
        for (int j = i + 1; j < 3; j++)
          if (a[i] > a[j]) {
            int t = a[i];
            a[i] = a[j];
            a[j] = t;

            direction temp = d[i];
            d[i] = d[j];
            d[j] = d[i];
          }
      for (int i = 0; i < 3; i++)
        if (find_tail(d[i])) {
          dir = d[i];
          return;
        }
    } else {
      direction[] d = {dir, direction.left, direction.right};
      int[] a = {find_food(dir), find_food(direction.left),
                 find_food(direction.right)};

      for (int i = 0; i < 2; i++) {
        for (int j = i + 1; j < 3; j++)
          if (a[i] > a[j]) {
            int t = a[i];
            a[i] = a[j];
            a[j] = t;

            direction temp = d[i];
            d[i] = d[j];
            d[j] = d[i];
          }
      }

      for (int i = 0; i < 3; i++) {
        if (find_tail(d[i])) {
          dir = d[i];
          return;
        }
      }
    }
  }

  public static int find_food(direction d) {
    point p = Move(d);
    if (map[p.x][p.y] == Wall || map[p.x][p.y] == body)
      return 10000;
    else {
      node begin = new node(p.x, p.y, 0);
      node end = new node(food_x, food_y, 0);
      Queue<node> que = new LinkedList<node>();
      que.offer(begin);
      map[begin.x][begin.y] = '#';
      int[][] move_d = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
      while (!que.isEmpty()) {
        node temp = que.peek(), cur = null;
        que.poll();
        if (temp.equals(end)) {
          resume();
          return temp.step;
        }
        for (int i = 0; i < 4; i++) {
          cur = new node(temp.x + move_d[i][0], temp.y + move_d[i][1],
                         temp.step + 1);
          if (cur.x > 0 && cur.x < N && cur.y > 0 && cur.y < M &&
              (map[cur.x][cur.y] == Empty || map[cur.x][cur.y] == food)) {
            que.offer(cur);
            map[cur.x][cur.y] = '#';
          }
        }
      }
      resume();
      return 10000;
    }
  }

  public static point Move(direction d) {
    point q = new point(snake.x, snake.y, null, null);
    if (d == direction.right)
      q.y++;
    else if (d == direction.left)
      q.y--;
    else if (d == direction.up)
      q.x--;
    else if (d == direction.down)
      q.x++;
    return q;
  }

  public static boolean find_tail(direction d) {
    point p = Move(d);
    if (p.x == tail.x && p.y == tail.y)
      return true;
    else if (map[p.x][p.y] == Wall || map[p.x][p.y] == body)
      return false;
    else if (map[p.x][p.y] == food) {
      node begin = new node(p.x, p.y, 0), end1 = new node(tail.x + 1, tail.y, 0),
           end4 = new node(tail.x - 1, tail.y, 0), end2 = new node(tail.x, tail.y + 1, 0),
           end3 = new node(tail.x, tail.y - 1, 0);
      Queue<node> que = new LinkedList<node>();
      que.offer(begin);
      map[begin.x][begin.y] = '#';
      int [][]move_d = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
      while (!que.isEmpty()) {
        node temp = que.peek(), cur;
        que.poll();
        if (temp.equals(end1) || temp.equals(end2) || temp.equals(end3) || temp.equals(end4)) {
          resume();
          return true;
        }
        for (int i = 0; i < 4; i++) {
          cur = new node(temp.x + move_d[i][0], temp.y + move_d[i][1], temp.step + 1);
          if (cur.x > 0 && cur.x < N && cur.y > 0 && cur.y < M &&
              map[cur.x][cur.y] == Empty) {
            que.offer(cur);
            map[cur.x][cur.y] = '#';
          }
        }
      }
      resume();
      return false;
    } else {
      node begin = new node(p.x, p.y, 0), end = new node(tail.x, tail.y, 0);
      map[tail.x][tail.y] = Empty;
      Queue<node> que = new LinkedList<node>();
      que.offer(begin);
      map[begin.x][begin.y] = '#';
      int [][]move_d = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
      while (!que.isEmpty()) {
        node temp = que.peek(), cur;
        que.poll();
        if (temp.equals(end)) {
          resume();
          return true;
        }
        for (int i = 0; i < 4; i++) {
          cur = new node(temp.x + move_d[i][0], temp.y + move_d[i][1], temp.step + 1);
          if (cur.x > 0 && cur.x < N && cur.y > 0 && cur.y < M &&
              (map[cur.x][cur.y] == Empty || map[cur.x][cur.y] == food)) {
            que.offer(cur);
            map[cur.x][cur.y] = '#';
          }
        }
      }
      resume();
      return false;
    }
  }

  public static void goto_0_0() { System.out.print("\033[0;0H"); }

  public static void clear() {
    System.out.print("\33[?25l");
    System.out.print("\033[2J");
  }

  public static char[][] map;

  public static int food_x, food_y;

  public static int score;

  public static point tail;
  public static point snake;

  public static direction dir;

  public static int N = 20;
  public static int M = 65;
  public static char food = '$';
  public static char head = 'O';
  public static char body = 'X';
  public static char Wall = '*';
  public static char Empty = ' ';
}