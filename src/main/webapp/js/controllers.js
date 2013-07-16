'use strict';

/* Controllers */

accumuloApp.controller('MonitorCtrl', [
		'$scope',
		'$state',
		'$http',
		function($scope, $state, $http) {

			$http.get('accumulo/Admin/InstanceOperations/instanceData')
					.success(function(data, status, headers, config) {
						$scope.instanceData = data;
					}).error(function(data, status, headers, config) {
						$scope.status = status;
					});

		} ]);

accumuloApp.controller('ProxyCtrl', [ '$scope', 'proxies',
		function($scope, proxies) {
			$scope.proxyList = proxies;
		} ]);

accumuloApp.controller('ProxyStateCtrl', [ '$scope', '$state', '$http',
		'proxies', function($scope, $state, $http, proxies) {

			$scope.proxies = proxies;

			console.log('Performing GET: ' + $state.current.data.url);

		} ]);

accumuloApp.controller('RESTCtrl', [ '$scope', '$state', '$http', '$templateCache',
		function($scope, $state, $http, $templateCache) {

			$scope.table = '!METADATA';
			$scope.auths = 'test';
			
			$scope.scan = function() {
				$scope.code = null;
				$scope.response = null;

				$http({
					method : 'GET',
					url : 'accumulo/Scanner.json?range=nothing&table='+escape($scope.table)+'&auths='+$scope.auths,
					cache : $templateCache
				}).success(function(data, status) {
					$scope.status = status;
					$scope.data = data;
				}).error(function(data, status) {
					$scope.data = data || "Request failed";
					$scope.status = status;
				});
			};

			$scope.list = function() {
				$scope.code = null;
				$scope.response = null;

				$http({
					method : 'GET',
					url : 'accumulo/Admin/TableOperations/list',
					cache : $templateCache
				}).success(function(data, status) {
					$scope.status = status;
					$scope.data = data;
				}).error(function(data, status) {
					$scope.data = data || "Request failed";
					$scope.status = status;
				});
			};
			
			$scope.updateModel = function(table) {
				$scope.table = table;
			};

		} ]);
