'use strict';

function getCookie(cname) {
	var name = cname + "=";
	var ca = document.cookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ')
			c = c.substring(1);
		if (c.indexOf(name) == 0)
			return c.substring(name.length, c.length);
	}
	return "";
}
var TEST = false;

var userID = getCookie("authentication");
console.log(userID);

var PREFIX_RQ = "";
// var PREFIX_RQ = "http://purple:9998";

var searchItemIntoArrayWithAttribute = function(array, attr, value) {
return searchItemIntoArrayWithAttribute(array, attr, value, "");
}

var searchItemIntoArrayWithAttribute = function(array, attr, value ,special) {
	for (var i = 0; i < array.length; i++) {
		 console.log("special + (array[i][attr] "+ (special + (array[i][attr])) );
		if (special + (array[i][attr]) == value) {
			return i;
		}
	}
	return null;
}



// Declare app level module which depends on views, and components
angular.module(
		'myApp',
		[ 'ngRoute', 'angularFileUpload', 'ui.bootstrap', 'myApp.index',
				'myApp.home', 'myApp.myvideos', 'myApp.mypictures',
				'myApp.myprofile', 'myApp.mycloud', 'myApp.friends',
				'myApp.friendSearch', 'myApp.newvideo' ]).config(
		[ '$routeProvider', function($routeProvider) {
			$routeProvider.otherwise({
				redirectTo : '/home'
			});
		} ]);
