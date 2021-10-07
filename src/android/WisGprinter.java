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
// import android.content.ServiceConnection;
// import android.content.ComponentName;
// import android.content.Context;
// import android.content.Intent;
// import android.os.Bundle;
// import android.os.IBinder;
import android.content.*;
// import android.content.ContextWrapper;
import android.os.*;
// import android.os.RemoteException;
import android.app.Activity;
import android.view.*;
import android.Manifest;
import android.content.pm.PackageManager;
// import android.view.ContextThemeWrapper;


public class WisGprinter extends CordovaPlugin {

    private final int REQUEST_ACCESS_FINE_LOCATION = 59628;
    private static final String LOG_TAG = "WisGprinter";

    private int id = 0;
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
    private CallbackContext mcallbackContext;
    private JSONArray requestArgs;
    private ThreadPool threadPool;
    private BluetoothPort mPort;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothAdapter mAdapter;
    // private PrinterServiceConnection conn = null;
    // private GpService mGpService = null;
    private Activity activity;
    private String boothAddress = "";
    private String oneModel, drawingRev, oneClass, oneCode, chipId, dateTime, specification = "";

    private boolean isConnection = false;//蓝牙是否连接
    private BluetoothDevice device = null;
    private static BluetoothSocket bluetoothSocket = null;
    private OutputStream outputStream = null;
    private InputStream inputStream = null;
    private int state = 0;
    private static final UUID uuid = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        activity = cordova.getActivity();

    }


    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.mcallbackContext = callbackContext;
        this.requestArgs = args;

        //获取已配对的蓝牙设备
        if (action.equals("getPairedDevices")) {
            getPairedDevices();
            return true;
        }
        //连接选中的蓝牙设备(打印机)
        if (action.equals("connectDevice")) {
            //android permission auto add
            if (!hasPermisssion()) {
                requestPermissions(REQUEST_ACCESS_FINE_LOCATION);
            } else {
                connectDevice(args, callbackContext);

                return true;
            }
        }
        //打印测试
        if (action.equals("printTest")) {
            printTest(callbackContext);
            return true;
        }

        return false;
    }

    /**
     * 重新连接回收上次连接的对象，避免内存泄漏
     */
    private void closeport() {
        if (mPort != null) {
            mPort.closePort();
            mPort = null;
        }
    }

    private void initSocketStream() throws IOException {
        this.inputStream = this.bluetoothSocket.getInputStream();
        this.outputStream = this.bluetoothSocket.getOutputStream();
    }

    public boolean openPort(String macAddress) {
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mAdapter.cancelDiscovery();
        if (this.mAdapter == null) {
            this.state = 0;
            Log.e(LOG_TAG, "Bluetooth is not support");
        } else if (!this.mAdapter.isEnabled()) {
            this.state = 0;
            Log.e(LOG_TAG, "Bluetooth is not open");
        } else {
            try {
                if (BluetoothAdapter.checkBluetoothAddress(macAddress)) {
                    this.device = this.mAdapter.getRemoteDevice(macAddress);
                    this.bluetoothSocket = this.device.createInsecureRfcommSocketToServiceRecord(uuid);
                    this.bluetoothSocket.connect();
                    this.initSocketStream();
                    this.state = 3;
                    return true;
                }

                this.state = 0;
                Log.e(LOG_TAG, "Bluetooth address is invalid");
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        }

        return false;
    }

    private void connectDevice(final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        final String macAddress = args.getString(0);
        if (!isConnection) {
            try {
                closeport();
                //打开端口
                openPort(macAddress);

                isConnection = true;

                PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
                result.setKeepCallback(true);
                callbackContext.sendPluginResult(result);

            } catch (Exception e) {
                isConnection = false;
                Toast.makeText(activity.getApplicationContext(), "连接失败！", 1).show();

            }
            Toast.makeText(activity.getApplicationContext(), device.getName() + "连接成功！",
                    Toast.LENGTH_SHORT).show();

        }


    }

    private void getPairedDevices() {

        // Get the local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            String str = "";
            for (BluetoothDevice device : pairedDevices) {

                str += device.getName() + "&" + device.getAddress() + ",";


            }
            String jsEvent = String.format(
                    "cordova.fireDocumentEvent('bluetoothprint.DataReceived',{'bluetoothPrintData':'%s'})",
                    str);
            webView.sendJavascript(jsEvent);
        }

    }

    protected byte[] convertVectorByteToBytes(Vector<Byte> data) {
        byte[] sendData = new byte[data.size()];
        if (data.size() > 0) {
            for (int i = 0; i < data.size(); ++i) {
                sendData[i] = (Byte) data.get(i);
            }
        }

        return sendData;
    }

    public Vector<Byte> getLabel() {
        LabelCommand tsc = new LabelCommand();
        // 设置标签尺寸宽高，按照实际尺寸设置 单位mm
        tsc.addSize(75, 50);
        // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0 单位mm
//        tsc.addGap(0);
        // 设置打印方向
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);
        // 开启带Response的打印，用于连续打印
        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON);
        // 设置原点坐标
        tsc.addReference(0, 0);
        //设置浓度
        tsc.addDensity(LabelCommand.DENSITY.DNESITY4);
        // 撕纸模式开启
        tsc.addTear(EscCommand.ENABLE.ON);
        // 清除打印缓冲区
        tsc.addCls();
        // 绘制简体中文
        tsc.addText(10, 0, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "欢迎使用Printer");
        //绘制二维码
        tsc.addQRCode(10, 10, LabelCommand.EEC.LEVEL_L, 5, LabelCommand.ROTATION.ROTATION_0, " www.smarnet.cc");
        // 绘制一维条码
        tsc.add1DBarcode(10, 25, LabelCommand.BARCODETYPE.CODE128, 100, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, "SMARNET");
        // 打印标签
        tsc.addPrint(1, 1);
        // 打印标签后 蜂鸣器响
        tsc.addSound(2, 100);
        //开启钱箱
//        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand();
        // 发送数据
        return datas;
    }

    private void printTest(final CallbackContext callbackContext) throws JSONException {

        if (isConnection) {
            System.out.println("开始打印！！");
            try {

                Vector<Byte> data = getLabel();
                this.outputStream.write(this.convertVectorByteToBytes(data), 0, data.size());
                this.outputStream.flush();
                callbackContext.success();
            } catch (IOException e) {
                callbackContext.error("发送失败！");
                Toast.makeText(activity.getApplicationContext(), "发送失败！", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            callbackContext.error("设备未连接，请重新连接！");
            Toast.makeText(activity.getApplicationContext(), "设备未连接，请重新连接！", Toast.LENGTH_SHORT)
                    .show();

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
     * We override this so that we can access the permissions variable, which no longer exists in
     * the parent class, since we can't initialize it reliably in the constructor!
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
    public void onRequestPermissionResult(int requestCode, String[] permissions,
                                          int[] grantResults) throws JSONException {
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
     * need to implement the save/restore API in case the Activity gets killed
     * by the OS while it's in the background.
     */
    public void onRestoreStateForActivityResult(Bundle state, CallbackContext callbackContext) {
        this.mcallbackContext = callbackContext;
    }

    // private void setPrintData(String oneModel,String drawingRev,String oneClass,String oneCode,String chipId,String dateTime,String specification){

    //     TscCommand tsc = new TscCommand();
    //     tsc.addSize(70, 15); //设置标签尺寸，按照实际尺寸设置
    //     tsc.addGap(2); //设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
    //     tsc.addDirection(DIRECTION.BACKWARD,MIRROR.NORMAL);//设置打印方向
    //     tsc.addReference(0, 0);//设置原点坐标
    //     tsc.addTear(ENABLE.ON); //撕纸模式开启
    //     tsc.addCls();// 清除打印缓冲区

    //     //绘制简体中文
    //     tsc.addText(10,30,FONTTYPE.SIMPLIFIED_CHINESE,ROTATION.ROTATION_0,FONTMUL.MUL_1,FONTMUL.MUL_1,oneModel);
    //     tsc.addText(95,30,FONTTYPE.SIMPLIFIED_CHINESE,ROTATION.ROTATION_0,FONTMUL.MUL_1,FONTMUL.MUL_1,drawingRev);
    //     tsc.addText(10,80,FONTTYPE.SIMPLIFIED_CHINESE,ROTATION.ROTATION_0,FONTMUL.MUL_1,FONTMUL.MUL_1,oneClass);
    //     tsc.addText(35,80,FONTTYPE.SIMPLIFIED_CHINESE,ROTATION.ROTATION_0,FONTMUL.MUL_1,FONTMUL.MUL_1,dateTime);
    //     tsc.addText(150,40,FONTTYPE.SIMPLIFIED_CHINESE,ROTATION.ROTATION_0,FONTMUL.MUL_2,FONTMUL.MUL_2,oneCode);
    //     tsc.addText(200,40,FONTTYPE.SIMPLIFIED_CHINESE,ROTATION.ROTATION_0,FONTMUL.MUL_2,FONTMUL.MUL_2,specification);
    //     //绘制二维码
    //     tsc.addQRCode(250, 10, EEC.LEVEL_L,5,ROTATION.ROTATION_0, chipId);
    //     tsc.addText(370,30,FONTTYPE.SIMPLIFIED_CHINESE,ROTATION.ROTATION_0,FONTMUL.MUL_1,FONTMUL.MUL_1,chipId);
    //     tsc.addText(370,80,FONTTYPE.SIMPLIFIED_CHINESE,ROTATION.ROTATION_0,FONTMUL.MUL_1,FONTMUL.MUL_1,chipId);

    //     tsc.addPrint(1,1); // 打印标签
    //     tsc.addSound(2, 100); //打印标签后 蜂鸣器响
    //     Vector<Byte> datas = tsc.getCommand(); //发送数据
    //     Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
    //     byte[] bytes = ArrayUtils.toPrimitive(Bytes);
    //     String str = Base64.encodeToString(bytes, Base64.DEFAULT);
    //     int rel;
    //     try {
    //         rel = mGpService.sendTscCommand(0, str);
    //         // GpCom.ERROR_CODE r=GpCom.ERROR_CODE.values()[rel];
    //         // if(r != GpCom.ERROR_CODE.SUCCESS){
    //         //    Toast.makeText(getApplicationContext(),GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
    //         // }
    //     }
    //     catch (RemoteException e) { // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     }


    // }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}