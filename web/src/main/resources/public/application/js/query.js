/**Query*/

var QueryTypes = {
    TERM: "TERM"
};

addQueryTypes("Term Query",QueryTypes.TERM,TermQuery);


function Query(queryTerm) {
    var self = this;

    ko.utils.extend(self, new ConfigurableFields());

    self.queryType =  ko.observable(queryTerm);
    self.queryString = ko.observable("*");
    self.queryTargetType = ko.observable(new QueryTargetType());

    self.changeQueryTargetId = function (element) {
        var typeName = getQueryTargetSourceById(element.queryTargetType().id());
        var functionType = getQueryTargetTypeByName(typeName);
        var functionObject = new functionType()
        functionObject.id(element.queryTargetType().id());
        self.queryTargetType(functionObject);
    }
}


function TermQuery() {
    var self = this;
    ko.utils.extend(self, new Query(QueryTypes.TERM));

    self.size = ko.observable(10);
    self.field = ko.observable();
    self.includeUnknown = ko.observable(true);
    self.includeOther = ko.observable(true);


    self.addConfigurableField(new SelectQueryTargetType('Field', 'field', self.field, self.queryTargetType));
    self.addConfigurableField(new Input('Size', self.size));
    self.addConfigurableField(new CheckBox('IncludeUnknown', self.includeUnknown));
    self.addConfigurableField(new CheckBox('IncludeOther', self.includeOther));
}
/** End Query*/
