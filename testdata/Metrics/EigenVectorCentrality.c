//Example with v1 - v5 based on http://djjr-courses.wikidot.com/soc180:eigenvector-centrality

void v1() {
    v2();
    v3();
    v4();
}

void v2() {
    v1();
    v3();
}

void v3() {
    v1();
    v2();
    v4();
}

void v4() {
    v1();
    v3();
    v5();
}

void v5() {
    v4();
}

void v6() {
    v6();
}

void v7() {
    v7();
}

void v8() {
    v7();
    v8();
}

void v9() {
    v7();
}

void unconnectedFunction() {}