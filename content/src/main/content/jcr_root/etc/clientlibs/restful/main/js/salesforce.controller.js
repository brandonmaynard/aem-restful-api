/**
 *** SalesForce API - SalesForce Controller
 *******************************************/

(function () {
    'use strict';

    angular.module('restful.controllers')
        .controller('salesforceCtrl', salesforceCtrl);

    salesforceCtrl.$inject = ['$scope'];

    function salesforceCtrl($scope) {
        $scope.actionURL = '/';
        $scope.uri = '';

        $scope.processSearch = function () {
            $scope.actionURL = encodeURI($scope.uri + '.sfdc.json');
        };
    }
})();
