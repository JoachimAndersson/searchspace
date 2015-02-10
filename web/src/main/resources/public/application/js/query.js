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
        var blank = getQueryTargetTypeById(element.queryTargetType().id());
        if(blank) {
            self.queryTargetType(blank.cloneObject());
        } else {
            self.queryTargetType(new QueryTargetType());
        }
    }
}


function TermQuery() {
    var self = this;
    ko.utils.extend(self, new Query(QueryTypes.TERM));

    self.size = ko.observable(10);
    self.field = ko.observable();
    self.includeUnknown = ko.observable(true);
    self.includeOther = ko.observable(true);


    self.addConfigurableField(new SelectQueryTargetType('Field', 'Field', self.field, self.queryTargetType));
    self.addConfigurableField(new Input('Size', self.size));
    self.addConfigurableField(new CheckBox('IncludeUnknown', self.includeUnknown));
    self.addConfigurableField(new CheckBox('IncludeOther', self.includeOther));
}
/** End Query*/
