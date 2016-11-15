SOURCE = ./src/lucahome.cpp ./src/xml/xmlparser.cpp ./src/xml/xmlservice.cpp ./src/xml/xmlwriter.cpp ./src/temperature/temperatureservice.cpp ./src/remote/gpio.cpp ./src/remote/schedule.cpp ./src/remote/socket.cpp ./src/remote/remoteservice.cpp ./src/receiver/receiverservice.cpp ./src/movies/movie.cpp ./src/movies/movieservice.cpp ./src/mail/mailservice.cpp ./src/logger/logger.cpp ./src/logger/log.cpp ./src/informations/information.cpp ./src/informations/informationservice.cpp ./src/flatmap/point.cpp ./src/flatmap/mapcontentservice.cpp ./src/flatmap/mapcontent.cpp ./src/controller/filecontroller.cpp ./src/common/picontrol.cpp ./src/common/tools.cpp ./src/changes/change.cpp ./src/changes/changeservice.cpp ./src/birthdays/birthday.cpp ./src/birthdays/birthdayservice.cpp ./src/authentification/authentificationservice.cpp ./src/authentification/user.cpp ./src/audio/audioservice.cpp ./src/accesscontrol/accesscontrolservice.cpp -lwiringPi -lpthread
APP = ./bin/lucahome
CC = g++

all: $(APP)

$(APP): $(SOURCE)
	chmod -R 755 *
	$(CC) $(SOURCE) -o $(APP)

clean:
	rm -fv ./bin/*
	update-rc.d -f lucahome remove
	rm -fv /etc/init.d/lucahome/
	rm -rf /etc/default/lucahome/
	rm -fv /bin/lucahome

install:
	mkdir /etc/default/lucahome/
	mkdir /etc/default/lucahome/log/
	cp ./assets/birthdays /etc/default/lucahome/birthdays
	cp ./assets/changes /etc/default/lucahome/changes
	cp ./assets/infos /etc/default/lucahome/infos
	cp ./assets/mapcontent /etc/default/lucahome/mapcontent
	cp ./assets/movies /etc/default/lucahome/movies
	cp ./assets/settings /etc/default/lucahome/settings
	cp ./assets/users /etc/default/lucahome/users
	cp ./src/mail/python/mail.py /etc/default/lucahome/mail.py
	cp ./bin/lucahome /bin/lucahome
	chmod 777 /etc/default/lucahome/birthdays
	chmod 777 /etc/default/lucahome/changes
	chmod 777 /etc/default/lucahome/infos
	chmod 777 /etc/default/lucahome/mapcontent
	chmod 777 /etc/default/lucahome/movies
	chmod 777 /etc/default/lucahome/settings
	chmod 777 /etc/default/lucahome/users
	chmod 777 /etc/default/lucahome/mail.py
	cp ./init/lucahome /etc/init.d
	update-rc.d lucahome defaults
