#include "snake.h"
int main() {
	initial();
    clear();
	while (1) {
        setoutputposition();
		find_dir();
		if (move())
			break;
		printMap();
        usleep(7000);
	}
    printf("\033[?25h");
}