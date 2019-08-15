void funcStub() {}

void funcEmpty() {
    ; 
}

void funcOneIfDef() {
    #ifdef A
    int declaration;
    #endif
}

void funcNested1() {
    #ifdef A
    ;
    #ifdef B
    ;
    #endif
    #endif
}

void funcNested2() {
    #ifdef A
    ;
        #ifdef B
        ;
        #endif
    ;
    #else
    ;
    #endif
}

void funcNested3() {
    #ifdef A
        ;
        #ifdef B
            ;
        #else
            ;
        #endif
    #endif
}

void funcNested4() {
    #ifdef A
        ;
        #ifdef B
            ;
        #else
            ;
        #endif
    #else
        ;
    #endif
}

void funcElif() {
    #ifdef A
        ;
    #elif defined(B)
        ;
    #else
        ;
    #endif
}

void funcElif2() {
    #ifdef A
        ;
    #elif defined(B)
        ;
    #elif defined(C)
        ;
    #else
        ;
    #endif
}

void funcElifNested1() {
    #ifdef A
        ;
        #if defined(B)
            ;
        #elif defined(C)
            ;
        #endif
        ;
    #endif
}

void funcElifNested2() {
    #ifdef A
        ;
        #if defined(B)
            ;
        #elif defined(C)
            ;
        #endif
        ;
    #elif defined(D)
        ;
    #else
        ;
    #endif
}