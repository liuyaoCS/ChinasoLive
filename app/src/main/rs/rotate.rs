#pragma version(1)
#pragma rs java_package_name(com.letv.com.recorder.utils.rs)
#pragma rs_fp_relaxed

rs_allocation gIn;
rs_allocation gOut;
int width;
int height;
int direction = 2; // 0 - flip horizontally, 1 - flip vertically

void __attribute__((kernel)) filter(const uchar4 in, uint32_t x, uint32_t y) {

	uchar4 element = {0, 0, 0, 0};
	switch(direction) {
		case 0: // flip horiz
		element = rsGetElementAt_uchar4(gIn, width-1 - x, y);
		break;
		case 1: // flip vert
		element = rsGetElementAt_uchar4(gIn, x, height-1 - y);
		break;
		case 2: // rotate left
		element = rsGetElementAt_uchar4(gIn, y, width-1 - x);
		break;
		case 3: // rotate right
		element = rsGetElementAt_uchar4(gIn, height-1 - y, width-1 - x);
		break;
	}
	rsSetElementAt_uchar4(gOut, element, x, y);

}