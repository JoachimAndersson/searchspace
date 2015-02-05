function ajax(uri, method, data, async) {

    var request = {
        type: method,
        dataType: "json",
        crossDomain: true,
        url: uri,
        async:async,
        contentType: "application/json",
        data: data,
        beforeSend: function () {
        },
        complete: function () {
        },
        success: function (data) {
            var index = pageView.ajaxRequests.indexOf(ajaxRequest);
            if (index > -1) {
                pageView.ajaxRequests.splice(index, 1);
            }
        },
        error: function (data) {
            var index = pageView.ajaxRequests.indexOf(ajaxRequest);
            if (index > -1) {
                pageView.ajaxRequests.splice(index, 1);
            }
        }
    };

    var ajaxRequest = $.ajax(request);
    pageView.ajaxRequests.push(ajaxRequest);

    return ajaxRequest;
}



function search(search, loadDataFunction, searchingObject) {

    var loadSpan = $('<i>');
    loadSpan.addClass("fa");
    loadSpan.addClass("fa-spinner");
    loadSpan.addClass("fa-spin");
    searchingObject.append(loadSpan);

    var jsonData = formToJSON(search);

    var tasksURI = baseURL + "rest/search";

    ajax(tasksURI, 'PUT', jsonData, true).done(function (data) {
            //Check if TermData
            var returnData = [];
            for (var i = 0; i < data.queryResultList.length; i++) {

                var queryResult = new QueryResult();
                if (data.queryResultList[i].queryType == QueryTypes.TERM) {
                    queryResult.data = getTermData(data.queryResultList[i], i);
                }
                queryResult.query = data.queryResultList[i].searchQuery;
                returnData[i] = queryResult;
            }
            loadDataFunction(returnData);
            loadSpan.detach();
        }
    ).error(function (data) {
            loadSpan.detach();
        });
}

function getTermData(data) {
    var partReturnData = [];
    for (var t = 0; t < data.list.length; t++) {
        var line = data.list[t];
        partReturnData.push([line.value, line.size]);
    }
    return partReturnData;
}

function formToJSON(object) {
    return ko.toJSON(object);
}

function loadColumn(column) {
    if (column.chart) {
        column.content.empty();
        var chart = column.chart;
        chart.setContainer(column.content);
        chart.loadSettings();
        chart.render();
        //Load Data
    }
}