//														positive only		negative only		positive + negative
void funcNoVariability() {							
    ;
    ;
}													//	0					0					0

void aPositiveVariable() {
    #if defined(FIRST_VAR)
    ;												//	1					0					1
    #endif
}													//	1					0					1

void positiveAndNegativeVariable() {
    #if defined(SECOND_VAR)
    ;												//	1					0					1
    #else
    ;												//	1					1					2
    ;												//	1					2					3
    #endif
}													//	1					2					3

void twoVariables() {
    #if defined(THIRD_VAR) && defined(FOURTH_VAR)
    ;												//	1					0					1
    ;												//	2*2					0					4
    #endif
}													//	2*2					0					4

void complex() {
    #if defined(FITH_VAR)
    ;												//	1					0					1
    ;												//	2					0					2
    #elif defined(SIXTH_VAR)
    ;												//	2+1					1					4
    #endif
}													//	3					1					4

void variableInTwoFunctions1() {
	#if defined(FOO)
	;												//	1					0					1
	#elif defined(BAR)
	;												//	1+1					1					3
	;												//	1+2					2					5
	;												//	1+3					3					7
	#endif
}													//	4					3					7

void variableInTwoFunctions2() {
	#if defined(BAR)
	;												//	1					0					1
	;												//	2					0					2
	#elif defined(FOO)
	;												//	1+2					1					4
	;												//	2+2					2					6
	;												//	3+2					3					8
	;												//	4+2					4					10
	#endif
}													//	6					4					10
