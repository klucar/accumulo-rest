'use strict';


// Declare app level module which depends on filters, and services
angular.module('accumuloApp', ['accumuloApp.filters', 
                               'accumuloApp.services', 
                               'accumuloApp.directives', 
                               'accumuloApp.controllers', 
                               'placeholders', 
                               'ui.bootstrap']).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/Monitor', {templateUrl: 'partials/monitor.html', controller: 'Monitor'});
    $routeProvider.when('/Shell', {templateUrl: 'partials/partial2.html', controller: 'MyCtrl2'});
    $routeProvider.when('/Proxy', {templateUrl: 'partials/partial2.html', controller: 'MyCtrl2'});
    $routeProvider.otherwise({redirectTo: '/Monitor'});
  }]);
