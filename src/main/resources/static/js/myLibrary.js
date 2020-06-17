async function logout() {
	try {
		if (await $.post(getAjaxObject('./api/user/logout'))) {
			alert('로그아웃 되었습니다.');

			window.location = './signIn.html';
		}
	}
	catch (e) {
		console.log(e);
	}
}

$('#portfolioBtn').on('click', (e) => window.location = './portfolio.html');
$('#boardBtn').on('click', (e) => window.location = './index.html');
$('#logoutBtn').on('click', (e) => logout());
$('#myPageBtn').on('click', (e) => window.location = './mypage.html');

function getExtension(filePath) {
	return filePath.substring(filePath.lastIndexOf('.') + 1, filePath.length).toLowerCase();
}

jQuery.each(['put', 'delete'], function (i, method) {
	jQuery[method] = function (info) {
		if (jQuery.isFunction(info.data)) {
			info.type = info.type || info.success;
			info.success = info.data;
			info.data = {};
		}

		return jQuery.ajax({
			url: info.url,
			type: method,
			dataType: info.dataType,
			data: info.data,
			success: info.success,
			error: info.error
		});
	};
});

function getAjaxObject(url, data = {}, bFile = false) {
	const result = {
		url: url,
		dataType: 'json',
		data: data
	};

	if (bFile) {
		result['enctype'] = 'multipart/form-data';
		result['contentType'] = false;
		result['processData'] = false;
	}

	return result;
}

function getParameters() {
	const url = document.location.href;
	const qs = url.substring(url.indexOf('?') + 1).split('&');
	const result = {};
	for (let i = 0; i < qs.length; i++) {
		qs[i] = qs[i].split('=');
		result[qs[i][0]] = decodeURIComponent(qs[i][1]);
	}

	return result;
}

Date.prototype.format = function (f) {
	if (!this.valueOf()) {
		return ' ';
	}

	const weekKorName = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];
	const weekKorShortName = ['일', '월', '화', '수', '목', '금', '토'];
	const weekEngName = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
	const weekEngShortName = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
	const d = this;

	return f.replace(/(yyyy|yy|MM|dd|KS|KL|ES|EL|HH|hh|mm|ss|a\/p)/gi, function ($1) {
		switch ($1) {
			case 'yyyy':
				return d.getFullYear(); // 년 (4자리)
			case 'yy':
				return (d.getFullYear() % 1000).zf(2); // 년 (2자리)
			case 'MM':
				return (d.getMonth() + 1).zf(2); // 월 (2자리)
			case 'dd':
				return d.getDate().zf(2); // 일 (2자리)
			case 'KS':
				return weekKorShortName[d.getDay()]; // 요일 (짧은 한글)
			case 'KL':
				return weekKorName[d.getDay()]; // 요일 (긴 한글)
			case 'ES':
				return weekEngShortName[d.getDay()]; // 요일 (짧은 영어)
			case 'EL':
				return weekEngName[d.getDay()]; // 요일 (긴 영어)
			case 'HH':
				return d.getHours().zf(2); // 시간 (24시간 기준, 2자리)
			case 'hh':
				return ((h = d.getHours() % 12) ? h : 12).zf(2); // 시간 (12시간 기준, 2자리)
			case 'mm':
				return d.getMinutes().zf(2); // 분 (2자리)
			case 'ss':
				return d.getSeconds().zf(2); // 초 (2자리)
			case 'a/p':
				return d.getHours() < 12 ? '오전' : '오후'; // 오전/오후 구분
			default:
				return $1;
		}
	});
};

String.prototype.string = function (len) {
	let s = '', i = 0;
	while (i++ < len) {
		s += this;
	}

	return s;
};

String.prototype.zf = function (len) {
	return '0'.string(len - this.length) + this;
};

Number.prototype.zf = function (len) {
	return this.toString().zf(len);
};

function getMemberNumbers(projectObj, presentNumbers, allNumbers, start, callbackPresent = null, callbackAll = null) {
	let presentNumber = 0, allNumber = 0;
	for (const member of projectObj.members) { // 현재 인원 현황
		const field = member.field.field;
		presentNumbers[field] = presentNumbers.hasOwnProperty(field) ? presentNumbers[field] + 1 : 1;
		presentNumber++;

		if (typeof callbackPresent === 'function') {
			callbackPresent(projectObj, member);
		}
	}

	for (const applyField of projectObj.applyFields) { // 전체 인원 현황
		const field = applyField.field;
		allNumbers[field] = applyField.number;
		allNumber += applyField.number;
		presentNumbers[field] = presentNumbers.hasOwnProperty(field) ? presentNumbers[field] : start;

		if (typeof callbackAll === 'function') {
			callbackAll(presentNumbers, allNumbers, applyField);
		}
	}

	return [presentNumber, allNumber];
}