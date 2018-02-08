// Expected Metric Values as follows:
//                          dLoC        McCabe

void funcEmpty() {          //          1
    ;                       // 1
}

void funcDecl() {           //          1
    int declaration;        // 1
}

void funcIfElse() {         //          1
    if(true) {              // 1        2
        int declaration;    // 2
    } else {                // 3
        int declaration;    // 4
    }
}

void funcGoto(int a) {      //          1

	char c = 'a';           // 1
	int i = 0;              // 2
loop:

	c++;                    // 3
	i++;                    // 4

	if (i < a) {            // 5        2
		goto loop;          // 6
	}

}

void functDoWhile(int a) {  //          1

	char c = 'a';           // 1
	int i = 0;              // 2
	do {                    // 3        2
		c++;                // 4

		if (c == 'c') {     // 5        3
			continue;       // 6
		}

		if (c == 'z') {     // 7        4
			break;          // 8
		}

		i++;                // 9
	} while (i < a);        //Already counted

}

void funcFor(int a) {       //          1
	char c = 'a';           // 1
	int i;                  // 2
	for (i = 0; i < a; i++) {//3        2
		c++;                // 4

		if (c == 'c') {     // 5        3
			continue;       // 6
		}

		if (c == 'z') {     // 7        4
			break;          // 8
		}

	}
}

void funcWhile(int a) {     //          1
	char c = 'a';           // 1
	int i = 0;              // 2
	while (!(i < a)) {      // 3        2
		++c;                // 4

		if (c == 'c') {     // 5        3
			continue;       // 6
		}

		if (c == 'z') {     // 7        4
			break;          // 8
		}

		i++;                // 9
	}
}

char funcSwitch(int a) {    //          1
	char result = '\0';     // 1

	switch (a) {            // 2
	case 0:                 // 3        2
		result = 'a';       // 4
		break;              // 5

	case 1:                 // 6        3
	case 2:                 // 7        4
		result = 'b';       // 8
		break;              // 9

	default:                //10
		result = 'c';       //11
	}

	return result;          //12
}