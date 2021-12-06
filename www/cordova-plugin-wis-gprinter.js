var exec = require("cordova/exec")

/**
 * 初始化蓝牙
 * @param {*} params {request: true}
 * @param {*} success
 * @param {*} error
 * @returns {}
 */
exports.initialize = function (params, success, error) {
  exec(success, error, "WisGprinter", "initialize", [params])
}

/**
 * 连接打印机
 * @param {*} address
 * @param {*} success
 * @param {*} error
 * @returns {}
 */
exports.connectDevice = function (address, success, error) {
  exec(success, error, "WisGprinter", "connectDevice", [address])
}
/**
 * 判断蓝牙打印机是否连接
 * @param {*}} success (open/close)
 * @param {*} error
 * @returns {}
 */
exports.isOpenStatus = function (success, error) {
  exec(success, error, "WisGprinter", "isOpenStatus")
}

/**
 * 获取已配对的蓝牙设备
 * @param {*} success
 * @param {*} error
 * @returns {}
 */
exports.getPairedDevices = function (success, error) {
  exec(success, error, "WisGprinter", "getPairedDevices", [])
}

/**
 * 关闭连接
 * @param {*} success
 * @param {*} error
 * @returns {}
 */
exports.closeConnect = function (success, error) {
  exec(success, error, "WisGprinter", "closeConnect", [])
}

/**
 * 打印测试
 * @param {*} success
 * @param {*} error
 * @returns {}
 */
exports.printTest = function (success, error) {
  exec(success, error, "WisGprinter", "printTest", [])
}

/**
 * 打印，执行打印指令集，打印最后一步
 * @param {*} success
 * @param {*} error
 * @param {*} index 打印次数
 * @returns {}
 */
exports.print = function (success, error) {
  exec(success, error, "WisGprinter", "print", [])
}
/**
 * 打印，执行打印指令集，打印最后一步
 * @param {*} success
 * @param {*} error
 * @returns {}
 */
exports.printWithCount = function (success, error, count) {
  exec(success, error, "WisGprinter", "printWithCount", [count])
}
/**
 * 创建标签打印指令（打印标签的第一步）
 * @param {*} index
 * @returns {}
 */
exports.createTsc = function (index = 0) {
  exec(null, null, "WisGprinter", "createTsc", [index])
  return exports
}
/**
 * 该指令用于设定卷标纸的宽度和长度
 * @param {*} pageWidth 标签宽度 单位mm
 * @param {*} pageHeight 标签高度 单位mm
 * @returns {}
 */
exports.addSize = function (pageWidth, pageHeight) {
  exec(null, null, "WisGprinter", "addSize", [pageWidth, pageHeight])
  return exports
}
/**
 * 该指令用于定义两张卷标纸间的垂直间距距离
 * @param {*} printGap 标签间隙 单位mm
 * @returns {}
 */
exports.addGap = function (printGap) {
  exec(null, null, "WisGprinter", "addGap", [printGap])
  return exports
}
/**
 * 该指令用于在标签上画线
 * @param {*} x 线条左上角X 坐标，单位dots
 * @param {*} y 线条左上角Y 坐标，单位dots
 * @param {*} width 线宽，单位dots
 * @param {*} height 线高，单位dots
 * @returns {}
 */
exports.addBar = function (x, y, width, height) {
  exec(null, null, "WisGprinter", "addBar", [x, y, width, height])
  return exports
}
/**
 * 该指令用于在卷标上绘制矩形方框
 * @param {*} x_start 方框左上角X 坐标，单位dots
 * @param {*} y_start 方框左上角Y 坐标，单位dots
 * @param {*} x_end 方框右下角X 坐标，单位dots
 * @param {*} y_end 方框右下角Y 坐标，单位dots
 * @param {*} thickness 方框线宽，单位dots
 * @returns {}
 */
exports.addBox = function (x_start, y_start, x_end, y_end, thickness) {
  exec(null, null, "WisGprinter", "addBox", [
    x_start,
    y_start,
    x_end,
    y_end,
    thickness
  ])
  return exports
}
/**
 * 该指令用来画一维条码
 * CODE128("128"),CODE128M("128M"),EAN128("EAN128"),ITF25("25"),ITF25C("25C"),
 * CODE39("39"),CODE39C("39C"),CODE39S("39S"),
 * CODE93("93"),EAN13("EAN13"),EAN13_2("EAN13+2"),EAN13_5("EAN13+5"),EAN8("EAN8"),
 * EAN8_2("EAN8+2"),EAN8_5("EAN8+5"),CODABAR("CODA"),POST("POST"),UPCA("UPCA"),
 * UPCA_2("UPCA+2"),UPCA_5("UPCA+5"),UPCE("UPCE13"),UPCE_2("UPCE13+2"),UPCE_5("UPCE13+5"),
 * CPOST("CPOST"),MSI("MSI"), MSIC("MSIC"),PLESSEY("PLESSEY"),ITF14("ITF14"),EAN14("EAN14")
 * @param {*} x 左上角水平坐标起点，以点（dot）表示
 * @param {*} y 左上角垂直坐标起点，以点（dot）表示
 * @param {*} codetype 条码类型
 * @param {*} height 条形码高度，以点（dot）表示
 * @param {*} readable 0 表示人眼不可识，1 表示人眼可识
 * @param {*} rotation 条形码旋转角度，顺时针方向 0,90,180,270
 * @param {*} content 打印内容
 * @returns {}
 */
exports.addBarCode = function (
  x,
  y,
  codetype,
  height,
  readable,
  rotation,
  content
) {
  exec(null, null, "WisGprinter", "addBarCode", [
    x,
    y,
    codetype,
    height,
    readable,
    rotation,
    content
  ])
  return exports
}
/**
 * 该指令用来打印二维码
 * @param {*} x 二维码水平方向起始点坐标
 * @param {*} y 二维码垂直方向起始点坐标
 * @param {*} level ECC level 选择QRCODE 纠错等级 L 7% M 15% Q 25% H 30%
 * @param {*} width cell width 二维码宽度1-10
 * @param {*} rotation 旋转角度（顺时针方向） 0，90，180，270
 * @param {*} content 内容
 * @returns {}
 */
exports.addQRCode = function (x, y, level, width, rotation, content) {
  exec(null, null, "WisGprinter", "addQRCode", [
    x,
    y,
    level,
    width,
    rotation,
    content
  ])
  return exports
}
/**
 * 该指令用于打印字符串
 * @param {*} x 文字X 方向起始点坐标
 * @param {*} y 文字Y 方向起始点坐标
 * @param {*} font 字体名称
 *  1 8×12 dot 英数字体
 *  2 12×20 dot 英数字体
 *  3 16×24 dot 英数字体
 *  4 24×32 dot 英数字体
 *  5 32×48 dot 英数字体
 *  6 14×19 dot 英数字体OCR-B
 *  7 21×27 dot 英数字体OCR-B
 *  8 14×25 dot 英数字体OCR-A
 *  9 9×17 dot 英数字体
 *  10 12×24 dot 英数字体
 *  TSS16.BF2 简体中文16×16（GB 码）
 *  TSS20.BF2 简体中文20×20（GB 码）
 *  TST24.BF2 繁体中文24×24（大五码）
 *  TSS24.BF2 简体中文24×24（GB 码）
 *  K 韩文24×24Font（KS 码）
 * TSS32.BF2 简体中文32×32（GB 码）
 * @param {*} rotation 文字旋转角度（顺时针方向） 0， 90， 180， 270
 * @param {*} x_
 * @param {*} y_
 * @param {*} str 内容
 * @returns {}
 */
exports.addText = function (
  x,
  y,
  str,
  font = "SIMPLIFIED_CHINESE",
  rotation = 0,
  x_ = 1,
  y_ = 1
) {
  exec(null, null, "WisGprinter", "addText", [
    x,
    y,
    font,
    rotation,
    x_,
    y_,
    str
  ])
  return exports
}

/**
 * 该指令用于控制在剥离模式时（peel-off mode）每张卷标停止的位置，在打印
 * 下一张时打印机会将原先多推出或少推出的部分以回拉方式补偿回来。该指令仅
 * 适用于剥离模式。
 * @param {*} offset 纸张停止的距离 单位mm
 * @returns {}
 */
exports.addOffset = function (offset) {
  exec(null, null, "WisGprinter", "addOffset", [offset])
  return exports
}
/**
 * 该指令用于控制打印速度
 * @param {*} printSpeed 1<=printSpeed<=4 实际支持速度以自检页为准
 * @returns {}
 */
exports.addSpeed = function (printSpeed) {
  exec(null, null, "WisGprinter", "addSpeed", [printSpeed])
  return exports
}
/**
 * 该指令用于控制打印时的浓度
 * @param {*} printDensity 0<=printDensity<=15
 * @returns {}
 */
exports.addDensity = function (printDensity) {
  exec(null, null, "WisGprinter", "addDensity", [printDensity])
  return exports
}
/**
 * 该指令用于定义打印时出纸和打印字体的方向
 * @param {*} direction 0(横向)和1(纵向)
 * @param {*} mirror 0(正常)和1(镜像)
 * @returns {}
 */
exports.addDirection = function (direction, mirror) {
  exec(null, null, "WisGprinter", "addDirection", [direction, mirror])
  return exports
}
/**
 * 该指令用于定义卷标的参考坐标原点。坐标原点位置和打印方向有关
 * 打印机分辨率200 DPI:  1 mm = 8  dots
 * 打印机分辨率300 DPI:  1 mm = 12 dots
 * @param {*} x 水平方向的坐标位置,单位dots
 * @param {*} y 垂直方向的坐标位置,单位dots
 * @returns {}
 */
exports.addReference = function (x, y) {
  exec(null, null, "WisGprinter", "addReference", [x, y])
  return exports
}
/**
 * 该指令表示标签打印偏移量多少设置
 * 打印机分辨率200 DPI:  1 mm = 8  dots
 * 打印机分辨率300 DPI:  1 mm = 12 dots
 * @param {*} n 偏移量 ，单位dot 1mm=8 dots
 * @returns {}
 */
exports.addShift = function (n) {
  exec(null, null, "WisGprinter", "addShift", [n])
  return exports
}
/**
 * 该指令用于选择对应的国际字符集
 * 8-bit codepage 字符集代表
 * 437:United States
 * 850:Multilingual
 * 852:Slavic
 * 860:Portuguese
 * 863:Canadian/French
 * 865:Nordic
 *
 * Windows code page
 * 1250:Central Europe
 * 1252:Latin I
 * 1253:Greek
 * 1254:Turkish
 * @param {*} codepage
 * @returns {}
 */
exports.addCodepage = function (codepage) {
  exec(null, null, "WisGprinter", "addCodepage", [codepage])
  return exports
}
/**
 * 该指令用于清除图像缓冲区（image buffer)的数据
 * @returns {}
 */
exports.addCls = function () {
  exec(null, null, "WisGprinter", "addCls", [])
  return exports
}
/**
 * 该指令用于将标签纸向前推送指定的长度
 * 打印机分辨率200 DPI:1 mm = 8  dots
 * 打印机分辨率300 DPI:1 mm = 12 dots
 * @param {*} feed 点数dots
 * @returns {}
 */
exports.addFeed = function (feed) {
  exec(null, null, "WisGprinter", "addFeed", [feed])
  return exports
}
/**
 * 该指令用于将标签纸向后回拉指定的长度
 * @param {*} backup 点数dots
 * @returns {}
 */
exports.addBackFeed = function (backup) {
  exec(null, null, "WisGprinter", "addBackFeed", [backup])
  return exports
}
/**
 * 该指令用于控制打印机进一张标签纸
 * @returns {}
 */
exports.addFromfeed = function () {
  exec(null, null, "WisGprinter", "addFromfeed", [])
  return exports
}
/**
 * 在使用含有间隙或黑标的标签纸时，若不能确定第一张标签纸是否在正确打印位
 * 置时，此指令可将标签纸向前推送至下一张标签纸的起点开始打印。标签尺寸和
 * 间隙需要在本条指令前设置
 * 注：使用该指令时，纸张高度大于或等于30 mm
 * @returns {}
 */
exports.addHome = function () {
  exec(null, null, "WisGprinter", "addHome", [])
  return exports
}
/**
 * 该指令用于打印出存储于影像缓冲区内的数据
 * @returns {} {}
 */
exports.addPagePrint = function () {
  exec(null, null, "WisGprinter", "addPagePrint", [])
  return exports
}
/**
 * 该指令用于打印出存储于影像缓冲区内的数据
 * @param {*} page 打印份数
 * @param {*} n
 * @returns {}
 */
exports.addPrint = function (page, n = 1) {
  exec(null, null, "WisGprinter", "addPrint", [page, n])
  return exports
}
/**
 * 该指令用于控制蜂鸣器的频率，可设定10 阶的声音，每阶声音的长短由第二个参数控制
 * @param {*} level 音阶:0-9
 * @param {*} interval 间隔时间:1-4095
 * @returns {}
 */
exports.addSound = function (level, interval) {
  exec(null, null, "WisGprinter", "addSound", [level, interval])
  return exports
}
/**
 * 该指令用于设定打印机进纸时，若经过所设定的长度仍无法侦测到垂直间距，则打印机在连续纸模式工作
 * @param {*} limit 检测垂直间距 点数dots
 * @returns {}
 */
exports.addLimitfeed = function (limit) {
  exec(null, null, "WisGprinter", "addLimitfeed", [limit])
  return exports
}
/**
 * 打印自检页
 * @returns {}
 */
exports.addSelfTest = function () {
  exec(null, null, "WisGprinter", "addSelfTest", [])
  return exports
}
/**
 * 该指令用于清除影像缓冲区部分区域的数据
 * @param {*} x_start 反相区域左上角X 坐标，单位dot
 * @param {*} y_start 反相区域左上角Y 坐标，单位dot
 * @param {*} x_width 反相区域宽度，单位dot
 * @param {*} y_height 反相区域高度，单位dot
 * @returns {}
 */
exports.addErase = function (x_start, y_start, x_width, y_height) {
  exec(null, null, "WisGprinter", "addErase", [
    x_start,
    y_start,
    x_width,
    y_height
  ])
  return exports
}
/**
 * 将指定的区域反相打印
 * @param {*} x_start 反相区域左上角X 坐标，单位dot
 * @param {*} y_start 反相区域左上角Y 坐标，单位dot
 * @param {*} x_width 反相区域宽度，单位dot
 * @param {*} y_height 反相区域高度，单位dot
 * @returns {}
 */
exports.addReverse = function (x_start, y_start, x_width, y_height) {
  exec(null, null, "WisGprinter", "addReverse", [
    x_start,
    y_start,
    x_width,
    y_height
  ])
  return exports
}
/**
 * 该指令用来启动/关闭剥离模式，默认值为关闭
 * @param {*} n 1 起动剥离模式 0 关闭剥离模式
 * @returns {}
 */
exports.addPeel = function (n) {
  exec(null, null, "WisGprinter", "addPeel", [n])
  return exports
}
/**
 * 此命令是用来启用/禁用撕纸位置走到撕纸处，此设置关掉电源后将保存在打印机内
 * @param {*} n 1 启用撕纸位置走到撕纸处 0 禁用撕纸位置走到撕纸处，命令在起始位置有效
 * @returns {}
 */
exports.addTear = function (n) {
  exec(null, null, "WisGprinter", "addTear", [n])
  return exports
}
/**
 * 此命令将禁用/启用标签机在无纸或开盖错误发生后，上纸或合盖后重新打印一次标签内容
 * @param {*} n 1 启用此功能 0 禁止此功能
 * @returns {}
 */
exports.addReprint = function (n) {
  exec(null, null, "WisGprinter", "addReprint", [n])
  return exports
}
/**
 * 此命令用于设置切刀状态，关闭打印机电源后，该设置将会被存储在打印机内存中
 * @param {*} n  1 启用此功能 0 禁止此功能
 * @returns {}
 */
exports.addCut = function (n) {
  exec(null, null, "WisGprinter", "addCut", [n])
  return exports
}
/**
 * 在PRINT 命令结束后切纸
 * @param {*} pieces pieces 0-65535，用于设置每几个标签进行切纸
 * @returns {}
 */
exports.addCutterPieces = function (pieces) {
  exec(null, null, "WisGprinter", "addCutterPieces", [pieces])
  return exports
}
/**
 * 开启带Response的打印，用于连续打印
 * @param {*} n ON, OFF, BATCH
 * @returns {}
 */
exports.addCut = function (n) {
  exec(null, null, "WisGprinter", "addCut", [n])
  return exports
}
