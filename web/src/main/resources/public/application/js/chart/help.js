function filterEvent(queryResult, event) {
    var filter;

    if (this.name == 'unknown') {
        filter = new ExistsFilter();
        filter.field = queryResult.query.field;

        if (event.ctrlKey) {
            filter.occur = "MUST";
        } else if (event.altKey) {
            filter.occur = "SHOULD";
        } else {
            filter.occur = "MUST_NOT";
        }
    } else {

        filter = new TermFilter();
        filter.value = this.name;

        filter.field = queryResult.query.field;
        if (event.ctrlKey) {
            filter.occur = "MUST_NOT";
        } else if (event.altKey) {
            filter.occur = "SHOULD";
        } else {
            filter.occur = "MUST";
        }
    }
    filter.sourceTargetType = queryResult.query.queryTargetType;
    pageView.addFilter(filter);
    pageView.search();
}


function BaseChart(name) {
    var self = this;
    self.container;

    ko.utils.extend(self, new ConfigurableFields());

    self.setContainer = function (object) {
        self.container = object;
    }

    self.loadSettings = function (settings) {
    }

    self.loadData = function (data) {
    }

    self.reRender = function () {
    }

    self.render = function () {
    }

}
