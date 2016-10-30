#include "receiverservice.h"

/*===============PUBLIC==============*/

ReceiverService::ReceiverService() {
}

ReceiverService::~ReceiverService() {
}

std::string ReceiverService::performAction(int receivedValue) {
	//TODO add prober handling!

	std::stringstream out;
	out << "{ReceivedValue:" << Tools::convertIntToStr(receivedValue) << "};"
			<< "\x00" << std::endl;
	return out.str();
}
