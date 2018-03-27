// Expected Metric Values as follows:
//                          LoF        PLOF     ND_MAX  ND_AVG  VP-ND_MAX  VP-ND_AVG    CC-VP

void funcEmpty() {          //                  1       1       0             0         1
    ;                       // 0        0
}

void funcDecl() {           //                  1       1       1             1         1
#ifdef A
    int declaration;        // 1        1                                               2
#endif
}

void funcHalfVar() {        //                  1       1       1           1/2         1
#ifdef A
    int declaration;        // 1        1                                               2
#endif
    ;                       // 1        0.5
}

void funcVarNesting() {     //                  1       1       2           2/2         1
#ifdef A
#ifdef B
    int declaration;        // 1        1                                               3
#endif
#endif
    ;                       // 1        0.5
}