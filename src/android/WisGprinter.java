package com.isesol.wis;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PermissionHelper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.Set;
import java.util.UUID;
import java.io.IOException;
import java.io.OutputStream;

import com.gprinter.command.CpclCommand;
import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import android.util.Base64;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.BluetoothManager;
import android.content.*;
import android.os.*;
import android.app.Activity;
import android.view.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.content.BroadcastReceiver;
import android.content.Context;

public class WisGprinter extends CordovaPlugin {

    private final int REQUEST_ACCESS_FINE_LOCATION = 59628;
    private final int REQUEST_BT_ENABLE = 59627; /*Random integer*/
    private static final String LOG_TAG = "WisGprinter";
    private final String keyStatusReceiver = "statusReceiver";
    private final String keyStatus = "status";
    private final String keyError = "error";
    private final String keyMessage = "message";
    private final String keyRequest = "request";
    private final String statusDisabled = "disabled";
    private final String statusEnabled = "enabled";
    private final String logNotEnabled = "蓝牙不可用";

    private int id = 0;
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};


    // callbacks
    private CallbackContext connectCallback;
    private CallbackContext initCallbackContext;
    private CallbackContext dataAvailableCallback;
    private CallbackContext rawDataAvailableCallback;
    private CallbackContext enableBluetoothCallback;
    private CallbackContext deviceDiscoveredCallback;
    private CallbackContext mcallbackContext;
    private BluetoothAdapter bluetoothAdapter;



    private JSONArray requestArgs;
    // private PrinterServiceConnection conn = null;
    // private GpService mGpService = null;
    private Activity activity;

    private String oneModel, drawingRev, oneClass, oneCode, chipId, dateTime, specification = "";

    private boolean isConnection = false;// 蓝牙是否连接
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        activity = cordova.getActivity();

    }

    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.mcallbackContext = callbackContext;
        this.requestArgs = args;
        LabelCommand tsc;
        boolean result = true;
        switch (action) {
            case "initialize": // 初始化蓝牙
                initializeAction(args, callbackContext);
                break;
            case "connectDevice": // 连接选中的蓝牙设备(打印机)
                // android permission auto add
                if (!hasPermisssion()) {
                    requestPermissions(REQUEST_ACCESS_FINE_LOCATION);
                } else {
                    connectDevice(args, callbackContext);
                }
                break;
            case "closeConnect": // 关闭连接
                closePort();
                callbackContext.success("ok");
                break;
            case "printTest": // 打印测试
                printTest(callbackContext);
                break;
            case "printWithCount": // 指定打印次数后打印
                sendPrint(args.optInt(0, 1), callbackContext);
                break;
            case "print": // 打印
                sendPrint(1, callbackContext);
                break;
            case "getPairedDevices": // 获取已配对的蓝牙设备
                getPairedDevices(callbackContext);
                break;
            case "createTsc": // 创建标签打印（打印标签的第一步）
                createTsc(args, callbackContext);
                break;
            case "addGap": // 该指令用于定义两张卷标纸间的垂直间距距离
                tsc = getTsc(1);
                if (tsc != null) {
                    tsc.addGap(args.getInt(0));
                } else {
                    result = false;
                }
                break;
            case "addSize": // 该指令用于设定卷标纸的宽度和长度
                tsc = getTsc(2);
                if (tsc != null) {
                    tsc.addSize(args.getInt(0), args.getInt(1));
                } else {
                    result = false;
                }
                break;
            case "addOffset": // 该指令用于控制在剥离模式时（peel-off
                // mode）每张卷标停止的位置，在打印下一张时打印机会将原先多推出或少推出的部分以回拉方式补偿回来。该指令仅适用于剥离模式。
                tsc = getTsc(1);
                if (tsc != null) {
                    tsc.addOffset(args.getInt(0));
                } else {
                    result = false;
                }
                break;
            case "addSpeed": // 该指令用于控制打印速度, >=1 && <=4
                tsc = getTsc(1);
                if (tsc != null) {
                    tsc.addSpeed(LabelCommand.SPEED.values()[args.getInt(0) - 1]);
                } else {
                    result = false;
                }
                break;
            case "addDensity": // 该指令用于控制打印时的浓度, >=0 && <=15
                tsc = getTsc(1);
                if (tsc != null) {
                    tsc.addDensity(LabelCommand.DENSITY.values()[args.getInt(0)]);
                } else {
                    result = false;
                }
                break;
            case "addDirection": // 该指令用于定义打印时出纸和打印字体的方向, 0(横向)和1(纵向)
                tsc = getTsc(2);
                if (tsc != null) {
                    tsc.addDirection(LabelCommand.DIRECTION.values()[args.getInt(0)],
                            LabelCommand.MIRROR.values()[args.getInt(1)]);
                } else {
                    result = false;
                }
                break;
            case "addReference": // 该指令用于定义卷标的参考坐标原点。坐标原点位置和打印方向有关, x 水平方向的坐标位置,单位dots, y 垂直方向的坐标位置,单位dots
                tsc = getTsc(2);
                if (tsc != null) {
                    tsc.addReference(args.getInt(0), args.getInt(1));
                } else {
                    result = false;
                }
                break;
            case "addShift": // 该指令表示标签打印偏移量多少设置, n 偏移量 ，单位dot 1mm=8 dots
                tsc = getTsc(1);
                if (tsc != null) {
                    tsc.addShif(args.getInt(0));
                } else {
                    result = false;
                }
                break;
            case "addCodepage": // 该指令用于选择对应的国际代码页
                tsc = getTsc(1);
                if (tsc != null) {
                    tsc.addCodePage(LabelCommand.CODEPAGE.valueOf(args.getString(0)));
                } else {
                    result = false;
                }
                break;
            case "addCls": // 该指令用于清除图像缓冲区（image buffer)的数据
                tsc = getTsc(0);
                if (tsc != null) {
                    tsc.addCls();
                } else {
                    result = false;
                }
                break;
            case "addFeed": // 该指令用于将标签纸向前推送指定的长度,点数dots
                tsc = getTsc(1);
                if (tsc != null) {
                    tsc.addFeed(args.getInt(0));
                } else {
                    result = false;
                }
                break;
            case "addBackFeed": // 该指令用于将标签纸向后回拉指定的长度,点数dots
                tsc = getTsc(1);
                if (tsc != null) {
                    tsc.addBackFeed(args.getInt(0));
                } else {
                    result = false;
                }
                break;
            case "addFromfeed": // 该指令用于将标签纸向后回拉指定的长度,点数dots
                tsc = getTsc(0);
                if (tsc != null) {
                    tsc.addFormFeed();
                } else {
                    result = false;
                }
                break;
            case "addHome":
                /**
                 * 在使用含有间隙或黑标的标签纸时，若不能确定第一张标签纸是否在正确打印位 置时，此指令可将标签纸向前推送至下一张标签纸的起点开始打印。标签尺寸和
                 * 间隙需要在本条指令前设置 注：使用该指令时，纸张高度大于或等于30 mm
                 */
                tsc = getTsc(0);
                if (tsc != null) {
                    tsc.addHome();
                } else {
                    result = false;
                }
                break;
            case "addPagePrint": // 该指令用于打印出存储于影像缓冲区内的数据
                tsc = getTsc(0);
                if (tsc != null) {
                    tsc.addPrint(1, 1);
                } else {
                    result = false;
                }
                break;
            case "addPrint":
                /**
                 * 该指令用于打印出存储于影像缓冲区内的数据 传入参数说明 打印份数 1≤n≤65535
                 */
                tsc = getTsc(2);
                if (tsc != null) {
                    tsc.addPrint(args.getInt(0), args.optInt(1, 1));
                } else {
                    result = false;
                }
                break;
            case "addSound":
                /**
                 * 该指令用于控制蜂鸣器的频率，可设定10 阶的声音，每阶声音的长短由第二个参数控制 传入参数说明 level 音阶:0-9 interval
                 * 间隔时间:1-4095
                 */
                tsc = getTsc(2);
                if (tsc != null) {
                    tsc.addSound(args.optInt(0, 1), args.optInt(1, 1));
                } else {
                    result = false;
                }
                break;
            case "addLimitfeed":
                /**
                 * 该指令用于设定打印机进纸时，若经过所设定的长度仍无法侦测到垂直间距，则打印机在连续纸模式工作 传入参数说明 点数dots
                 */
                tsc = getTsc(1);
                if (tsc != null) {
                    tsc.addLimitFeed(args.getInt(0));
                } else {
                    result = false;
                }
                break;
            case "addSelfTest": // 打印自检页
                tsc = getTsc(0);
                if (tsc != null) {
                    tsc.addSelfTest();
                } else {
                    result = false;
                }
                break;
            case "addBar":
                /**
                 * 该指令用于在标签上画线 传入参数说明 x 线条左上角X 坐标，单位dots y 线条左上角Y 坐标，单位dots width 线宽，单位dots
                 * height 线高，单位dots
                 */
                tsc = getTsc(4);
                if (tsc != null) {
                    tsc.addBar(args.getInt(0), args.getInt(1), args.getInt(2), args.getInt(3));
                } else {
                    result = false;
                }
                break;
            case "addBox":
                /**
                 * 该指令用于在标签上画线 传入参数说明 x 线条左上角X 坐标，单位dots y 线条左上角Y 坐标，单位dots width 线宽，单位dots
                 * height 线高，单位dots
                 */
                tsc = getTsc(5);
                if (tsc != null) {
                    tsc.addBox(args.getInt(0), args.getInt(1), args.getInt(2), args.getInt(3), args.getInt(4));
                } else {
                    result = false;
                }
                break;
            case "addBarCode":
                /**
                 * 该指令用来画一维条码 传入参数说明 x 左上角水平坐标起点，以点（dot）表示 y 左上角垂直坐标起点，以点（dot）表示 codetype 条形码类型
                 * height 条形码高度，以点（dot）表示 readable 0 表示人眼不可识，1 表示人眼可识 rotation 条形码旋转角度，顺时针方向
                 * 0,90,180,270 narrow 窄bar 宽度，以点（dots）表示 wide 宽bar 宽度，以点（dot）表示 content 打印内容
                 */
                tsc = getTsc(9);
                if (tsc != null) {
                    tsc.add1DBarcode(args.getInt(0), args.getInt(1),
                            LabelCommand.BARCODETYPE.valueOf(args.getString(2)), args.getInt(3),
                            LabelCommand.READABEL.values()[args.getInt(4)],
                            LabelCommand.ROTATION.valueOf("ROTATION_" + args.getInt(5)),
                            args.getString(6));
                } else {
                    result = false;
                }
                break;
            /*
             * case "addBitmap": // TODO
             *//**
             * 打印图片（单色图片） res为画布参数
             *//*
             * tsc = getTsc(4); if (tsc != null) { tsc.addBitmap(args.getInt(0),
             * args.getInt(1), LabelCommand.BITMAP_MODE.valueOf(args.getString(2)),
             * args.getInt(3)); } else { result = false; } break;
             */
            case "addErase":
                /**
                 * 将指定的区域反相打印 传入参数说明 x_start 反相区域左上角X 坐标，单位dot y_start 反相区域左上角Y 坐标，单位dot x_width
                 * 反相区域宽度，单位dot y_height 反相区域高度，单位dot
                 */
                tsc = getTsc(4);
                if (tsc != null) {
                    tsc.addErase(args.getInt(0), args.getInt(1), args.getInt(2), args.getInt(3));
                } else {
                    result = false;
                }
                break;
            case "addReverse":
                /**
                 * 将指定的区域反相打印 传入参数说明 x_start 反相区域左上角X 坐标，单位dot y_start 反相区域左上角Y 坐标，单位dot x_width
                 * 反相区域宽度，单位dot y_height 反相区域高度，单位dot
                 */
                tsc = getTsc(4);
                if (tsc != null) {
                    tsc.addReverse(args.getInt(0), args.getInt(1), args.getInt(2), args.getInt(3));
                } else {
                    result = false;
                }
                break;
            case "addText":
                /**
                 * 该指令用于打印字符串 传入参数说明 x 文字X 方向起始点坐标 y 文字Y 方向起始点坐标 font 字体名称 1 8×12 dot 英数字体 2
                 * 12×20 dot 英数字体 3 16×24 dot 英数字体 4 24×32 dot 英数字体 5 32×48 dot 英数字体 6 14×19 dot
                 * 英数字体OCR-B 7 21×27 dot 英数字体OCR-B 8 14×25 dot 英数字体OCR-A 9 9×17 dot 英数字体 10
                 * 12×24 dot 英数字体 TSS16.BF2 简体中文16×16（GB 码） TSS20.BF2 简体中文20×20（GB 码） TST24.BF2
                 * 繁体中文24×24（大五码） TSS24.BF2 简体中文24×24（GB 码） K 韩文24×24Font（KS 码） TSS32.BF2
                 * 简体中文32×32（GB 码） rotation 文字旋转角度（顺时针方向） 0， 90， 180， 270
                 */
                tsc = getTsc(7);
                if (tsc != null) {
                    tsc.addText(args.getInt(0), args.getInt(1),
                            LabelCommand.FONTTYPE.valueOf(args.getString(2)),
                            LabelCommand.ROTATION.valueOf("ROTATION_" + args.getInt(3)),
//                            LabelCommand.FONTMUL.MUL_1,
//                            LabelCommand.FONTMUL.MUL_1,
                            LabelCommand.FONTMUL.valueOf("MUL_" + args.getInt(4)),
                            LabelCommand.FONTMUL.valueOf("MUL_" + args.getInt(5)),
                            args.getString(6));
                } else {
                    result = false;
                }
                break;
            case "addQRCode":
                /**
                 * 该指令用来打印二维码 ｘ 二维码水平方向起始点坐标 ｙ 二维码垂直方向起始点坐标 ECC level 选择QRCODE 纠错等级 L 7% M 15% Q
                 * 25% H 30% cell width 二维码宽度1-10 rotation 旋转角度（顺时针方向） 0，90，180，270 content 内容
                 */
                tsc = getTsc(6);
                if (tsc != null) {
                    tsc.addQRCode(args.getInt(0), args.getInt(1),
                            LabelCommand.EEC.valueOf("LEVEL_" + args.getString(2)),
                            args.getInt(3),
                            LabelCommand.ROTATION.valueOf("ROTATION_" + args.getInt(4)),
                            args.getString(5));
                } else {
                    result = false;
                }
                break;
            case "addPeel":
                /**
                 * 该指令用来启动/关闭剥离模式，默认值为关闭 传入参数说明 ON 起动剥离模式 OFF 关闭剥离模式
                 */
                tsc = getTsc(1);
                if (tsc != null) {
                    tsc.addPeel(EscCommand.ENABLE.values()[args.getInt(0)]);
                } else {
                    result = false;
                }
                break;
            case "addTear":
                /**
                 * 此命令是用来启用/禁用撕纸位置走到撕纸处，此设置关掉电源后将保存在打印机内 传入参数说明 ON 启用撕纸位置走到撕纸处 OFF
                 * 禁用撕纸位置走到撕纸处，命令在起始位置有效
                 */
                tsc = getTsc(1);
                if (tsc != null) {
                    tsc.addTear(EscCommand.ENABLE.values()[args.getInt(0)]);
                } else {
                    result = false;
                }
                break;
            case "addReprint":
                /**
                 * 此命令将禁用/启用标签机在无纸或开盖错误发生后，上纸或合盖后重新打印一次标签内容 传入参数说明 OFF 禁止此功能 ON 启用此功能
                 */
                tsc = getTsc(1);
                if (tsc != null) {
                    tsc.addReprint(EscCommand.ENABLE.values()[args.getInt(0)]);
                } else {
                    result = false;
                }
                break;
            case "addCut":
                /**
                 * 此命令用于设置切刀状态，关闭打印机电源后，该设置将会被存储在打印机内存中。 传入参数说明 OFF 关闭切刀功能 BATCH 在PRINT 命令结束后切纸
                 * pieces 0-65535，用于设置每几个标签进行切纸
                 */
                tsc = getTsc(1);
                if (tsc != null) {
                    tsc.addCutter(EscCommand.ENABLE.values()[args.getInt(0)]);
                } else {
                    result = false;
                }
                break;
            case "addCutterPieces":
                /**
                 * 在PRINT 命令结束后切纸 pieces 0-65535，用于设置每几个标签进行切纸
                 */
                tsc = getTsc(1);
                if (tsc != null) {
                    tsc.addCutterPieces((short) args.optInt(0, 1));
                } else {
                    result = false;
                }
                break;
            case "addQueryPrinterStatus": // 开启带Response的打印，用于连续打印
                tsc = getTsc(1);
                if (tsc != null) {
                    tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.valueOf(args.getString(0)));
                } else {
                    result = false;
                }
                break;
            default:
                result = false;

        }

        return result;
    }

    //General Helpers
    private void addProperty(JSONObject obj, String key, Object value) {
        //Believe exception only occurs when adding duplicate keys, so just ignore it
        try {
            if (value == null) {
                obj.put(key, JSONObject.NULL);
            } else {
                obj.put(key, value);
            }
        } catch (JSONException e) {
        }
    }

    private JSONObject getArgsObject(JSONArray args) {
        if (args.length() == 1) {
            try {
                return args.getJSONObject(0);
            } catch (JSONException ex) {
            }
        }

        return null;
    }

    private boolean getStatusReceiver(JSONObject obj) {
        return obj.optBoolean(keyStatusReceiver, true);
    }

    private boolean getRequest(JSONObject obj) {
        return obj.optBoolean(keyRequest, false);
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (initCallbackContext == null) {
                return;
            }

            if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                JSONObject returnObj = new JSONObject();
                PluginResult pluginResult;

                switch (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                    case BluetoothAdapter.STATE_OFF:
                        addProperty(returnObj, keyStatus, statusDisabled);
                        addProperty(returnObj, keyMessage, logNotEnabled);
                        pluginResult = new PluginResult(PluginResult.Status.OK, returnObj);
                        pluginResult.setKeepCallback(true);
                        initCallbackContext.sendPluginResult(pluginResult);
                        break;
                    case BluetoothAdapter.STATE_ON:

                        addProperty(returnObj, keyStatus, statusEnabled);

                        pluginResult = new PluginResult(PluginResult.Status.OK, returnObj);
                        pluginResult.setKeepCallback(true);
                        initCallbackContext.sendPluginResult(pluginResult);
                        break;
                }
            }
        }
    };

    private void initializeAction(JSONArray args, CallbackContext callbackContext) {
        //Save init callback
        initCallbackContext = callbackContext;

        if (bluetoothAdapter != null) {
            JSONObject returnObj = new JSONObject();
            PluginResult pluginResult;

            if (bluetoothAdapter.isEnabled()) {
                addProperty(returnObj, keyStatus, statusEnabled);

                pluginResult = new PluginResult(PluginResult.Status.OK, returnObj);
                pluginResult.setKeepCallback(true);
                initCallbackContext.sendPluginResult(pluginResult);
            } else {
                addProperty(returnObj, keyStatus, statusDisabled);
                addProperty(returnObj, keyMessage, logNotEnabled);

                pluginResult = new PluginResult(PluginResult.Status.OK, returnObj);
                pluginResult.setKeepCallback(true);
                initCallbackContext.sendPluginResult(pluginResult);
            }

            return;
        }


        JSONObject obj = getArgsObject(args);

        if (obj != null && getStatusReceiver(obj)) {
            //Add a receiver to pick up when Bluetooth state changes
            activity.registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        }

        //Get Bluetooth adapter via Bluetooth Manager
        BluetoothManager bluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();


        JSONObject returnObj = new JSONObject();

        //If it's already enabled,
        if (bluetoothAdapter.isEnabled()) {
            addProperty(returnObj, keyStatus, statusEnabled);
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, returnObj);
            pluginResult.setKeepCallback(true);
            initCallbackContext.sendPluginResult(pluginResult);
            return;
        }

        boolean request = false;
        if (obj != null) {
            request = getRequest(obj);
        }

        //Request user to enable Bluetooth
        if (request) {
            //Request Bluetooth to be enabled
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            cordova.startActivityForResult(this, enableBtIntent, REQUEST_BT_ENABLE);
        } else {
            //No request, so send back not enabled
            addProperty(returnObj, keyStatus, statusDisabled);
            addProperty(returnObj, keyMessage, logNotEnabled);
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, returnObj);
            pluginResult.setKeepCallback(true);
            initCallbackContext.sendPluginResult(pluginResult);
        }
    }



    private void getPairedDevices(CallbackContext callbackContext) throws JSONException {

        // Get the local Bluetooth adapter
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices, add each one to the ArrayAdapter
        JSONArray deviceList = new JSONArray();

        for (BluetoothDevice device : pairedDevices) {
            deviceList.put(deviceToJSON(device));
        }
        callbackContext.success(deviceList);

    }

    private JSONObject deviceToJSON(BluetoothDevice device) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name", device.getName());
        json.put("address", device.getAddress());
        json.put("id", device.getAddress());
        if (device.getBluetoothClass() != null) {
            json.put("class", device.getBluetoothClass().getDeviceClass());
        }
        return json;
    }

    private Vector<Byte> getLabel() {
        LabelCommand tsc = new LabelCommand();
        // 设置标签尺寸宽高，按照实际尺寸设置 单位mm
        tsc.addSize(75, 50);
        // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0 单位mm
        // tsc.addGap(0);
        // 设置打印方向
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);
        // 开启带Response的打印，用于连续打印
        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON);
        // 设置原点坐标
        tsc.addReference(0, 0);
        // 设置浓度
        tsc.addDensity(LabelCommand.DENSITY.DNESITY4);
        // 撕纸模式开启
        tsc.addTear(EscCommand.ENABLE.ON);
        // 清除打印缓冲区
        tsc.addCls();
        // 绘制简体中文
        tsc.addText(10, 0, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0,
                LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "欢迎使用Printer");
        // 绘制二维码
        tsc.addQRCode(10, 10, LabelCommand.EEC.LEVEL_L, 5, LabelCommand.ROTATION.ROTATION_0, " www.smarnet.cc");
        // 绘制一维条码
        tsc.add1DBarcode(10, 25, LabelCommand.BARCODETYPE.CODE128, 100, LabelCommand.READABEL.EANBEL,
                LabelCommand.ROTATION.ROTATION_0, "SMARNET");
        // 打印标签
        tsc.addPrint(1, 1);
        // 打印标签后 蜂鸣器响
        tsc.addSound(2, 100);
        // 开启钱箱
        // tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand();
        // 发送数据
        return datas;
    }

    private void sendPrint(int pageCount, CallbackContext callbackContext) {
        if (isConnection) {
            try {
                LabelCommand tsc = getTsc(0);
                if (pageCount > 0) {
                    tsc.addPrint(pageCount, 1);
                }

                Vector<Byte> datas = tsc.getCommand();
                DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(datas);

            } catch (IOException e) {
                callbackContext.error("发送失败！");
                Toast.makeText(activity.getApplicationContext(), "发送失败！", Toast.LENGTH_SHORT).show();
            }
        } else {
            callbackContext.error("设备未连接，请重新连接！");
            Toast.makeText(activity.getApplicationContext(), "设备未连接，请重新连接！", Toast.LENGTH_SHORT).show();
        }
        callbackContext.success();
        Toast.makeText(activity.getApplicationContext(), "打印指令已发送", Toast.LENGTH_SHORT).show();
    }

    private void printTest(final CallbackContext callbackContext) throws JSONException {

        if (isConnection) {
            try {
                DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(getLabel());
                callbackContext.success();
            } catch (IOException e) {
                callbackContext.error("发送失败！");
                Toast.makeText(activity.getApplicationContext(), "发送失败！", Toast.LENGTH_SHORT).show();
            }
        } else {
            callbackContext.error("设备未连接，请重新连接！");
            Toast.makeText(activity.getApplicationContext(), "设备未连接，请重新连接！", Toast.LENGTH_SHORT).show();
        }
    }

    private void closePort() {
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] != null
                && DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].mPort != null) {
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].closePort(id);
            isConnection = false;
        }
    }

    private void connectDevice(JSONArray args, CallbackContext callbackContext) throws JSONException {
        String macAddress = args.getString(0);
        if (!isConnection) {
            try {
                closePort();
                connectCallback = callbackContext;
                new DeviceConnFactoryManager.Build().setId(id).setMacAddress(macAddress).setContext(activity.getApplicationContext()).setCallback(callbackContext)
                        .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.BLUETOOTH).build();
                DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
                isConnection = true;
                PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
                result.setKeepCallback(true);
                callbackContext.sendPluginResult(result);

            } catch (Exception e) {
                isConnection = false;
                Toast.makeText(activity.getApplicationContext(), "连接失败！", 1).show();
                callbackContext.error("连接失败");
            }
            String deviceName = DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentBluetoothDevice().getName();
            Toast.makeText(activity.getApplicationContext(), deviceName + "连接成功！", Toast.LENGTH_SHORT).show();
            if (isConnection) {
                notifyConnectionSuccess(macAddress);
            }

        }

    }

    private void notifyConnectionLost(String error) {
        if (connectCallback != null) {
            connectCallback.error(error);
            connectCallback = null;
        }
    }

    private void notifyConnectionSuccess(String data) {
        if (connectCallback != null) {
            PluginResult result = new PluginResult(PluginResult.Status.OK, data);
            result.setKeepCallback(true);
            connectCallback.sendPluginResult(result);
        }
    }

    private LabelCommand getTsc(int paramsSize) {
        try {
            int labelIndex = 0;
            if (this.requestArgs.length() > paramsSize) {
                labelIndex = this.requestArgs.getInt(this.requestArgs.length() - 1);
            }
            if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].labels.containsKey(labelIndex)) {
                return DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].labels.get(labelIndex);
            }
        } catch (JSONException e) {
        }

        return null;
    }

    /**
     * 新增打印指令 -- 打印第一步
     *
     * @param args
     * @param callbackContext
     */
    private void createTsc(JSONArray args, CallbackContext callbackContext) {
        try {
            int commandIndex = args.getInt(0);
            LabelCommand tsc = new LabelCommand();
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].labels.put(commandIndex, tsc);
            callbackContext.success("ok");
        } catch (JSONException e) {
        }

    }

    /**
     * check application's permissions
     */
    public boolean hasPermisssion() {
        for (String p : permissions) {
            if (!PermissionHelper.hasPermission(this, p)) {
                return false;
            }
        }
        return true;
    }

    /**
     * We override this so that we can access the permissions variable, which no
     * longer exists in the parent class, since we can't initialize it reliably in
     * the constructor!
     *
     * @param requestCode The code to get request action
     */
    public void requestPermissions(int requestCode) {
        PermissionHelper.requestPermissions(this, requestCode, permissions);
    }

    /**
     * processes the result of permission request
     *
     * @param requestCode  The code to get request action
     * @param permissions  The collection of permissions
     * @param grantResults The result of grant
     */
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults)
            throws JSONException {
        PluginResult result;
        for (int r : grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                Log.d(LOG_TAG, "Permission Denied!");
                result = new PluginResult(PluginResult.Status.ILLEGAL_ACCESS_EXCEPTION);
                this.mcallbackContext.sendPluginResult(result);
                return;
            }
        }

        switch (requestCode) {
            case REQUEST_ACCESS_FINE_LOCATION:
                connectDevice(this.requestArgs, this.mcallbackContext);
                break;
        }
    }

    /**
     * This plugin launches an external Activity when the camera is opened, so we
     * need to implement the save/restore API in case the Activity gets killed by
     * the OS while it's in the background.
     */
    public void onRestoreStateForActivityResult(Bundle state, CallbackContext callbackContext) {
        this.mcallbackContext = callbackContext;
    }

    // private void setPrintData(String oneModel,String drawingRev,String
    // oneClass,String oneCode,String chipId,String dateTime,String specification){

    // TscCommand tsc = new TscCommand();
    // tsc.addSize(70, 15); //设置标签尺寸，按照实际尺寸设置
    // tsc.addGap(2); //设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
    // tsc.addDirection(DIRECTION.BACKWARD,MIRROR.NORMAL);//设置打印方向
    // tsc.addReference(0, 0);//设置原点坐标
    // tsc.addTear(ENABLE.ON); //撕纸模式开启
    // tsc.addCls();// 清除打印缓冲区

    // //绘制简体中文
    // tsc.addText(10,30,FONTTYPE.SIMPLIFIED_CHINESE,ROTATION.ROTATION_0,FONTMUL.MUL_1,FONTMUL.MUL_1,oneModel);
    // tsc.addText(95,30,FONTTYPE.SIMPLIFIED_CHINESE,ROTATION.ROTATION_0,FONTMUL.MUL_1,FONTMUL.MUL_1,drawingRev);
    // tsc.addText(10,80,FONTTYPE.SIMPLIFIED_CHINESE,ROTATION.ROTATION_0,FONTMUL.MUL_1,FONTMUL.MUL_1,oneClass);
    // tsc.addText(35,80,FONTTYPE.SIMPLIFIED_CHINESE,ROTATION.ROTATION_0,FONTMUL.MUL_1,FONTMUL.MUL_1,dateTime);
    // tsc.addText(150,40,FONTTYPE.SIMPLIFIED_CHINESE,ROTATION.ROTATION_0,FONTMUL.MUL_2,FONTMUL.MUL_2,oneCode);
    // tsc.addText(200,40,FONTTYPE.SIMPLIFIED_CHINESE,ROTATION.ROTATION_0,FONTMUL.MUL_2,FONTMUL.MUL_2,specification);
    // //绘制二维码
    // tsc.addQRCode(250, 10, EEC.LEVEL_L,5,ROTATION.ROTATION_0, chipId);
    // tsc.addText(370,30,FONTTYPE.SIMPLIFIED_CHINESE,ROTATION.ROTATION_0,FONTMUL.MUL_1,FONTMUL.MUL_1,chipId);
    // tsc.addText(370,80,FONTTYPE.SIMPLIFIED_CHINESE,ROTATION.ROTATION_0,FONTMUL.MUL_1,FONTMUL.MUL_1,chipId);

    // tsc.addPrint(1,1); // 打印标签
    // tsc.addSound(2, 100); //打印标签后 蜂鸣器响
    // Vector<Byte> datas = tsc.getCommand(); //发送数据
    // Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
    // byte[] bytes = ArrayUtils.toPrimitive(Bytes);
    // String str = Base64.encodeToString(bytes, Base64.DEFAULT);
    // int rel;
    // try {
    // rel = mGpService.sendTscCommand(0, str);
    // // GpCom.ERROR_CODE r=GpCom.ERROR_CODE.values()[rel];
    // // if(r != GpCom.ERROR_CODE.SUCCESS){
    // // Toast.makeText(getApplicationContext(),GpCom.getErrorText(r),
    // Toast.LENGTH_SHORT).show();
    // // }
    // }
    // catch (RemoteException e) { // TODO Auto-generated catch block
    // e.printStackTrace();
    // }

    // }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}