/**QueryTargetType*/
function getQueryTargetSource() {
    var tasksURI = baseURL + "rest/search/sources";
    var response = null;
    ajax(tasksURI, 'GET', null, false).done(function (data) {
            response = data;
            data.forEach(function (entry) {

                var queryTarget = new QueryTargetType();
                queryTarget.id(entry.id);
                queryTarget.queryTarget(entry.queryTarget);
                if(entry.settings) {
                    entry.settings.forEach(function (attribute) {
                        queryTarget.settings().push(new Parameter(attribute.name, attribute.value, attribute.guiType))
                    });
                }
                if(entry.supportedQueryTypes) {
                    entry.supportedQueryTypes.forEach(function (value) {
                        queryTarget.supportedQueryTypes().push(value)
                    });
                }

                queryTarget.id(entry.id);
                addQueryTargetSource(entry.id, queryTarget);
            });
        }
    ).error(function (data) {
            alert(data);
        }
    );
    return response;
}

function QueryTargetType() {
    var self = this;
    ko.utils.extend(self, new ConfigurableFields());

    self.id = ko.observable();
    self.queryTarget = ko.observable();
    self.settings = ko.observableArray();
    self.supportedQueryTypes = ko.observableArray();

    self.settings.subscribe(function (changes) {
        changes.forEach(function (change) {
            if (change.status === 'added') {

                var baseQueryTargetType = getQueryTargetTypeById(self.id());
                if(baseQueryTargetType) {
                    baseQueryTargetType.settings().forEach(function (setting) {
                        if (setting.name() == change.value.name()) {
                            change.value.guiType(setting.guiType());
                        }
                    });
                }

                if (change.value.guiType() == 'AUTOCOMPLETE_WITH_QUERY') {
                    self.addConfigurableField(new AutoCompleteQueryTargetType(change.value.name(), change.value.name(), change.value.value, ko.observable(self)));
                }
                if (change.value.guiType() == 'SELECT_WITH_QUERY') {
                    self.addConfigurableField(new SelectQueryTargetType(change.value.name(), change.value.name(), change.value.value, ko.observable(self)));
                }
                if (change.value.guiType() == 'INPUT') {
                    self.addConfigurableField(new Input(change.value.name(), change.value.value));
                }
                if (change.value.guiType() == 'CHECKBOX') {
                    self.addConfigurableField(new CheckBox(change.value.name(), change.value.value));
                }
            }
            if (change.status === 'deleted') {
                console.log("Added or removed! The added/removed element is:", change.value);
            }

        });

    }, null, "arrayChange");

    self.cloneObject = function () {
        var copy = new QueryTargetType();

        copy.id(self.id());
        copy.queryTarget(self.queryTarget());
        jQuery.each(self.settings(), function (index, parameter) {
            copy.settings.push(parameter.cloneObject());
        });
        return copy;
    }
}

function Parameter(name, value, guiTypes) {
    var self = this;

    self.name = ko.observable(name);
    self.guiType = ko.observable(guiTypes);
    self.value = ko.observable(value);

    self.cloneObject = function () {
        return new Parameter(self.name(),self.value(),self.guiType());
    }
}

QueryTargetType.prototype.toJSON = function () {
    var copy = ko.toJS(this); //just a quick way to get a clean copy
    delete copy.configurableFields;
    return copy;
}