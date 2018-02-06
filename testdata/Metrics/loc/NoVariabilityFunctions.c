// Expected Metric Values as follows:
//                          dLoC

void funcDecl() {
    int declaration;        // 1
}

void funcIfElse() {
    if(true) {              // 1
        int declaration;    // 2
    } else {                // 3
        int declaration;    // 4
    }
}

void funcGoto(int a) {

	char c = 'a';           // 1
	int i = 0;              // 2
loop:

	c++;                    // 3
	i++;                    // 4

	if (i < a) {            // 5
		goto loop;          // 6
	}

}

void functDoWhile(int a) {

	char c = 'a';           // 1
	int i = 0;              // 2
	do {                    // 3
		c++;                // 4

		if (c == 'c') {     // 5
			continue;       // 6
		}

		if (c == 'z') {     // 7
			break;          // 8
		}

		i++;                // 9
	} while (i < a);//Already counted

}

void funcFor(int a) {
	char c = 'a';           // 1
	int i;                  // 2
	for (i = 0; i < a; i++) {//3
		c++;                // 4

		if (c == 'c') {     // 5
			continue;       // 6
		}

		if (c == 'z') {     // 7
			break;          // 8
		}

	}
}

void funcWhile(int a) {
	char c = 'a';           // 1
	int i = 0;              // 2
	while (!(i < a)) {      // 3
		++c;                // 4

		if (c == 'c') {     // 5
			continue;       // 6
		}

		if (c == 'z') {     // 7
			break;          // 8
		}

		i++;                // 9
	}
}

char funcSwitch(int a) {
	char result = '\0';     // 1

	switch (a) {            // 2
	case 0:                 // 3
		result = 'a';       // 4
		break;              // 5

	case 1:                 // 6
	case 2:                 // 7
		result = 'b';       // 8
		break;              // 9

	default:                //10
		result = 'c';       //11
	}

	return result;          //12
}