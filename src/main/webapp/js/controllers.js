'use strict';

/* Controllers */

accumuloApp.controller('MonitorCtrl', [ '$scope', '$state',
		function($scope, $state) {

			// nothing yet

		} ]);

accumuloApp.controller('ProxyCtrl', [ '$scope', '$state',
		function($scope, $state) {
			// TODO load this list from the REST interface.
			// create an angular service for getting the proxy list.
			$scope.proxies = [ {
				name : 'HDFS',
				path : '#/proxy/hdfs'
			}, {
				name : 'Map/Reduce',
				path : '#/proxy/mapreduce'
			}, {
				name : 'Accumulo',
				path : '#/proxy/accumulo'
			} ];

			
		} ]);
