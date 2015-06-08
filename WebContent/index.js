var pageSize = 8;
var curPage = 1;
var pageCount = 1;
var dataList;
function onSearchBtnClick(){
	curPage = 1;
	onSearch();
}
function onSearch() {
	var keyWord = $('#keyword').val();
	$.ajax({url:'SearchServlet',
		dataType:'json',
		contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
		data:{
			keyWord : encodeURI(keyWord),
			curPage : curPage,
			pageSize : pageSize
	},success : function(json){
		pageCount = json.pageCount;
		onSearchSuccess(json);
	}});
	console.log(keyWord);
}
function onSearchSuccess(json) {
	dataList = json.data;
	buildList(json.data);
	buildPages(Number(json.curPage),Number(json.pageSize),Number(json.pageCount));
}
function buildList(list_data) {
	var list = $('.left .list');
	list.empty();
	var i;
	for(i = 0; i < list_data.length; i++){
		var data_item = list_data[i];
		var item = $('#item_clone').clone();
		item.css('visibility','visible');
		item.find('#productId').text(data_item.id);
		item.find('#item_name').text(data_item.name);
		item.find("#item_price").text(data_item.price);
		item.find('#item_time').text(data_item.time);
		item.find("#item_img").attr('src',data_item.img);
		item.find("#item_url").attr('href',data_item.url);
		list.append(item);
	}
}
function buildPages(curPage,pageSize,pageCount) {
	var curPage_v = $('#curPage');
	var pageCount_v = $('#pageCount');
	curPage_v.text(curPage);
	pageCount_v.text(pageCount);
}
function onItemClick(div) {
	var item_view = $(div);
	var id = item_view.find('#productId').text();
	buildDetail(queryDetail(id));
}
function queryDetail(id) {
	var i;
	for(i = 0; i < dataList.length; i++){
		var data_item = dataList[i];
		if(data_item.id == id){
			return data_item;
		}
	}
	return 'not found';
}
function buildDetail(data_item) {
	if('not found' == data_item){
		alert('查询出错，查不到这个id的手机');
	}else{
		var right = $('.right');
		right.find('#detail_img').attr('src',data_item.img);
		right.find('#detail_id').text(data_item.id);
		right.find('#detail_name').text(data_item.name);
		right.find('#detail_price').text(data_item.price);
		right.find('#detail_time').text(data_item.time);
		right.find('#detail_url').text(data_item.url);
		right.find('#detail_url').attr('href',data_item.url);
	}
}
function onPrePageClick(){
	if(curPage > 1){
		curPage --;
		onSearch();
	}
}
function onNextPageClick(){
	if(curPage < pageCount){
		curPage ++;
		onSearch();
	}
}
var json_list = [ {
	id : "1938",
	name : "a",
	type : "1",
	price : "1342"
}, {
	id : "3451",
	name : "b",
	type : "2",
	price : "1111"
}, {
	id : "2938",
	name : "c",
	type : "1",
	price : "1342"
}, {
	id : "3351",
	name : "d",
	type : "2",
	price : "1111"
}, {
	id : "1931",
	name : "a",
	type : "1",
	price : "1342"
}, {
	id : "3452",
	name : "b",
	type : "2",
	price : "1111"
}, {
	id : "2937",
	name : "c",
	type : "1",
	price : "1342"
}, {
	id : "3352",
	name : "d",
	type : "2",
	price : "1111"
} ]
//http://localhost:8080/uselucene/index.html
//http://localhost:8080/uselucene/SearchServlet?keyWord=string%20in&curPage=1&pageSize=4