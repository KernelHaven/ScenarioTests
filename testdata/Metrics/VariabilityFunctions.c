// Expected Metric Values as follows:
//                          LoF        PLOF     ND_MAX  ND_AVG

void funcEmpty() {          //                  0       0
    ;                       // 0        0
}

void funcDecl() {           //                  1       1
#ifdef A
    int declaration;        // 1        1
#endif
}

void funcHalfVar() {        //                  1       2/1
#ifdef A
    int declaration;        // 1        1
#endif
    ;                       // 1        0.5
}

void funcVarNesting() {     //                  2       2/1
#ifdef A
#ifdef B
    int declaration;        // 1        1
#endif                      // 1        1
#endif
    ;                       // 1        0.5
}