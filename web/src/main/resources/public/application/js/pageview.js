var baseURL = "http://localhost:8090/";

var pageView = new PageView();
ko.applyBindings(pageView, document.getElementById('body'));

function PageView() {
    var self = this;

    self.edit = ko.observable(true);
    self.rowToEdit = ko.observable();
    self.columnToEdit = ko.observable();


    self.loading = false;
    self.queryString = ko.observable('*');
    self.rows = ko.observableArray();
    self.filters = ko.observableArray();
    self.timeFrom = ko.observable('');
    self.timeTo = ko.observable('');
    self.templateName = ko.observable('');

    self.ajaxRequests = [];

    self.rows.subscribe(function (newValue) {
        if (!self.loading) {
        }
    });

    self.queryString.subscribe(function (newValue) {
        self.setQueryString(newValue);
    });

    self.filters.subscribe(function (newValue) {
    });

    self.timeFrom.subscribe(function (newValue) {
        self.setTimeFrom(newValue);
    });

    self.timeTo.subscribe(function (newValue) {
        self.setTimeTo(newValue);
    });

    self.setQueryString = function (queryString) {
        if (!queryString || queryString.length == 0) {
            queryString = "*";
        }
        jQuery.each(self.rows(), function (index, row) {

            jQuery.each(row.columns(), function (index, col) {
                jQuery.each(col.search().queries(), function (index, query) {
                    query.queryString(queryString);
                });
            });
        });
    }

    self.setTimeFrom = function (timeFrom) {
        jQuery.each(self.rows(), function (index, row) {
            jQuery.each(row.columns(), function (index, col) {
                col.search().timeFrom(timeFrom);
            });
        });
    }

    self.setTimeTo = function (timeTo) {
        jQuery.each(self.rows(), function (index, row) {
            jQuery.each(row.columns(), function (index, col) {
                col.search().timeTo(timeTo);
            });
        });
    }

    self.addFilter = function (filter) {
        self.filters.push(filter);
        jQuery.each(self.rows(), function (index, row) {
            jQuery.each(row.columns(), function (index, col) {
                col.search().filters(self.filters.slice(0));
            });
        });
    }

    self.removeFilter = function (filter) {
        self.filters.remove(filter);
        jQuery.each(self.rows(), function (index, row) {
            jQuery.each(row.columns(), function (index, col) {
                col.search().filters(self.filters.slice(0));
            });
        });
        self.search();
    }

    self.reRender = function () {
        jQuery.each(self.rows(), function (index, row) {
            jQuery.each(row.columns(), function (index, col) {
                if (col.chart) {
                    col.chart.reRender();
                }
            });
        });
    }

    self.search = function () {

        jQuery.each(self.ajaxRequests, function (index, request) {
            if (request && request['abort']) {
                request.abort();
            }
        });

        self.ajaxRequests = [];

        jQuery.each(self.rows(), function (index, row) {
            jQuery.each(row.columns(), function (index, col) {
                if (col.chart) {
                    search(col.search(), col.chart.loadData, col.container);
                }
            });
        });
    }

    self.searchEditedColumn = function () {
        if (self.columnToEdit().chart) {
            search(self.columnToEdit().search(), self.columnToEdit().chart.loadData, self.columnToEdit().container);
        }
    }

    self.createRow = function () {
        var createdNewRow = new Row();
        self.rows.push(createdNewRow);
        self.rowToEdit(createdNewRow);
    }

    self.editRow = function (item) {
        self.rowToEdit(item);
    }

    self.getSources = function () {
        return ko.observableArray(getQueryTargetSource());
    }

    self.getQueries = function () {
        var queries = ko.observableArray();
        jQuery.each(getQueryNameToObjectMap(), function (index, query) {
            queries.push(query);
        });
        return queries;
    }

    self.cloneObject = function () {
        var copy = ko.toJS(new PageView());
        delete copy.edit; //remove an extra property
        delete copy.rowToEdit; //remove an extra property
        delete copy.columnToEdit; //remove an extra property
        delete copy.loading; //remove an extra property
        delete copy.ajaxRequests; //remove an extra property

        copy.queryString = self.queryString(); //remove an extra property
        copy.timeFrom = self.timeFrom(); //remove an extra property
        copy.timeTo = self.timeTo(); //remove an extra property
        copy.templateName = self.templateName(); //remove an extra property

        jQuery.each(self.rows(), function (index, row) {
            copy.rows.push(row.cloneObject());
        });
        copy.filters = ko.toJS(self.filters); //remove an extra property
        return copy;
    }

    self.saveTemplate = function () {
        $.ajax({
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            url: baseURL + "rest/template/save",
            data: ko.toJSON(self.cloneObject()),
            success: function (data) {
            }
        });

    }


    self.loadTemplate = function (templateName) {
        $.ajax({
            type: 'GET',
            dataType: "json",
            contentType: "application/json",
            url: baseURL + "rest/template/template?templateName=" + templateName,
            success: function (data) {
                self.loadJson(ko.toJSON(data));
            }
        });

    }

    self.loadJson = function (json) {
        var parsed = JSON.parse(json);

        self.edit(true);
        self.rowToEdit();
        self.columnToEdit();
        self.loading = false;

        self.queryString();
        self.rows.removeAll();
        self.filters.removeAll();
        self.timeFrom();
        self.timeTo();


        jQuery.each(parsed.rows, function (index, row) {
            var newRow = new Row();
            loadObject(row, newRow, ["columns"]);
            self.rows.push(newRow);

            jQuery.each(row.columns, function (index, column) {
                var newColumn = new Col();
                loadObject(column, newColumn, ["chart", "container", "content", "actions", "search", "chartName"]);
                newRow.columns.push(newColumn);
                newColumn.chartName(column.chartName);

                var newSearch = new Search();

                newColumn.search(newSearch);

                jQuery.each(column.search.queries, function (index, query) {
                    var queryType = getQueryByQueryType(query.queryType);
                    if (queryType) {
                        var newQuery = new queryType();

                        loadObject(query, newQuery, ["queryType", "queryTargetType"]);

                        if (query.queryTargetType) {
                            newQuery.queryTargetType(loadQueryTargetType(query.queryTargetType));
                        }
                        newSearch.queries.push(newQuery);
                    }
                });
            });
        });

        jQuery.each(parsed.filters, function (index, filter) {
            var filterType = getFilterType(filter.type);
            if (filterType) {
                var newFilter = new filterType();
                loadObject(filter, newFilter, ["type", "sourceTargetType"]);

                newFilter.sourceTargetType = loadQueryTargetType(filter.sourceTargetType);
                self.addFilter(newFilter)

            }
        });

        self.setQueryString(parsed.queryString);
        self.setTimeFrom(parsed.timeFrom);
        self.setTimeTo(parsed.timeTo);
        self.templateName(parsed.templateName);
        self.search();
    }
}

function loadQueryTargetType(sourceTargetType) {
    if (sourceTargetType) {
        var targetType = getQueryTargetTypeByName(sourceTargetType.queryTarget);
        if (targetType) {
            var newTargetType = new targetType();
            loadObject(sourceTargetType, newTargetType, ["queryTarget"]);
            return newTargetType;
        }
    }
}


function loadObject(source, target, ignoreArray) {
    for (var key in source) {
        if (ignoreArray.indexOf(key) < 0) {
            if (typeof(target[key]) == "function") {
                target[key](source[key]);
            } else {
                target[key] = source[key];
            }
        } else {
        }
    }
}


function loadPageView(json) {
    var parsed = JSON.parse(json);
    pageView = new PageView();


}

function Row() {

    var self = this;
    self.height = ko.observable(300);
    self.columns = ko.observableArray();

    self.height.subscribe(function (newValue) {
        pageView.reRender();
    });

    self.removeRow = function (item) {
        pageView.rows.remove(item);
    }

    self.createColumn = function () {
        pageView.rowToEdit().columns.push(new Col());
    }

    self.addColumn = function (element, data) {
        var action = $("#" + element[1].id + "action");
        var content = $("#" + element[1].id + "cont");
        data.container = action;
        data.content = content;
        loadColumn(data);
    }

    self.editColumn = function (item) {
        pageView.columnToEdit(item);
    }

    self.cloneObject = function () {
        var copy = ko.toJS(new Row());
        copy.height = self.height();
        jQuery.each(self.columns(), function (index, column) {
            copy.columns.push(column.cloneObject());
        });

        return copy;
    }
}

function Col() {
    var self = this;
    self.search = ko.observable(new Search());
    self.chart;
    self.chartName = ko.observable("");
    self.title = ko.observable("");

    self.container;
    self.content;

    self.actions;
    self.sizeXS = ko.observable(12);
    self.sizeSM = ko.observable(8);
    self.sizeMD = ko.observable(6);
    self.sizeLG = ko.observable(4);

    self.queryJson = function () {
    }

    self.cloneObject = function () {
        var copy = ko.toJS(new Col());

        copy.search = ko.toJS(self.search());
        jQuery.each(copy.search.queries, function (index, query) {
            delete query.configurableFields;
        })

        copy.chartName = self.chartName();
        copy.title = self.title();
        copy.sizeXS = self.sizeXS();
        copy.sizeSM = self.sizeSM();
        copy.sizeMD = self.sizeMD();
        copy.sizeLG = self.sizeLG();


        copy.chartConfig = {};

        if (self.chart) {
            //Save chart properties
        }
        delete copy.container;
        delete copy.content;
        delete copy.action;
        delete copy.styleClass;

        return copy;
    }

    self.chartName.subscribe(function (newValue) {
        //Reset
        var chartObject = getChartObject(newValue);
        if (chartObject && self.container && self.content) {
            self.chart = new chartObject();
            loadColumn(self);
        }

    });


    self.removeColumn = function (item) {
        pageView.rowToEdit().columns.remove(item);
    }

    self.moveLeft = function (item) {
        var i = pageView.rowToEdit().columns.indexOf(item);
        if (i >= 1) {
            var array = pageView.rowToEdit().columns();
            pageView.rowToEdit().columns.splice(i - 1, 2, array[i], array[i - 1]);
        }
    }

    self.moveRight = function (item) {
        var i = pageView.rowToEdit().columns.indexOf(item);
        var array = pageView.rowToEdit().columns();
        if (i < array.length - 1) {
            pageView.rowToEdit().columns.splice(i, 2, array[i + 1], array[i]);
        }
    }

    self.styleClass = ko.pureComputed(function () {
        return 'col-lg-' + self.sizeLG() + ' col-md-' + self.sizeMD() + ' col-sm-' + self.sizeSM() + ' col-xs-' + self.sizeXS();
    });


    self.getChartNameList = function () {
        var charts = ko.observableArray();
        jQuery.each(getChartList(), function (index, chartinformation) {
            var contains = true;
            jQuery.each(self.search().queries, function (index2, query) {
                if (!jQuery.inArray(query.queryType, chartinformation.queryTypes)) {
                    contains = false;
                }
            });

            if (contains) {
                charts.push(chartinformation.name);
            }
        });
        return charts;
    }


}


function Search() {
    var self = this;

    self.queries = ko.observableArray();
    self.filters = ko.observableArray();
    self.timeFrom = ko.observable(null);
    self.timeTo = ko.observable(null);

    self.removeQuery = function (item, parent) {
        self.queries.remove(item);
    }

    self.addQuery = function () {
        var q = new Query();
        self.queries.push(q);
    }

    self.changeQueryType = function (element) {
        var queryTargetType = element.queryTargetType();
        var typeObject = getQueryByQueryType(element.queryType());

        var functionName = new typeObject();
        functionName.queryTargetType(queryTargetType);
        self.queries.replace(element, functionName);
    }
}


function getURLParameter(name) {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search) || [, ""])[1].replace(/\+/g, '%20')) || null
}


$(document).ready(function () {
    var templateName = getURLParameter("templateName");
    if (templateName) {
        pageView.loadTemplate(templateName);
    }
});