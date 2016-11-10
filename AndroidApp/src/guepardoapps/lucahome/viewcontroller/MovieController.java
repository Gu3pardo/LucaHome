package guepardoapps.lucahome.viewcontroller;

import android.content.Context;

import guepardoapps.lucahome.common.Constants;
import guepardoapps.lucahome.common.LucaHomeLogger;
import guepardoapps.lucahome.common.controller.ServiceController;
import guepardoapps.lucahome.common.enums.LucaObject;
import guepardoapps.lucahome.common.enums.MainServiceAction;
import guepardoapps.lucahome.common.enums.RaspberrySelection;
import guepardoapps.lucahome.dto.MovieDto;
import guepardoapps.lucahome.services.PackageService;

import guepardoapps.toolset.controller.BroadcastController;
import guepardoapps.toolset.controller.SharedPrefController;

public class MovieController {

	private static final String TAG = MovieController.class.getName();
	private LucaHomeLogger _logger;

	private Context _context;

	private BroadcastController _broadcastController;
	private ServiceController _serviceController;
	private SharedPrefController _sharedPrefController;

	private PackageService _packageService;

	public MovieController(Context context) {
		_logger = new LucaHomeLogger(TAG);

		_context = context;
		_broadcastController = new BroadcastController(_context);
		_serviceController = new ServiceController(_context);
		_sharedPrefController = new SharedPrefController(_context, Constants.SHARED_PREF_NAME);

		_packageService = new PackageService(_context);
	}

	public void StartMovie(MovieDto movie) {
		_logger.Debug("Trying to start movie: " + movie.GetTitle());

		String action = Constants.ACTION_START_MOVIE + movie.GetTitle();
		_serviceController.StartRestService(movie.GetTitle(), action, null, LucaObject.MOVIE, RaspberrySelection.BOTH);

		if (_sharedPrefController.LoadBooleanValueFromSharedPreferences(Constants.START_OSMC_APP)) {
			if (_packageService.IsPackageInstalled(Constants.PACKAGE_KORE)) {
				_packageService.StartApplication(Constants.PACKAGE_KORE);
			} else if (_packageService.IsPackageInstalled(Constants.PACKAGE_YATSE)) {
				_packageService.StartApplication(Constants.PACKAGE_YATSE);
			} else {
				_logger.Warn("User wanted to start an application, but nothing is installed!");
			}
		}

		_broadcastController.SendSerializableArrayBroadcast(Constants.BROADCAST_MAIN_SERVICE_COMMAND,
				new String[] { Constants.BUNDLE_MAIN_SERVICE_ACTION },
				new Object[] { MainServiceAction.DOWLOAD_SOCKETS });
	}
}
