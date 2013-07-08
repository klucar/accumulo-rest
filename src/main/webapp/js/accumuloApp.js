'use strict';

// Declare app level module which depends on filters, and services
var accumuloApp = angular
		.module('accumuloApp', [ 'ui.compat', 'ui.bootstrap' ]);

accumuloApp.config([ '$stateProvider', '$routeProvider',
		function($stateProvider, $routeProvider) {
			$routeProvider.when('', {
				redirectTo : '/#'
			});

			$stateProvider.state('main', {
				url : "/",
				templateUrl : 'partials/accumulo.html'
			})
			//
			// Monitor
			//
			.state('monitor', {
				url : "/monitor",
				abstract : true,
				templateUrl : 'partials/monitor/monitor.html',
				controller : 'MonitorCtrl'
			}).state('monitor.overview', {
				url : "", // default view
				templateUrl : 'partials/monitor/monitor.overview.html',
				data : {
					title : 'Accumulo Overview'
				}
			}).state('monitor.master', {
				url : "/master",
				templateUrl : 'partials/monitor/monitor.master.html',
				data : {
					title : 'Master Server'
				}
			}).state('monitor.tserver', {
				url : "/tserver",
				templateUrl : 'partials/monitor/monitor.nyi.html',
				data : {
					title : 'Tablet Server Status'
				}
			}).state('monitor.activity', {
				url : "/activity",
				templateUrl : 'partials/monitor/monitor.nyi.html',
				data : {
					title : 'Server Activity'
				}
			}).state('monitor.gc', {
				url : "/gc",
				templateUrl : 'partials/monitor/monitor.nyi.html',
				data : {
					title : 'Garbage Collector Status'
				}
			}).state('monitor.tables', {
				url : "/tables",
				templateUrl : 'partials/monitor/monitor.nyi.html',
				data : {
					title : 'Table Status'
				}
			}).state('monitor.traces', {
				url : "/traces",
				templateUrl : 'partials/monitor/monitor.nyi.html',
				data : {
					title : 'Traces for the last 10 minutes'
				}
			}).state('monitor.doc', {
				url : "/doc",
				templateUrl : 'partials/monitor/monitor.nyi.html',
				data : {
					title : 'Documentation'
				}
			}).state('monitor.logs', {
				url : "/logs",
				templateUrl : 'partials/monitor/monitor.nyi.html',
				data : {
					title : 'Recent Logs'
				}
			})
			//
			// SHELL
			//
			.state('shell', {
				url : "/shell", // root route
				template : '<p>shell url</p>'
			})
			//
			// Proxy
			//
			.state('proxy', {
				url : "/proxy", // root route
				templateUrl : 'partials/proxy.html',
				controller : 'ProxyCtrl'
			}).state('proxy.hdfs', {
				url : "/hdfs", 
				template : '<p>proxy hdfs</p>'
			}).state('proxy.mapreduce', {
				url : "/mapreduce", // root route
				template : '<p>proxy mapreduce</p>'
			}).state('proxy.accumulo', {
				url : "/accumulo", // root route
				template : '<p>proxy accumulo</p>'
			})
			//
			// REST
			//
			.state('rest', {
				url : "/rest", // root route
				template : '<p>rest url</p>'
			});
		} ]);

// allow $state in templates
accumuloApp.run(function($rootScope, $state, $stateParams) {
	$rootScope.$state = $state;
	$rootScope.$stateParams = $stateParams;
});
