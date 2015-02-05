/** Filters**/
var FilterTypes = {
    TERMFILTER: "TERMFILTER",
    EXISTSFILTER: "EXISTSFILTER"
};

addFilterType(FilterTypes.EXISTSFILTER, ExistsFilter);
addFilterType(FilterTypes.TERMFILTER, TermFilter);

function Filter(type) {
    var self = this;
    self.type = type;
    self.sourceTargetType;
    self.occur;
}

function TermFilter() {
    var self = this;
    ko.utils.extend(self, new Filter(FilterTypes.TERMFILTER));

    self.field;
    self.value;
}

function ExistsFilter() {
    var self = this;
    ko.utils.extend(self, new Filter(FilterTypes.EXISTSFILTER));

    self.field;
}

/** End filters.*/