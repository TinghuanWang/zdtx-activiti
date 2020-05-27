//游客年龄性别统计
function countAge(dataObj){
	var ageData = [0, 0, 0, 0, 0];
	if(dataObj && dataObj!=null){
		ageData = dataObj.dataMap["age"];
	}
	// alert(ageData);
	var touristAge = echarts.init(document.getElementById('touristAge'));
	option_touristAge = {
		color: ['#F6931C','#388BFF'],
		grid: {
			top: 10,
			left: 0,
			bottom: 30,
			right: 20,
			containLabel: true,
		},
		legend: {
			// data: ['男游客', '女游客'],
			bottom: 0,
			icon: 'circle',
			textStyle: {
				color: '#fff',
				fontSize: 12
			},
			itemGap: 20,
			itemWidth: 10,
			itemHeight: 10
		},
		tooltip: {
			trigger: "axis",
			formatter: '{b0}: {c0}人',
			axisPointer: {
				type: "shadow",
				label: {
					show: false
				}
			}
		},
		xAxis: {
			name: '人次',
			type: 'value',
			nameRotate: 1,
			splitLine: {
				show: false
			},
			axisLine: {
				lineStyle: {
					color: 'rgba(255,255,255, 0.5)'
				}
			},
			axisTick: {
				show: false
			},
			axisLabel: {
				fontSize: 12,
				color: '#fff'
			}
		},
		yAxis: {
			type: 'category',
			splitLine: {
				show: false
			},
			axisLine: {
				lineStyle: {
					color: 'rgba(255,255,255, 0.5)'
				}
			},
			axisTick: {
				show: false
			},
			axisLabel: {
				fontSize: 12,
				color: '#fff'
			},
			data: ['60岁以上', '40岁-60岁', '30岁-40岁', '18岁-30岁', '18岁以下']
		},
		series: [{
			name: '年龄范围',
			type: 'bar',
			stack: 'a',
			barWidth: '20',
			data: ageData
		}]
	};
	touristAge.setOption(option_touristAge);
}
//游客年龄性别统计  end

//游客来源地统计
function countSource(dataObj){
	var data = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
	if(dataObj && dataObj!=null){
		data = dataObj.dataMap["source"];
	}
	var touristSource = echarts.init(document.getElementById('touristSource'));
	option_touristSource = {
		tooltip: {
			trigger: "axis",
			formatter: '{b0}: {c0}人',
			axisPointer: {
				type: "shadow",
				textStyle: {
					color: "#fff"
				}
			},
		},
		grid: {
			borderWidth: 0,
			top: 20,
			bottom: 30,
			left: 0,
			right: 0,
			textStyle: {
				color: "#fff"
			},
			containLabel: true,
		},
		legend: {
			left: 'center',
			bottom: 0,
			itemWidth: 10,
			itemGap: 20,
			icon: 'circle',
			textStyle: {
				color: '#fff',
			}
		},
		calculable: true,
		xAxis: [{
			type: "category",
			axisLine: {
				lineStyle: {
					color: 'rgba(255,255,255, 0.5)'
				}
			},
			splitLine: {
				show: false
			},
			axisTick: {
				show: false
			},
			splitArea: {
				show: false
			},
			axisLabel: {
				interval: 0,
				color: '#fff'
			},
			data: ['南昌市', '景德镇市', '萍乡市', '江西其他市', '湖南', '广东', '广西', '河南', '海南','其他城市', ],
		}],
		yAxis: [{
			// name: '人',
			nameGap: 5,
			nameTextStyle: {
				color: '#fff'
			},
			type: "value",
			splitLine: {
				lineStyle: {
					color: 'rgba(255,255,255, 0.1)'
				}
			},
			axisLine: {
				lineStyle: {
					color: 'rgba(255,255,255, 0.5)'
				}
			},
			axisTick: {
				show: false
			},
			axisLabel: {
				interval: 0,
				color: '#fff'
			},
			splitArea: {
				show: false
			},
		}],
		series: [{
			name: "来源地区",
			type: "bar",
			barMaxWidth: 30,
			barGap: "10%",
			itemStyle: {
				normal: {
					color: "rgba(255,144,128,1)",
					label: {
						show: true,
						position: "top",
						formatter: function(p) {
							return p.value > 0 ? (p.value) : '';
						}
					}
				}
			},
			data: data,
		}]
	}
	touristSource.setOption(option_touristSource);
}
//游客来源地统计  end

//游客性别
function countSex(dataObj) {
	var objArr = [{
		value: 0,
		name: '男'
	},{
		value: 0,
		name: '女'
	},
	];
	if(dataObj && dataObj!=null){
		objArr = new Array();
		var dataMap = dataObj.dataArray[0];
		for(key in dataMap){
			var obj = {};
			obj.name = key == 'man'?'男':'女';
			obj.value = dataMap[key];
			objArr.push(obj);
		}
	}

	var touristType = echarts.init(document.getElementById('touristType'));
	option_touristType = {
		tooltip: {
			trigger: 'item',
			formatter: "{b} : {c}人 ({d}%)"
		},
		legend: {
			orient: 'vertical',
			x: '65%',
			y: 'center',
			data: ['男', '女'],
			icon: 'circle',
			itemWidth: 10,
			itemGap: 15,
			bottom: 0,
			textStyle: {
				fontSize: 12,
				color: '#fff'
			},
		},

		series: [{
			name: '',
			type: 'pie',
			radius: '80%',
			center: ['40%', '50%'],
			color: ['#F6931C','#388BFF'],
			data: objArr.sort(function(a, b) {
				return a.value - b.value
			}),
			roseType: 'radius',
			label: {
				normal: {
					show: false,
				}
			},
		}]
	};
	touristType.setOption(option_touristType);
}
//游客性别  end

//游客类型分析
function countTicketType(dataObj) {
	var touristAnalysis = echarts.init(document.getElementById('touristAnalysis'));
	var chartName = ['个人', '团体', '单位', '学生'];
	var chartData = ['0', '0', '0', '0']
	var data = []
	var legendName = []
	for (var i = 0; i < chartData.length; i++) {
		var c = {
			value: chartData[i],
			name: chartName[i] + '：' + chartData[i]
		}
		data[i] = c;
		legendName[i] = chartName[i] + '：' + chartData[i];
	}
	if(dataObj && dataObj!=null){
		var ticketTypePie = dataObj;
		var dataMap = ticketTypePie.dataArray[0];
		// alert(dataMap);
		var objArr = new Array();
		for(key in dataMap){
			var obj = {};
			obj.name = key;
			obj.value = dataMap[key];
			objArr.push(obj);
		}
		data = objArr;
		legendName = ticketTypePie.typeArray;
	}

	option_touristAnalysis = {
		tooltip: {
			trigger: 'item',
			formatter: '{b0}: {c0}',
		},
		legend: {
			orient: 'vertical',
			x: '65%',
			y: 'center',
			icon: 'circle',
			itemWidth: 10,
			itemHeight: 10,
			itemGap: 15,
			align: 'left',
			textStyle: {
				fontSize: 12,
				color: '#fff'
			},
			data: legendName
		},
		series: [{
			type: 'pie',
			radius: ['60%', '80%'],
			center: ['40%', '50%'],
			color: ['#FF999A', '#00e473', '#AF89D6', '#009DFF'],
			data: data,
			labelLine: {
				normal: {
					show: false,
					length: 20,
					length2: 20,
					lineStyle: {
						color: '#12EABE',
						width: 2
					}
				}
			},
			label: {
				normal: {
					show: false,
					formatter: '{c|{c}}\n{hr|}\n{d|{d}%}',
				}
			}
		}]
	};
	touristAnalysis.setOption(option_touristAnalysis);
}
//游客类型分析  end

//近三个月数据
function countTicketType2(dataObj){
	var legendName = ['个人'];
	var xArray = ['2019-05'];
	var data = [{
		name: '个人',
		type: 'bar',
		barWidth: '20',
		itemStyle: {
			normal: {
				color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
					offset: 0,
					color: '#fccb05'
				}, {
					offset: 1,
					color: '#f5804d'
				}]),
				barBorderRadius: 12,
			},
		},
		data: [0, 0, 0]
	}];
	if(dataObj && dataObj!=null){
		var ticketTypeBar = dataObj;
		var dataMap = ticketTypeBar.dataMap;
		var objArr = new Array();
		for(key in dataMap){
			var obj = {
				type: 'bar',
				barWidth: '20',
				itemStyle: {
					normal: {
						color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
							offset: 0,
							color: '#248ff7'
						}, {
							offset: 1,
							color: '#6851f1'
						}]),
						barBorderRadius: 11,
					}
				},
			};
			obj.name = key;
			obj.data = dataMap[key];
			objArr.push(obj);
		}
		data = objArr;
		xArray = ticketTypeBar.xArray;
		legendName = ticketTypeBar.yType;
	}
	var touristMouth = echarts.init(document.getElementById('touristMouth'));
	var option_touristMouth = {
		tooltip: {
			trigger: 'axis',
			axisPointer: {
				type: 'shadow'
			}
		},
		grid: {
			left: 0,
			right: 0,
			bottom: 30,
			top: 10,
			containLabel: true
		},
		legend: {
			data: legendName,
			right: 'center',
			bottom: 0,
			icon: 'circle',
			itemGap: 20,
			textStyle: {
				color: "#fff"
			},
			itemWidth: 12,
			itemHeight: 10,
		},
		xAxis: {
			type: 'category',
			data: xArray,
			axisLine: {
				lineStyle: {
					color: 'rgba(255,255,255, 0.5)'
				}
			},
			splitLine: {
				show: false
			},
			axisTick: {
				show: false
			},
			splitArea: {
				show: false
			},
			axisLabel: {
				interval: 0,
				color: '#fff'
			},
		},
		yAxis: {
			type: 'value',
			splitLine: {
				lineStyle: {
					color: 'rgba(255,255,255, 0.1)'
				}
			},
			axisLine: {
				lineStyle: {
					color: 'rgba(255,255,255, 0.5)'
				}
			},
			axisTick: {
				show: false
			},
			axisLabel: {
				interval: 0,
				color: '#fff'
			},
			splitArea: {
				show: false
			},
		},
		series: data
	};
	touristMouth.setOption(option_touristMouth);
}
//近三个月数据   end