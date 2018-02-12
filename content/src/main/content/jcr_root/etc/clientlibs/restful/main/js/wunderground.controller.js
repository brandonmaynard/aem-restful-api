/**
 *** Restful API - Wunderground Controller
 ******************************************/

(function () {
    'use strict';

    angular.module('restful.controllers')
        .controller('wundergroundCtrl', wundergroundCtrl);

    wundergroundCtrl.$inject = ['$scope'];

    function wundergroundCtrl($scope) {
        $scope.actionURL = '/';
        $scope.uri = '';
        $scope.type = '';
        $scope.state = '';
        $scope.city = '';

        $scope.processSearch = function () {
            $scope.actionURL = encodeURI($scope.uri + '.wunderground.' + $scope.type + '.' + $scope.state + '.' + $scope.city + '.json');
        };
    }
})();
