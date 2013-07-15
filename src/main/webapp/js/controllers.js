'use strict';

/* Controllers */

accumuloApp.controller('MonitorCtrl', [ '$scope', '$state',
		function($scope, $state) {

			// nothing yet

		} ]);

accumuloApp.controller('ProxyCtrl', [ '$scope', 'proxies',
		function($scope, proxies) {
			$scope.proxyList = proxies;
		} ]);

accumuloApp.controller('ProxyStateCtrl', [
		'$scope',
		'$state',
		'$http',
		'proxies',
		function($scope, $state, $http, proxies) {

			$scope.proxies = proxies;

			console.log('Performing GET: ' + $state.current.data.url);

			$http.get($state.current.data.url).success(
					function(data, status, headers, config) {
						// this callback will be called asynchronously
						// when the response is available
						console.log('GET was successful!');
						$scope.site = processHtmlPayload(data);
					}).error(function(data, status, headers, config) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
				console.log('GET error ' + status);
				$scope.site = data;
			});

		} ]);


function processHtmlPayload(data){
	var doc = x2js.parseXmlString(data);
	var bodyElement = doc.getElementsByTagName('body');
	console.log('found this: ' + bodyElement[0].textContent);
	return bodyElement[0].childNodes;
}

