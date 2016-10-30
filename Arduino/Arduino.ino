/*
 * 29.10.2016
 * Jonas Schubert
 * GuepardoApps
 *
 * v0.0.1.161029
 *
 * Arduino file for home automation
 *
 * receiving commands via 433MHz (to send below data)
 * send data via 433MHz (temperature, humidity, movements, battery)
 *
 * measuring temperature and warn by critical values
 * measuring humidity and warn by critical values
 *
 * perhaps possibility to warn for critical values visually (enabling LEDs)
 *
 * detecting movements and inform about possible intruder
 *
 * measuring input battery and warn by low battery
 */

#include <RCSwitch.h>

int receiverPin = 0;
/*
 * Receiver
 *
 * VCC  to Arduino 5V
 * GND  to Arduino GND
 * ATAD to Arduino D2 for Interrupt
 * on interrupt 0 => that is pin D2
 */

int transmitterPin = 10;
/*
 * Transmitter
 *
 * VCC  to Arduino 5V
 * GND  to Arduino GND
 * ATAD to Arduino D4
 * using Pin D10
 */

RCSwitch lucaSwitch = RCSwitch();

void setup() {
	Serial.begin(9600);

	lucaSwitch.enableReceive(receiverPin);
	lucaSwitch.enableTransmit(transmitterPin);
}

void loop() {
	if (lucaSwitch.available()) {
		int value = lucaSwitch.getReceivedValue();
		if (value == 0) {
			Serial.print("Unknown encoding");
		} else {
			handleReceivedData();
		}
		lucaSwitch.resetAvailable();
	}
}

void handleReceivedData() {
	Serial.print("Received ");
	Serial.print(lucaSwitch.getReceivedValue());
	Serial.print(" / ");
	Serial.print(lucaSwitch.getReceivedBitlength());
	Serial.print("bit ");
	Serial.print("Protocol: ");
	Serial.println(lucaSwitch.getReceivedProtocol());
}

void handleSendData() {
	lucaSwitch.send("101000001000101010001");
}
