/**
 * Created by Joachim on 2014-10-03.
 */

function ConfigurableFields() {
    var self = this;
    self.configurableFields = ko.observableArray();

    self.addConfigurableField = function (field) {
        self.configurableFields.push(field);
    }
}


function InputAbstract(label, type, variable) {
    var self = this;
    self.type = ko.observable(type);
    self.label = ko.observable(label);
    self.variable = variable;
}

function Input(label, variable) {
    var self = this;
    ko.utils.extend(self, new InputAbstract(label, "INPUT", variable));
}

function AutoComplete(label, field, variable) {
    var self = this;
    self.field = field;

    ko.utils.extend(self, new InputAbstract(label, "AUTOCOMPLETE", variable));
}

function AutoCompleteQueryTargetType(label, field, variable, queryTargetType) {
    var self = this;
    self.queryTargetType = queryTargetType;

    ko.utils.extend(self, new AutoComplete(label, field, variable));

    self.getOptions = function (searchTerm, callback) {
        $.ajax({
            type: 'PUT',
            dataType: "json",
            contentType: "application/json",
            url: baseURL+"rest/search/autocomplete/" + self.field + "/" + searchTerm,
            data: ko.toJSON(self.queryTargetType)
        }).done(callback);
    }
}

function SelectQueryTargetType(label, field, variable, queryTargetType) {
    var self = this;
    self.queryTargetType = queryTargetType;
    self.field = field;
    self.listValues = ko.observableArray();
    self.loaded = false;

    ko.utils.extend(self, new InputAbstract(label, "SELECT", variable));

    self.queryTargetType.subscribe(function (val) {
        self.loadSubscribed()
    });

    self.loadSubscribed = function () {
        if (self.queryTargetType().index) {
            self.queryTargetType().index.subscribe(function (val) {
                if (self.loaded && field != 'index') {
                    self.getList();
                }
            });
        }
        if (self.queryTargetType().type) {
            self.queryTargetType().type.subscribe(function (val) {
                if (self.loaded && field != 'type') {
                    self.getList();
                }
            });
        }
    }

    self.getList = function () {
        if (self.queryTargetType && self.queryTargetType().id() && self.queryTargetType().queryTarget) {
            $.ajax({
                type: 'PUT',
                dataType: "json",
                contentType: "application/json",
                url: baseURL+"rest/search/list/" + self.field,
                data: ko.toJSON(self.queryTargetType),
                success: function (data) {
                    self.listValues(data);
                }
            });
        }
    }

    self.computedList = ko.computed({
        read: function () {
            if (!self.loaded) {
                self.loaded = true;
                self.getList();
            }
            return self.listValues();
        },
        deferEvaluation: true  //don't evaluate until someone requests the value
    }, self);

    self.loadSubscribed();

}

function CheckBox(label, variable) {
    var self = this;
    ko.utils.extend(self, new InputAbstract(label, "CHECKBOX", variable));
}

