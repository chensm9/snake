#ifndef SNAKE_H
#define SNAKE_H

#include<iostream>
#include<time.h>
#include<queue>
#include<cstdlib>
#include<algorithm>
#include<cstdio>
#include<unistd.h>
using std::swap;
using std::cout;
using std::endl;
using std::queue;

#define N  20
#define M  65
#define food '$'
#define head 'O'
#define body 'X'
#define Wall '*'
#define Empty ' '

struct point {
	int x;
	int y;
	point *next;
	point *front; 
	point(int a, int b, point *f, point *n): x(a), y(b),front(f), next(n){}
};


struct node {
	int x;
	int y;
	int step; 
	node(int _x = 0, int _y = 0, int _step = 0) {
		this->x = _x;
		this->y = _y;
		this->step = _step;
	}
	bool operator==(node &other) {
		if (x == other.x && y == other.y)
			return true;
		return false;
	}
};

static char map[N][M] = {};   

static int food_x, food_y;  

static int score; 

static point *tail = new point(1, 1, NULL, NULL); 
static point *snake = new point(1, 2, NULL, NULL); 

enum direction{ up, down, right, left };   

static direction dir;  

void initial();

void resume();

void printMap();

void creatFood();

bool move();

point move(direction);

void find_dir();

int find_food(direction);

bool find_tail(direction);

void setoutputposition();

void clear();

#endif
