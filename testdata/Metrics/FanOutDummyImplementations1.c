#ifdef A
void func1() {
	// real implementation
}
#else
// dummy implementation
void func1() {}
#endif


void func2() {
	func1();
}
