addChart(new ChartInformation('3D Donout Chart',Dounout3D).addQueryType(QueryTypes.TERM));

function Dounout3D() {

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
                type: 'pie',
                data: queryResult.data,
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
                type: 'pie',
                renderTo: self.container.attr('id'),
                options3d: {
                    enabled: true,
                    alpha: 45
                },
                backgroundColor:'rgba(255, 255, 255, 0.1)'
            },
            title: {
                text: ''
            },
            tooltip: {
                pointFormat: '<b>{point.y}</b>'
            },
            plotOptions: {
                pie: {
                    innerSize: '30%',
                    depth: 45,
                    animation:false,
                    allowPointSelect: true,
                    cursor: 'pointer',
                    borderWidth: 0,
                    dataLabels: {
                        enabled: true,
                        formatter: function (ob) {

                            var truncated = this.point.name;
                            if (truncated.length > 30) {
                                truncated = truncated.substring(0, 30) + "...";
                            }
                            return '<b> ' + truncated + '</b>:' + this.point.percentage.toFixed(2) + '%';
                        },
                        style: {
                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                        }
                    }
                }
            }
        });
    }
}

