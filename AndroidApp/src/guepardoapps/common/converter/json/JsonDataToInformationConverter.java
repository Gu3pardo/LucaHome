package guepardoapps.common.converter.json;

import guepardoapps.common.classes.Logger;
import guepardoapps.common.Tools;
import guepardoapps.common.classes.Information;

public final class JsonDataToInformationConverter {

	private static String TAG = "JsonDataToInformationConverter";

	private static Logger _logger;

	public static Information Get(String[] restStringArray) {
		_logger = new Logger(TAG);
		for (String entry : restStringArray) {
			_logger.Debug(entry);
		}

		if (Tools.StringsAreEqual(restStringArray)) {
			return ParseStringToInformation(restStringArray[0]);
		} else {
			String usedEntry = Tools.SelectString(restStringArray, "{information:");
			return ParseStringToInformation(usedEntry);
		}

	}

	private static Information ParseStringToInformation(String restString) {
		if (Tools.GetStringCount(restString, "{information:") == 1) {
			if (restString.contains("{information:")) {
				restString = restString.replace("{information:", "").replace("};};", "");
				_logger.Debug("new restString: " + restString);

				String[] data = restString.split("\\};");
				if (data.length == 8) {
					if (data[0].contains("{Author:") && data[1].contains("{Company:") && data[2].contains("{Contact:")
							&& data[3].contains("{Build Date:") && data[4].contains("{Server Version:")
							&& data[5].contains("{Website Version:") && data[6].contains("{Temperature Log Version:")
							&& data[7].contains("{Android App Version:")) {

						String Author = data[0].replace("{Author:", "").replace("};", "");
						String Company = data[1].replace("{Company:", "").replace("};", "");
						String Contact = data[2].replace("{Contact:", "").replace("};", "");
						String Build_Date = data[3].replace("{Build Date:", "").replace("};", "");
						String Server_Version = data[4].replace("{Server Version:", "").replace("};", "");
						String Website_Version = data[5].replace("{Website Version:", "").replace("};", "");
						String Temperature_Log_Version = data[6].replace("{Temperature Log Version:", "").replace("};",
								"");
						String Android_App_Version = data[7].replace("{Android App Version:", "").replace("};", "");

						Information newValue = new Information(Author, Company, Contact, Build_Date, Server_Version,
								Website_Version, Temperature_Log_Version, Android_App_Version);
						_logger.Debug("newInformation: " + newValue.toString());

						return newValue;
					}
				}
			}
		}

		_logger.Error(restString + " has an error!");
		return null;
	}
}