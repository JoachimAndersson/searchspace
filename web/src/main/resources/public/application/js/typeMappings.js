/**
 * Created by Joachim on 2014-10-03.
 */
var filterNameToObject = {};
var queryTargetSoruceMap = {};
var queryTargetTypesToObjectMap = {};
var chartList = [];


function addFilterType(name, classType) {
    filterNameToObject[name] = classType;
}

function getFilterType(name) {
    return filterNameToObject[name];
}


function addQueryTargetType(name, classType) {
    queryTargetTypesToObjectMap[name] = classType;
}

function getQueryTargetTypeByName(name) {
    return queryTargetTypesToObjectMap[name];
}
function addQueryTargetSource(id, name) {
    queryTargetSoruceMap[id] = name;
}

function getQueryTargetSourceById(id) {
    return queryTargetSoruceMap[id];
}

var queryNameToObjectMap = {};

function addQueryTypes(name, queryType, query) {
    queryNameToObjectMap[queryType] = new QueryObject(name, queryType, query);
}

function getQueryNameToObjectMap() {
    return queryNameToObjectMap;
}
function getQueryByQueryType(queryType) {
    return queryNameToObjectMap[queryType].query;
}


function QueryObject(name, queryType, query) {
    var self = this;
    self.name = name;
    self.queryType = queryType;
    self.query = query;
}

function addChart(chartinformation) {
    chartList.push(chartinformation)
}

function getChartList() {
    return chartList;
}

function getChartObject(objectName) {
    var returnObject;
    chartList.forEach(function (entry) {
        if (objectName == entry.name) {
            returnObject = entry.chartType;
        }
    });

    return returnObject;
}


function ChartInformation(name, chartType) {
    var self = this;
    self.name = name;
    self.chartType = chartType;
    self.queryTypes = [];

    self.addQueryType = function (queryType) {
        self.queryTypes.push(queryType);
        return self;
    }
}
