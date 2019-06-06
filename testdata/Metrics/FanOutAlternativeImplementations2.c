void func1A() {
	// implementation 1
}

void func1B() {
	// implementation 2
}


void func2() {
#ifdef A
	func1A();
#else
	func1B();
#endif
}
