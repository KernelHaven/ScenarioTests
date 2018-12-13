// Expected Metric Values as follows:
//                          dLoC        McCabe  ND_MAX  ND_AVG

void funcEmpty() {          //          1       1       1
    ;                       // 1
}

void funcDecl() {           //          1       1       1
    int declaration;        // 1
}

void funcIfElse() {         //          1       1       2
    if(true) {              // 1        2
        int declaration;    // 2                2
    } else {                // 3
        int declaration;    // 4                2
    }
}

void funcGoto(int a) {      //          1       1       6/5

	char c = 'a';           // 1
	int i = 0;              // 2
loop:

	c++;                    // 3
	i++;                    // 4

	if (i < a) {            // 5        2
		goto loop;          // 6                2
	}

}

void functDoWhile(int a) {  //          1       1       12/6

	char c = 'a';           // 1
	int i = 0;              // 2
	do {                    // 3        2
		c++;                // 4                2

		if (c == 'c') {     // 5        3
			continue;       // 6                3
		}

		if (c == 'z') {     // 7        4
			break;          // 8                3
		}

		i++;                // 9
	} while (i < a);        //Already counted

}

void funcFor(int a) {       //          1       1       10/5
	char c = 'a';           // 1
	int i;                  // 2
	for (i = 0; i < a; i++) {//3        2
		c++;                // 4                2

		if (c == 'c') {     // 5        3
			continue;       // 6                3
		}

		if (c == 'z') {     // 7        4
			break;          // 8                3
		}

	}
}

void funcWhile(int a) {     //          1       1       12/6
	char c = 'a';           // 1
	int i = 0;              // 2
	while (!(i < a)) {      // 3        2
		++c;                // 4                2

		if (c == 'c') {     // 5        3
			continue;       // 6                3
		}

		if (c == 'z') {     // 7        4
			break;          // 8                3
		}

		i++;                // 9
	}
}

char funcSwitch(int a) {    //          1       1       12/7
	char result = '\0';     // 1

	switch (a) {            // 2
	case 0:                 // 3        2
		result = 'a';       // 4                2
		break;              // 5

	case 1:                 // 6
	case 2:                 // 7        3
		result = 'b';       // 8
		break;              // 9

	default:                //10
		result = 'c';       //11
	}

	return result;          //12
}
