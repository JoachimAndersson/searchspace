$(function() {
/*
    ensureTemplates(["modalrow"]);
*/
});

function ensureTemplates(list) {
    var loadedTemplates = [];
    ko.utils.arrayForEach(list, function(name) {
        $.get("/application/template/" + name + ".html", function(template) {
            $("body").append("<script id=\"" + name + "\" type=\"text/html\">" + template + "<\/script>");
            loadedTemplates.push(name);
            if (list.length === loadedTemplates.length) {
                ko.applyBindings(pageView);
            }
        }) .fail(function(event) {
            alert( "error unable to load template"+name);
        });
    });
}