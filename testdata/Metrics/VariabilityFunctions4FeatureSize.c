void funcEmpty() {
    ;
}

void funcA() {
#if defined(VAR_A) && defined(VAR_A)
    int declaration;
#endif
}

void funcBA() {
#ifdef VAR_B
    int declaration;
#endif
}

void funcHalfVar() {
#ifdef VAR_A
    int declaration;
#endif
    ;
}

void funcVarNesting() {
#ifdef VAR_A
#ifdef VAR_B
    int declaration;
#endif
#endif
    ;
}

#ifdef VAR_A
void conditionalFunction1() {
#ifdef VAR_B
    ;
#endif
}
#endif

#ifdef VAR_A
void conditionalFunction2() {
#ifdef VAR_A
#ifdef VAR_B
    ;
#endif
#endif
}
#endif

void funcCppElse() {
#ifdef VAR_A
    int declaration;
#else
    ;
    ;
#endif
    ;
}
