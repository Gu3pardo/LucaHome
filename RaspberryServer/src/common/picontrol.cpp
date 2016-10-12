#include "picontrol.h"

void PiControl::printCode(int code[]) {
	printf("Sending ");
	for (int i = 0; i < 16; i++) {
		printf("%d ", code[i]);
	}
	printf("via Ether\n");
}

bool PiControl::sendEther(int gpio, int code[]) {
	if (wiringPiSetupGpio() == -1) {
		return false;
	}
	pinMode(gpio, OUTPUT);
	int x = 0;
	for (int r = 0; r < REPEAT; r++) {
		for (int c = 0; c < 16; c++) {
			x = 128;
			for (int i = 1; i < 9; i++) {
				if ((code[c] & x) > 0) {
					digitalWrite(gpio, HIGH);
				} else {
					digitalWrite(gpio, LOW);
				}
				usleep(PLENGTH);
				x = x >> 1;
			}
		}
	}
	return true;
}

bool PiControl::send433Mhz(int gpio, std::string codeStr, int active) {
	// Validate code
	if (codeStr.length() != 6) {
		printf("The code must be 6 characters long");
		return false;
	}
	for (int i = 0; i < 5; i++) {
		if (codeStr[i] != '1' && codeStr[i] != '0') {
			printf("The code must be in this format: 10101A\n");
			return false;
		}
	}
	if (!(codeStr[5] >= (int) 'A' && codeStr[5] <= (int) 'E')) {
		printf("The code must be in this format: 10101B\n");
		return false;
	}
	int code[16] = { 142, 142, 142, 142, 142, 142, 142, 142, 142, 142, 142, 142,
			128, 0, 0, 0 };
	// Parse device-code
	for (int i = 0; i < 5; i++) {
		if (codeStr[i] == '1') {
			code[i] = WTRUE;
		} else {
			code[i] = WFALSE;
		}
	}
	// Parse device-id (A - E)
	int id = pow(2, (int) codeStr[5] - 65);
	// Set device-id
	int x = 1;
	for (int i = 1; i < 6; i++) {
		if ((id & x) > 0) {
			code[4 + i] = WTRUE;
		} else {
			code[4 + i] = WFALSE;
		}
		x = x << 1;
	}
	// Set Status
	if (active == 1) {
		code[10] = WTRUE;
		code[11] = WFALSE;
	} else {
		code[10] = WFALSE;
		code[11] = WTRUE;
	}

	printCode(code);

	return sendEther(gpio, code);
}

bool PiControl::writeGpio(int gpio, int status) {
	if (wiringPiSetupGpio() == -1) {
		return false;
	}
	pinMode(gpio, OUTPUT);
	if (status == 1) {
		digitalWrite(gpio, HIGH);
	} else {
		digitalWrite(gpio, LOW);
	}
	return true;
}
