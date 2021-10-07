var exec = require('cordova/exec');

//连接打印机
exports.connectDevice = function (address,success, error) {
	// alert(address);
	exec(success, error, "WisGprinter", "connectDevice", [address]);
};
//获取已配对的蓝牙设备
exports.getPairedDevices = function (success, error) {
	exec(success, error, "WisGprinter", "getPairedDevices", []);
};
//关闭连接
exports.closeConnect = function () {
	exec(null, null, "WisGprinter", "closeConnect", []);
};
//打印测试
exports.printTest = function (success, error) {

	exec(success, error, "WisGprinter", "printTest", []);
};