package com.linksprite.io.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.linksprite.io.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;

public class CaptureActivity extends SetupActivity {

    @InjectView(R.id.scanner_view)
    ScannerLiveView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        ButterKnife.inject(this);

        scannerView.setSameCodeRescanProtectionTime(20000);
        scannerView.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {

            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {

            }

            @Override
            public void onScannerError(Throwable err) {
                showErrMessage(err.getMessage());
            }

            @Override
            public void onCodeScanned(String data) {
                Log.e("Tag", "" + data);
                handleDecodeInternally(data);
            }
        });
    }

    public void errDialogClick() {

    }

    private boolean handleDecodeInternally(String data) {
        Log.d("TAG", "SCANNED:" + data);
        if (data.contains(",")) {
            String[] splitContents = data.split(",");

            if (splitContents[0].length() >= 4 && splitContents[1].length() >= 4) {
                String id = splitContents[0].substring(3);
                String key = splitContents[1].substring(3);
                if (TextUtils.isEmpty(id) || TextUtils.isEmpty(key)) {
                    return false;
                }
                Intent intent = CaptureActivity.this.getIntent();
                intent.setClass(CaptureActivity.this, AddDeviceActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("key", key);
                nextActivity(intent);
                return true;
            }
        }

        Log.d("TAG", "invalid barcode scanned:" + data);
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        ZXDecoder decoder = new ZXDecoder();
        decoder.setScanAreaPercent(0.5);
        scannerView.setDecoder(decoder);
        scannerView.setPlaySound(false);
        scannerView.startScanner();
    }

    @OnClick(R.id.scan_back_button)
    public void onClick() {
        finish();
    }

    @Override
    protected void onPause() {
        scannerView.stopScanner();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        scannerView.getCamera().getController().close();
    }
}
