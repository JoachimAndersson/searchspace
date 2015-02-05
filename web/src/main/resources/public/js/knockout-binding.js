// knockout-jqAutocomplete 0.2.1 | (c) 2013 Ryan Niemeyer |  http://www.opensource.org/licenses/mit-license
;(function(factory) {
    if (typeof define === "function" && define.amd) {
        // AMD anonymous module
        define(["knockout", "jquery", "jquery.ui"], factory);
    } else {
        // No module loader - put directly in global namespace
        factory(window.ko, jQuery);
    }
})(function(ko, $) {
    ko.bindingHandlers.datetimepicker = {
        init: function (element, valueAccessor, allBindingsAccessor) {

            allBindings = allBindingsAccessor();

            var options = {
                format: 'YYYY-MM-DD HH:mm:ss',
                defaultDate: ko.utils.unwrapObservable(valueAccessor())
            };

            ko.utils.extend(options, allBindings.dateTimePickerOptions)

            $(element).datetimepicker(options).on("change.dp", function (evntObj) {
                var observable = valueAccessor();
                if (evntObj.timeStamp !== undefined) {
                    var picker = $(this).data("DateTimePicker");
                    var d = picker.getDate();
                    if (d != null) // this line
                        observable(d.format(picker.format));
                }
            });
        },
        update: function (element, valueAccessor) {
            var value = ko.utils.unwrapObservable(valueAccessor());
            var picker = $(element).data("DateTimePicker");
            var newDate = moment(value, picker.format,true)

            if(newDate.isValid()) {
                picker.setDate(value);
            }
        }
    };
});