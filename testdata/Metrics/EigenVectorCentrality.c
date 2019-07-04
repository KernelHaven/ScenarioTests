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