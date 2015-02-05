/**QueryTargetType*/
addQueryTargetType(["ELASTICSEARCH"], ElasticQueryTargetType);

function getQueryTargetSource() {
    var tasksURI = baseURL+"rest/search/sources";
    var response = null;
    ajax(tasksURI, 'GET', null, false).done(function (data) {
            response = data;
            data.forEach(function (entry) {
                addQueryTargetSource(entry.id, entry.queryTarget);
            });
        }
    ).error(function (data) {
            alert(data);
        }
    );
    return response;
}

function QueryTargetType(queryTarget) {
    var self = this;
    ko.utils.extend(self, new ConfigurableFields());

    self.id = ko.observable();
    self.queryTarget = ko.observable(queryTarget);

}

function ElasticQueryTargetType() {
    var self = this;
    ko.utils.extend(self, new QueryTargetType("ELASTICSEARCH"));

    self.index = ko.observable("");
    self.type = ko.observable("");

    self.addConfigurableField(new AutoCompleteQueryTargetType('Index', 'index', self.index, ko.observable(self)));
    self.addConfigurableField(new SelectQueryTargetType('Type', 'type', self.type, ko.observable(self)));
}
/**End QueryTargetType*/

ElasticQueryTargetType.prototype.toJSON = function() {
    var copy = ko.toJS(this); //just a quick way to get a clean copy
    delete copy.configurableFields;
    return copy;
}