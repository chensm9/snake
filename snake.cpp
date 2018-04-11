#include "snake.h"
void initial() {
	for (int i = 0; i < N; i++)
		for (int j = 0; j < M; j++)
			if (i == 0 || j == 0 || i == N-1 || j == M-1)
				map[i][j] = Wall;
			else
				map[i][j] = Empty;

	snake->next = tail;
	tail->front = snake;
	map[1][1] = body;
	map[1][2] = head;
	creatFood();
	dir = right;
	score = 0;
}

void resume() {
	for (int i = 0; i < N; i++)
		for (int j = 0; j < M; j++)
			if (map[i][j] == '#')
				map[i][j] = Empty;
	map[food_x][food_y] = food;
	map[tail->x][tail->y] = body;
}

void printMap() {
	cout << "score: " << score << endl;
	map[tail->x][tail->y] = Empty;
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < M; j++)
				cout << map[i][j];
		cout << endl;
	}
	map[tail->x][tail->y] = body;
}

void creatFood() {
	score++;
	srand((unsigned)time(NULL));
	while (map[food_x][food_y] != Empty) {
		food_x = rand() % (N-1) + 1;
		food_y = rand() % (M-1) + 1;
	}
	map[food_x][food_y] = food;
}

bool move() {
	point *q = new point(snake->x, snake->y, NULL, snake);
	map[tail->x][tail->y] = Empty;

	if (dir == right) q->y++;
	else if (dir == left) q->y--;
	else if (dir == up) q->x--;
	else if (dir == down) q->x++;

	if (map[q->x][q->y] == body || map[q->x][q->y] == Wall) {
		return true;
	}

	if (q->x == food_x && q->y == food_y) {
		map[q->x][q->y] = head;
		map[snake->x][snake->y] = body;
		snake->front = q;
		snake = q;
		map[tail->x][tail->y] = body;
		creatFood();
		return false;
	}
	else {
		map[q->x][q->y] = head;
		map[snake->x][snake->y] = body;
		snake->front = q;
		snake = q;
		point *p = tail;
		tail = tail->front;
		tail->next = NULL;
		delete p;
		return false;
	}
}

void find_dir() {
	if (dir == left || dir == right) {
		direction d[3] = { dir, up, down };
		int a[3] = { find_food(dir), find_food(up), find_food(down) };
		for (int i = 0; i < 2; i++)
			for (int j = i + 1; j < 3; j++)
				if (a[i] > a[j]) {
					swap(a[i], a[j]);
					swap(d[i], d[j]);
				}
		for (int i = 0; i < 3; i++)
			if (find_tail(d[i])) {
				dir = d[i];
				return;
			}
	}
	else {
		direction d[3] = { dir, left, right };
		int a[3] = { find_food(dir), find_food(left), find_food(right) };

		for (int i = 0; i < 2; i++) {
			for (int j = i + 1; j < 3; j++)
				if (a[i] > a[j]) {
					swap(a[i], a[j]);
					swap(d[i], d[j]);
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

point Move(direction d) {
	point q(snake->x, snake->y, NULL, NULL);
	if (d == right) q.y++;
	else if (d == left) q.y--;
	else if (d == up) q.x--;
	else if (d == down) q.x++;
	return q;
}

bool find_tail(direction d) {
	point p = Move(d);
	if (p.x == tail->x&&p.y == tail->y)
		return true;
	else if (map[p.x][p.y] == Wall || map[p.x][p.y] == body)
		return false;
	else if (map[p.x][p.y] == food) {
		node begin = { p.x, p.y, 0 }, end1 = { tail->x+1, tail->y, 0 }, end4 = { tail->x-1, tail->y, 0 };
		node end2 = { tail->x, tail->y + 1, 0 }, end3 = { tail->x, tail->y - 1, 0 };
		queue<node>que;
		que.push(begin);
		map[begin.x][begin.y] = '#';
		int move_d[4][2] = { { 1, 0 },{ -1, 0 },{ 0, 1 },{ 0, -1 } };
		while (!que.empty()) {
			node temp = que.front(), cur;
			que.pop();
			if (temp == end1||temp == end2 || temp == end3 || temp == end4) {
				resume();
				return true;
			}
			for (int i = 0; i < 4; i++) {
				cur = { temp.x + move_d[i][0] , temp.y + move_d[i][1], temp.step + 1 };
				if (cur.x > 0 && cur.x < N && cur.y > 0 && cur.y < M &&map[cur.x][cur.y] == Empty ) {
					que.push(cur);
					map[cur.x][cur.y] = '#';
				}
			}
		}
		resume();
		return false;
	}
	else {
		node begin = { p.x, p.y, 0 }, end = { tail->x, tail->y, 0 };
		map[tail->x][tail->y] = Empty;
		queue<node>que;
		que.push(begin);
		map[begin.x][begin.y] = '#';
		int move_d[4][2] = { {1, 0}, {-1, 0}, {0, 1}, {0, -1} };
		while (!que.empty()) {
			node temp = que.front(), cur;
			que.pop();
			if (temp == end) {
				resume();
				return true;
			}
			for (int i = 0; i < 4; i++) {
				cur = { temp.x + move_d[i][0] , temp.y + move_d[i][1], temp.step + 1 };
				if (cur.x > 0 && cur.x < N && cur.y > 0 && cur.y < M && 
					(map[cur.x][cur.y] == Empty || map[cur.x][cur.y] == food)) {
					que.push(cur);
					map[cur.x][cur.y] = '#';
				}
			}
		}
		resume();
		return false;
	}
}

int find_food(direction d) {
	point p = Move(d);
	if (map[p.x][p.y] == Wall || map[p.x][p.y] == body)
		return 10000;
	else {
		node begin = { p.x, p.y, 0 }, end = { food_x, food_y, 0 };
		queue<node>que;
		que.push(begin);
		map[begin.x][begin.y] = '#';
		int move_d[4][2] = { { 1, 0 },{ -1, 0 },{ 0, 1 },{ 0, -1 } };
		while (!que.empty()) {
			node temp = que.front(), cur;
			que.pop();
			if (temp == end) {
				resume();
				return temp.step;
			}
			for (int i = 0; i < 4; i++) {
				cur = { temp.x + move_d[i][0] , temp.y + move_d[i][1], temp.step + 1 };
				if (cur.x > 0 && cur.x < N && cur.y > 0 && cur.y < M &&
					(map[cur.x][cur.y] == Empty || map[cur.x][cur.y] == food)) {
					que.push(cur);
					map[cur.x][cur.y] = '#';
				}
			}
		}
		resume();
		return 10000;
	}
}

void setoutputposition() {
    printf("\033[%d;%dH", 0, 0);
}

void clear(){
	printf("\33[?25l");
	printf("\033[2J");
}
