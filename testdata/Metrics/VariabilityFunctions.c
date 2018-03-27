// Expected Metric Values as follows:
//                          LoF        PLOF 

void funcEmpty() {          //
    ;                       // 0        0
}

void funcDecl() {           //
#ifdef A
    int declaration;        // 1        1
#endif
}

void funcHalfVariability() {
#ifdef A
    int declaration;        // 1        1
#endif
;                           // 1        0.5
}