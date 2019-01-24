<div align="center">
	<img width="360" src="logo.png" alt="LucaHome">
</div>

# LucaHome

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
<a target="_blank" href="https://www.paypal.me/GuepardoApps" title="Donate using PayPal"><img src="https://img.shields.io/badge/paypal-donate-blue.svg" /></a>

Project for home automation and other stuff

# Todo

- [ ] [Raspberry Pi Server](https://github.com/LucaHome/LucaHome-RaspberryServer)
    - [ ] 433 MHz Socket & Switch Control
		- [ ] requires .db for storing codes
		- [ ] library to send commands 433MHz
		- [ ] Api
		- will be C++/C#
	- [ ] Temperature Logging
		- [ ] requires .db for storing data
		- [ ] send warnings
		- [ ] Api
		- will be [PYTHON](https://github.com/LucaHome/LucaHome-RaspberryTemperatureLogger) script & C++/C#
	- [ ] PuckJs Beacons Logging
		- [ ] requires .db for storing codes and data
		- [ ] library to connect via Bluetooth
		- [ ] send warnings
		- [ ] Api
		- will be C++/C#
	- [ ] Network Control
		- [ ] library to monitor network
		- [ ] send warnings
		- [ ] Api
		- will be C++/C#
	- [ ] Camera Monitoring with Motion Detection
		- [ ] library for camera & motion detection
		- [ ] send warnings
		- [ ] Api
		- will be C++/C#
	- [ ] SmartPhone Navigation
		- [ ] requires PuckJs Beacon connection
		- [ ] Api
		- will be C++/C#
	- Further
		- Meal, ShoppingList will be in new own project for NextCloud (available in older v5.x)
		- BirthdayReminder  will be in NextCloud using calendar app (available in older v5.x)
	
- [ ] [Raspberry Pi Website](https://github.com/LucaHome/LucaHome-Website)
	- [ ] Display Temperature Data, PuckJs Data, Network Data, Camera Data, SmartPhone Data, About Data
	- will be TypeScript with Angular/React & Material

- [ ] [Android Application](https://github.com/LucaHome/LucaHome-AndroidApplication)
	- [ ] Display Temperature Data, PuckJs Data, Network Data, Camera Data, SmartPhone Data, About Data
	- [ ] Control sockets & switches
	- [ ] Manage Network Access
	- will be Kotlin

- [ ] [Windows Application](https://github.com/LucaHome/LucaHome-WPFApplication)
	- [ ] Display Temperature Data, PuckJs Data, Network Data, Camera Data, SmartPhone Data, About Data
	- [ ] Control sockets & switches
	- [ ] Manage Network Access
	- will be C#/JavaScript&Html

- [ ] [PuckJs Beacons](https://github.com/LucaHome/LucaHome-PuckJS)
	- [ ] Provide Temperature, Humidity & Light Data
	- [ ] Send Beacon for position calculation
	- will be JavaScript

- Other ideas
	- WakeUp with alarm (music playing) and light increasing ([old project](https://github.com/LucaHome/LucaHome-MediaServer))
	- control NextCloud
	- control Kodi
	- ...

# License

LucaHome is distributed under the MIT license. [See LICENSE](LICENSE.md) for details.

```
MIT License

Copyright (c) 2016-2019 GuepardoApps (Jonas Schubert)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

```