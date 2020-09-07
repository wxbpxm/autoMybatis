var analyse = function(ret, obj){
	for(var k in obj) {
		if(k == "dic") {
			ret.push(obj[k]);
			console.println(obj[k]);
		} else {
			var v = obj[k];
			if(typeof(v) == 'object') {
				analyse(ret, v);
			}
		}
	}
}

var ret = [];
var target = "(" + obj + ")";
target = eval(target);
analyse(ret, target);
ret;
