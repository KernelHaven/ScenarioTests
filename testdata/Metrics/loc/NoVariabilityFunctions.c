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