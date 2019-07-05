// Expected Metric Values as follows:
// Local Fan                FAN-IN  FAN-OUT     SUM

void funcNoCall() {         // 0        0       0
    ;
}


void funcCallsOne() {      // 0         1       1
    funcCalledByOne();
}

void funcCalledByOne() {  // 1          0       1
    ;
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