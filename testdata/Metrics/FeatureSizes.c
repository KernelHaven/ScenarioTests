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
    ;												//	1+1					0					2
    ;												//	2+2					0					4
    #endif
}													//	4					0					4

void complex() {
    #if defined(FITH_VAR)
    ;												//	1+0					0+0					1
    ;												//	2+0					0+0					2
    #elif defined(SIXTH_VAR)
    ;												//	2+1					1+0					4
    #endif
}													//	3					1					4

void variableInTwoFunctions1() {
	#if defined(FOO)
	;												//	1+0					0+0					1
	#elif defined(BAR)
	;												//	1+1					1+0					3
	;												//	1+2					2+0					5
	;												//	1+3					3+0					7
	#endif
}													//	4					3					7
												// +	3					8					11
													//	7					11					18

void variableInTwoFunctions2() {
	#if defined(BAR)
	;												//	0+1+0				0+0+0				1
	#elif defined(FOO)
	;												//	1+1+0				0+1+0				3
	;												//	2+1+0				0+2+0				5
	#elif defined(ANOTHER_VAR)
	;												//	2+1+1				1+3+0				8
	;												//	2+1+2				2+4+0				11
	;												//	2+1+3				3+5+0				14
	#endif
}													//	6					8					14
												// +	4					3					7
													//	10					11					21

void nestedFunction() {								//	A		B		C		D
	;
	#if defined(VAR_A)
		;											//	1/1		0/0		0/0		0/0
		;											//	2/2		0/0		0/0		0/0
		#if defined(VAR_B)
			;										//	3/3		1/1		0/0		0/0
		#elif defined(VAR_C)
			;										//	4/4		1/2		1/1		0/0
			;										//	5/5		1/3		2/2		0/0
			;										//	6/6		1/4		3/3		0/0
		#endif
		;											//	7/7		1/4		3/3		0/0
	#elif defined(VAR_D)
		;											//	7/8		1/4		3/3		1/1
	#endif
}													//	7/8		1/4		3/3		1/1
													//	7+1+3+1=12		8+4+3+1=16
													