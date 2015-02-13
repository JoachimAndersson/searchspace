/**
 * Created by Joachim on 2014-09-24.
 */
pageView.loading = true;
var row1 = new Row();
var row2 = new Row();

row1.height(200);

var column11 = new Col();
column11.sizeLG(3);
column11.sizeMD(4);
column11.sizeSM(6);
column11.sizeXS(12);
column11.title("11");
column11.chartName("Pie Chart");
column11.chart = new PieChart();
var search11 = new Search();
var query11 = new TermQuery();
query11.queryTargetType(new QueryTargetType());
query11.queryTargetType().id('Test1');
query11.queryTargetType().queryTarget("ELASTICSEARCH");
query11.queryTargetType().settings.push(new Parameter('Index','_all','xx'));
query11.queryTargetType().settings.push(new Parameter('Type','status','SELECT_WITH_QUERY'));
query11.size(10);
query11.field('user.screen_name');
query11.includeOther(false);
query11.includeUnknown(true);

search11.queries.push(query11);


column11.search(search11);


var column12 = new Col();
column12.sizeLG(3);
column12.sizeMD(4);
column12.sizeSM(6);
column12.sizeXS(12);
column12.title("12");

var column13 = new Col();
column13.sizeLG(3);
column13.sizeMD(4);
column13.sizeSM(6);
column13.sizeXS(12);
column13.title("13");

var column14 = new Col();
column14.sizeLG(3);
column14.sizeMD(4);
column14.sizeSM(6);
column14.sizeXS(12);
column14.title("14");


var column21 = new Col();
column21.title("21");
column21.chart = new PieChart();

var search21 = new Search();
var query21 = new TermQuery();
query21.queryTargetType(new QueryTargetType());
query21.queryTargetType().queryTarget("ELASTICSEARCH");
query21.queryTargetType().settings().push(new Parameter('Index','my_twitter_river','AUTOCOMPLETE_WITH_QUERY'));
query21.queryTargetType().settings().push(new Parameter('Type','status','AUTOCOMPLETE_WITH_QUERY'));
query21.queryTargetType().id('Test1');
query21.field("text");
query21.size(10);
query21.includeOther(true);
query21.includeUnknown(true);
search21.queries.push(query21)
column21.search(search21);


var column22 = new Col();
column22.title("22");
column22.chart = new PieChart();


var column23 = new Col();
column23.title("23");
column23.chart = new PieChart();


row1.columns.push(column11);
row1.columns.push(column12);
row1.columns.push(column13);
row1.columns.push(column14);

row2.columns.push(column21);
row2.columns.push(column22);
row2.columns.push(column23);

pageView.rows.push(row1);
pageView.rows.push(row2);


pageView.search();

