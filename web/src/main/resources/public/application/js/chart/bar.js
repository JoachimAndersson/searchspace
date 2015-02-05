addChart(new ChartInformation('Bart Chart',BarChart).addQueryType(QueryTypes.TERM));

function BarChart() {

    var self = this;

    ko.utils.extend(self, new BaseChart());

    self.chart;
    self.container;

    self.setContainer = function (object) {
        self.container = object;
    }

    self.loadSettings = function (settings) {
    }

    self.loadData = function (data) {
        while (self.chart.series.length > 0) {
            self.chart.series[0].remove(false);
        }

        jQuery.each(data, function (index, queryResult) {
            self.chart.addSeries({
                type: 'column',
                name: queryResult.query.field,
                data: queryResult.data,
                dataLabels: {
                    enabled: true,
                    crop: false,
                    overflow: 'none'
                },
                point: {
                    events: {
                        click: function (event) {
                            filterEvent.call(this, queryResult, event);
                        }
                    }
                }});
        });
    }

     self.reRender = function () {
        self.chart.redraw();
        self.chart.render();
   }

    self.render = function () {
        self.chart = new Highcharts.Chart({
            credits: {
                enabled: false
            },
            chart: {
                renderTo: self.container.attr('id'),
                type: 'column',
                backgroundColor:'rgba(255, 255, 255, 0.1)'
            },
            title: {
                text: ''
            },
            tooltip: {
                pointFormat: '<b>{point.y}</b>'
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            }
        });
    }
}

