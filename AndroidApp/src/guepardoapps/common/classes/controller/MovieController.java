package guepardoapps.common.classes.controller;

import android.content.Context;

import guepardoapps.common.Constants;
import guepardoapps.common.classes.Logger;
import guepardoapps.common.classes.Movie;
import guepardoapps.common.controller.ServiceController;
import guepardoapps.common.enums.LucaObject;
import guepardoapps.common.service.PackageService;

import guepardoapps.toolset.controller.SharedPrefController;

public class MovieController {

	private static String TAG = "MovieController";

	private Logger _logger;
	private ServiceController _serviceController;
	private SharedPrefController _sharedPrefController;

	private Context _context;

	private PackageService _packageService;

	public MovieController(Context context) {
		_context = context;

		_logger = new Logger(TAG);
		_serviceController = new ServiceController(_context);
		_sharedPrefController = new SharedPrefController(_context, Constants.SHARED_PREF_NAME);

		_packageService = new PackageService(_context);
	}

	public void StartMovie(Movie movie) {
		_logger.Debug("Trying to start movie: " + movie.GetTitle());
		String action = Constants.ACTION_START_MOVIE + movie.GetTitle();
		_serviceController.StartRestService(movie.GetTitle(), action, null, LucaObject.MOVIE);
		if (_sharedPrefController.LoadBooleanValueFromSharedPreferences(Constants.START_OSMC_APP)) {
			if (_packageService.IsPackageInstalled(Constants.PACKAGE_KORE)) {
				_packageService.StartApplication(Constants.PACKAGE_KORE);
			} else if (_packageService.IsPackageInstalled(Constants.PACKAGE_YATSE)) {
				_packageService.StartApplication(Constants.PACKAGE_YATSE);
			} else {
				_logger.Warn("User wanted to start an application, but nothing is installed!");
			}
		}
	}
}
