# cordova-plugin-gprinter-wis
A Cordova Plugin of Gprinter Bluetooth from WIS

## Functions

<dl>
<dt><a href="#initialize">initialize(params, success, error)</a></dt>
<dd><p>初始化蓝牙</p>
</dd>
<dt><a href="#connectDevice">connectDevice(address, success, error)</a></dt>
<dd><p>连接打印机</p>
</dd>
<dt><a href="#getPairedDevices">getPairedDevices(success, error)</a></dt>
<dd><p>获取已配对的蓝牙设备</p>
</dd>
<dt><a href="#closeConnect">closeConnect(success, error)</a></dt>
<dd><p>关闭连接</p>
</dd>
<dt><a href="#printTest">printTest(success, error)</a></dt>
<dd><p>打印测试</p>
</dd>
<dt><a href="#print">print(success, error, index)</a></dt>
<dd><p>打印，执行打印指令集，打印最后一步</p>
</dd>
<dt><a href="#printWithCount">printWithCount(success, error)</a></dt>
<dd><p>打印，执行打印指令集，打印最后一步</p>
</dd>
<dt><a href="#createTsc">createTsc(index)</a> ⇒ <code>this</code></dt>
<dd><p>创建标签打印指令（打印标签的第一步）</p>
</dd>
<dt><a href="#addSize">addSize(pageWidth, pageHeight)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于设定卷标纸的宽度和长度</p>
</dd>
<dt><a href="#addGap">addGap(printGap)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于定义两张卷标纸间的垂直间距距离</p>
</dd>
<dt><a href="#addBar">addBar(x, y, width, height)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于在标签上画线</p>
</dd>
<dt><a href="#addBox">addBox(x_start, y_start, x_end, y_end, thickness)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于在卷标上绘制矩形方框</p>
</dd>
<dt><a href="#addBarCode">addBarCode(x, y, codetype, height, readable, rotation, content)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用来画一维条码
CODE128(&quot;128&quot;),CODE128M(&quot;128M&quot;),EAN128(&quot;EAN128&quot;),ITF25(&quot;25&quot;),ITF25C(&quot;25C&quot;),
CODE39(&quot;39&quot;),CODE39C(&quot;39C&quot;),CODE39S(&quot;39S&quot;),
CODE93(&quot;93&quot;),EAN13(&quot;EAN13&quot;),EAN13_2(&quot;EAN13+2&quot;),EAN13_5(&quot;EAN13+5&quot;),EAN8(&quot;EAN8&quot;),
EAN8_2(&quot;EAN8+2&quot;),EAN8_5(&quot;EAN8+5&quot;),CODABAR(&quot;CODA&quot;),POST(&quot;POST&quot;),UPCA(&quot;UPCA&quot;),
UPCA_2(&quot;UPCA+2&quot;),UPCA_5(&quot;UPCA+5&quot;),UPCE(&quot;UPCE13&quot;),UPCE_2(&quot;UPCE13+2&quot;),UPCE_5(&quot;UPCE13+5&quot;),
CPOST(&quot;CPOST&quot;),MSI(&quot;MSI&quot;), MSIC(&quot;MSIC&quot;),PLESSEY(&quot;PLESSEY&quot;),ITF14(&quot;ITF14&quot;),EAN14(&quot;EAN14&quot;)</p>
</dd>
<dt><a href="#addQRCode">addQRCode(x, y, level, width, rotation, content)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用来打印二维码</p>
</dd>
<dt><a href="#addText">addText(x, y, font, rotation, x_, y_, str)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于打印字符串</p>
</dd>
<dt><a href="#addOffset">addOffset(offset)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于控制在剥离模式时（peel-off mode）每张卷标停止的位置，在打印
下一张时打印机会将原先多推出或少推出的部分以回拉方式补偿回来。该指令仅
适用于剥离模式。</p>
</dd>
<dt><a href="#addSpeed">addSpeed(printSpeed)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于控制打印速度</p>
</dd>
<dt><a href="#addDensity">addDensity(printDensity)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于控制打印时的浓度</p>
</dd>
<dt><a href="#addDirection">addDirection(direction, mirror)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于定义打印时出纸和打印字体的方向</p>
</dd>
<dt><a href="#addReference">addReference(x, y)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于定义卷标的参考坐标原点。坐标原点位置和打印方向有关
打印机分辨率200 DPI:  1 mm = 8  dots
打印机分辨率300 DPI:  1 mm = 12 dots</p>
</dd>
<dt><a href="#addShift">addShift(n)</a> ⇒ <code>this</code></dt>
<dd><p>该指令表示标签打印偏移量多少设置
打印机分辨率200 DPI:  1 mm = 8  dots
打印机分辨率300 DPI:  1 mm = 12 dots</p>
</dd>
<dt><a href="#addCodepage">addCodepage(codepage)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于选择对应的国际字符集
8-bit codepage 字符集代表
437:United States
850:Multilingual
852:Slavic
860:Portuguese
863:Canadian/French
865:Nordic</p>
<p>Windows code page
1250:Central Europe
1252:Latin I
1253:Greek
1254:Turkish</p>
</dd>
<dt><a href="#addCls">addCls()</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于清除图像缓冲区（image buffer)的数据</p>
</dd>
<dt><a href="#addFeed">addFeed(feed)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于将标签纸向前推送指定的长度
打印机分辨率200 DPI:1 mm = 8  dots
打印机分辨率300 DPI:1 mm = 12 dots</p>
</dd>
<dt><a href="#addBackFeed">addBackFeed(backup)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于将标签纸向后回拉指定的长度</p>
</dd>
<dt><a href="#addFromfeed">addFromfeed()</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于控制打印机进一张标签纸</p>
</dd>
<dt><a href="#addHome">addHome()</a> ⇒ <code>this</code></dt>
<dd><p>在使用含有间隙或黑标的标签纸时，若不能确定第一张标签纸是否在正确打印位
置时，此指令可将标签纸向前推送至下一张标签纸的起点开始打印。标签尺寸和
间隙需要在本条指令前设置
注：使用该指令时，纸张高度大于或等于30 mm</p>
</dd>
<dt><a href="#addPagePrint">addPagePrint()</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于打印出存储于影像缓冲区内的数据</p>
</dd>
<dt><a href="#addPrint">addPrint(page, n)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于打印出存储于影像缓冲区内的数据</p>
</dd>
<dt><a href="#addSound">addSound(level, interval)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于控制蜂鸣器的频率，可设定10 阶的声音，每阶声音的长短由第二个参数控制</p>
</dd>
<dt><a href="#addLimitfeed">addLimitfeed(limit)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于设定打印机进纸时，若经过所设定的长度仍无法侦测到垂直间距，则打印机在连续纸模式工作</p>
</dd>
<dt><a href="#addSelfTest">addSelfTest()</a> ⇒ <code>this</code></dt>
<dd><p>打印自检页</p>
</dd>
<dt><a href="#addErase">addErase(x_start, y_start, x_width, y_height)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用于清除影像缓冲区部分区域的数据</p>
</dd>
<dt><a href="#addReverse">addReverse(x_start, y_start, x_width, y_height)</a> ⇒ <code>this</code></dt>
<dd><p>将指定的区域反相打印</p>
</dd>
<dt><a href="#addPeel">addPeel(n)</a> ⇒ <code>this</code></dt>
<dd><p>该指令用来启动/关闭剥离模式，默认值为关闭</p>
</dd>
<dt><a href="#addTear">addTear(n)</a> ⇒ <code>this</code></dt>
<dd><p>此命令是用来启用/禁用撕纸位置走到撕纸处，此设置关掉电源后将保存在打印机内</p>
</dd>
<dt><a href="#addReprint">addReprint(n)</a> ⇒ <code>this</code></dt>
<dd><p>此命令将禁用/启用标签机在无纸或开盖错误发生后，上纸或合盖后重新打印一次标签内容</p>
</dd>
<dt><a href="#addCut">addCut(n)</a> ⇒ <code>this</code></dt>
<dd><p>此命令用于设置切刀状态，关闭打印机电源后，该设置将会被存储在打印机内存中</p>
</dd>
<dt><a href="#addCutterPieces">addCutterPieces(pieces)</a> ⇒ <code>this</code></dt>
<dd><p>在PRINT 命令结束后切纸</p>
</dd>
<dt><a href="#addCut">addCut(n)</a> ⇒ <code>this</code></dt>
<dd><p>开启带Response的打印，用于连续打印</p>
</dd>
</dl>

<a name="initialize"></a>

## initialize(params, success, error)
初始化蓝牙

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| params | <code>\*</code> | {request: true} |
| success | <code>\*</code> |  |
| error | <code>\*</code> |  |

<a name="connectDevice"></a>

## connectDevice(address, success, error)
连接打印机

**Kind**: global function

| Param | Type |
| --- | --- |
| address | <code>\*</code> |
| success | <code>\*</code> |
| error | <code>\*</code> |

<a name="getPairedDevices"></a>

## getPairedDevices(success, error)
获取已配对的蓝牙设备

**Kind**: global function

| Param | Type |
| --- | --- |
| success | <code>\*</code> |
| error | <code>\*</code> |

<a name="closeConnect"></a>

## closeConnect(success, error)
关闭连接

**Kind**: global function

| Param | Type |
| --- | --- |
| success | <code>\*</code> |
| error | <code>\*</code> |

<a name="printTest"></a>

## printTest(success, error)
打印测试

**Kind**: global function

| Param | Type |
| --- | --- |
| success | <code>\*</code> |
| error | <code>\*</code> |

<a name="print"></a>

## print(success, error, index)
打印，执行打印指令集，打印最后一步

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| success | <code>\*</code> |  |
| error | <code>\*</code> |  |
| index | <code>\*</code> | 打印次数 |

<a name="printWithCount"></a>

## printWithCount(success, error)
打印，执行打印指令集，打印最后一步

**Kind**: global function

| Param | Type |
| --- | --- |
| success | <code>\*</code> |
| error | <code>\*</code> |

<a name="createTsc"></a>

## createTsc(index) ⇒ <code>this</code>
创建标签打印指令（打印标签的第一步）

**Kind**: global function

| Param | Type |
| --- | --- |
| index | <code>\*</code> |

<a name="addSize"></a>

## addSize(pageWidth, pageHeight) ⇒ <code>this</code>
该指令用于设定卷标纸的宽度和长度

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| pageWidth | <code>\*</code> | 标签宽度 单位mm |
| pageHeight | <code>\*</code> | 标签高度 单位mm |

<a name="addGap"></a>

## addGap(printGap) ⇒ <code>this</code>
该指令用于定义两张卷标纸间的垂直间距距离

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| printGap | <code>\*</code> | 标签间隙 单位mm |

<a name="addBar"></a>

## addBar(x, y, width, height) ⇒ <code>this</code>
该指令用于在标签上画线

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| x | <code>\*</code> | 线条左上角X 坐标，单位dots |
| y | <code>\*</code> | 线条左上角Y 坐标，单位dots |
| width | <code>\*</code> | 线宽，单位dots |
| height | <code>\*</code> | 线高，单位dots |

<a name="addBox"></a>

## addBox(x_start, y_start, x_end, y_end, thickness) ⇒ <code>this</code>
该指令用于在卷标上绘制矩形方框

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| x_start | <code>\*</code> | 方框左上角X 坐标，单位dots |
| y_start | <code>\*</code> | 方框左上角Y 坐标，单位dots |
| x_end | <code>\*</code> | 方框右下角X 坐标，单位dots |
| y_end | <code>\*</code> | 方框右下角Y 坐标，单位dots |
| thickness | <code>\*</code> | 方框线宽，单位dots |

<a name="addBarCode"></a>

## addBarCode(x, y, codetype, height, readable, rotation, content) ⇒ <code>this</code>
CPOST("CPOST"),MSI("MSI"), MSIC("MSIC"),PLESSEY("PLESSEY"),ITF14("ITF14"),EAN14("EAN14")

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| x | <code>\*</code> | 左上角水平坐标起点，以点（dot）表示 |
| y | <code>\*</code> | 左上角垂直坐标起点，以点（dot）表示 |
| codetype | <code>\*</code> | 条码类型 |
| height | <code>\*</code> | 条形码高度，以点（dot）表示 |
| readable | <code>\*</code> | 0 表示人眼不可识，1 表示人眼可识 |
| rotation | <code>\*</code> | 条形码旋转角度，顺时针方向 0,90,180,270 |
| content | <code>\*</code> | 打印内容 |

<a name="addQRCode"></a>

## addQRCode(x, y, level, width, rotation, content) ⇒ <code>this</code>
该指令用来打印二维码

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| x | <code>\*</code> | 二维码水平方向起始点坐标 |
| y | <code>\*</code> | 二维码垂直方向起始点坐标 |
| level | <code>\*</code> | ECC level 选择QRCODE 纠错等级 L 7% M 15% Q 25% H 30% |
| width | <code>\*</code> | cell width 二维码宽度1-10 |
| rotation | <code>\*</code> | 旋转角度（顺时针方向） 0，90，180，270 |
| content | <code>\*</code> | 内容 |

<a name="addText"></a>

## addText(x, y, font, rotation, x_, y_, str) ⇒ <code>this</code>
该指令用于打印字符串

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| x | <code>\*</code> | 文字X 方向起始点坐标 |
| y | <code>\*</code> | 文字Y 方向起始点坐标 |
| font | <code>\*</code> | 字体名称  1 8×12 dot 英数字体  2 12×20 dot 英数字体  3 16×24 dot 英数字体  4 24×32 dot 英数字体  5 32×48 dot 英数字体  6 14×19 dot 英数字体OCR-B  7 21×27 dot 英数字体OCR-B  8 14×25 dot 英数字体OCR-A  9 9×17 dot 英数字体  10 12×24 dot 英数字体  TSS16.BF2 简体中文16×16（GB 码）  TSS20.BF2 简体中文20×20（GB 码）  TST24.BF2 繁体中文24×24（大五码）  TSS24.BF2 简体中文24×24（GB 码）  K 韩文24×24Font（KS 码） TSS32.BF2 简体中文32×32（GB 码 ） |
| rotation | <code>\*</code> | 文字旋转角度（顺时针方向） 0， 90， 180， 270 |
| x_ | <code>\*</code> |  |
| y_ | <code>\*</code> |  |
| str | <code>\*</code> | 内容 |

<a name="addOffset"></a>

## addOffset(offset) ⇒ <code>this</code>
适用于剥离模式。将原先多推出或少推出的部分以回拉方式补偿回来。该指令仅

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| offset | <code>\*</code> | 纸张停止的距离 单位mm |

<a name="addSpeed"></a>

## addSpeed(printSpeed) ⇒ <code>this</code>
该指令用于控制打印速度

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| printSpeed | <code>\*</code> | 1<=printSpeed<=4 实际支持速度以自检页为准 |

<a name="addDensity"></a>

## addDensity(printDensity) ⇒ <code>this</code>
该指令用于控制打印时的浓度

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| printDensity | <code>\*</code> | 0<=printDensity<=15 |

<a name="addDirection"></a>

## addDirection(direction, mirror) ⇒ <code>this</code>
该指令用于定义打印时出纸和打印字体的方向

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| direction | <code>\*</code> | 0(横向)和1(纵向) |
| mirror | <code>\*</code> | 0(正常)和1(镜像) |

<a name="addReference"></a>

## addReference(x, y) ⇒ <code>this</code>
打印机分辨率300 DPI:  1 mm = 12 dots标原点位置和打印方向有关

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| x | <code>\*</code> | 水平方向的坐标位置,单位dots |
| y | <code>\*</code> | 垂直方向的坐标位置,单位dots |

<a name="addShift"></a>

## addShift(n) ⇒ <code>this</code>
打印机分辨率300 DPI:  1 mm = 12 dots

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| n | <code>\*</code> | 偏移量 ，单位dot 1mm=8 dots |

<a name="addCodepage"></a>

## addCodepage(codepage) ⇒ <code>this</code>
1254:Turkish Europe集代表符集

**Kind**: global function

| Param | Type |
| --- | --- |
| codepage | <code>\*</code> |

<a name="addCls"></a>

## addCls() ⇒ <code>this</code>
该指令用于清除图像缓冲区（image buffer)的数据

**Kind**: global function
<a name="addFeed"></a>

## addFeed(feed) ⇒ <code>this</code>
打印机分辨率300 DPI:1 mm = 12 dots度

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| feed | <code>\*</code> | 点数dots |

<a name="addBackFeed"></a>

## addBackFeed(backup) ⇒ <code>this</code>
该指令用于将标签纸向后回拉指定的长度

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| backup | <code>\*</code> | 点数dots |

<a name="addFromfeed"></a>

## addFromfeed() ⇒ <code>this</code>
该指令用于控制打印机进一张标签纸

**Kind**: global function
<a name="addHome"></a>

## addHome() ⇒ <code>this</code>
注：使用该指令时，纸张高度大于或等于30 mm纸的起点开始打印。标签尺寸和

**Kind**: global function
<a name="addPagePrint"></a>

## addPagePrint() ⇒ <code>this</code>
该指令用于打印出存储于影像缓冲区内的数据

**Kind**: global function
<a name="addPrint"></a>

## addPrint(page, n) ⇒ <code>this</code>
该指令用于打印出存储于影像缓冲区内的数据

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| page | <code>\*</code> | 打印份数 |
| n | <code>\*</code> |  |

<a name="addSound"></a>

## addSound(level, interval) ⇒ <code>this</code>
该指令用于控制蜂鸣器的频率，可设定10 阶的声音，每阶声音的长短由第二个参数控制

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| level | <code>\*</code> | 音阶:0-9 |
| interval | <code>\*</code> | 间隔时间:1-4095 |

<a name="addLimitfeed"></a>

## addLimitfeed(limit) ⇒ <code>this</code>
该指令用于设定打印机进纸时，若经过所设定的长度仍无法侦测到垂直间距，则打印机在连续纸模式工作

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| limit | <code>\*</code> | 检测垂直间距 点数dots |

<a name="addSelfTest"></a>

## addSelfTest() ⇒ <code>this</code>
打印自检页

**Kind**: global function
<a name="addErase"></a>

## addErase(x_start, y_start, x_width, y_height) ⇒ <code>this</code>
该指令用于清除影像缓冲区部分区域的数据

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| x_start | <code>\*</code> | 反相区域左上角X 坐标，单位dot |
| y_start | <code>\*</code> | 反相区域左上角Y 坐标，单位dot |
| x_width | <code>\*</code> | 反相区域宽度，单位dot |
| y_height | <code>\*</code> | 反相区域高度，单位dot |

<a name="addReverse"></a>

## addReverse(x_start, y_start, x_width, y_height) ⇒ <code>this</code>
将指定的区域反相打印

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| x_start | <code>\*</code> | 反相区域左上角X 坐标，单位dot |
| y_start | <code>\*</code> | 反相区域左上角Y 坐标，单位dot |
| x_width | <code>\*</code> | 反相区域宽度，单位dot |
| y_height | <code>\*</code> | 反相区域高度，单位dot |

<a name="addPeel"></a>

## addPeel(n) ⇒ <code>this</code>
该指令用来启动/关闭剥离模式，默认值为关闭

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| n | <code>\*</code> | 1 起动剥离模式 0 关闭剥离模式 |

<a name="addTear"></a>

## addTear(n) ⇒ <code>this</code>
此命令是用来启用/禁用撕纸位置走到撕纸处，此设置关掉电源后将保存在打印机内

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| n | <code>\*</code> | 1 启用撕纸位置走到撕纸处 0 禁用撕纸位置走到撕纸处，命令在起始位置有效 |

<a name="addReprint"></a>

## addReprint(n) ⇒ <code>this</code>
此命令将禁用/启用标签机在无纸或开盖错误发生后，上纸或合盖后重新打印一次标签内容

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| n | <code>\*</code> | 1 启用此功能 0 禁止此功能 |

<a name="addCut"></a>

## addCut(n) ⇒ <code>this</code>
此命令用于设置切刀状态，关闭打印机电源后，该设置将会被存储在打印机内存中

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| n | <code>\*</code> | 1 启用此功能 0 禁止此功能 |

<a name="addCutterPieces"></a>

## addCutterPieces(pieces) ⇒ <code>this</code>
在PRINT 命令结束后切纸

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| pieces | <code>\*</code> | pieces 0-65535，用于设置每几个标签进行切纸 |

<a name="addCut"></a>

## addCut(n) ⇒ <code>this</code>
开启带Response的打印，用于连续打印

**Kind**: global function

| Param | Type | Description |
| --- | --- | --- |
| n | <code>\*</code> | ON, OFF, BATCH |
