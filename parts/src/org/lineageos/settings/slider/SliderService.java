/*
 * Copyright (C) 2018 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings.slidersettings;

import android.app.ActivityManager;
import android.app.Instrumentation;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.os.PowerManager;
import android.os.UserHandle;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.KeyEvent;
import android.os.Handler;

import org.lineageos.internal.util.FileUtils;

import com.android.internal.os.DeviceKeyHandler;

import java.util.Timer;
import java.util.TimerTask;

import static android.view.KeyEvent.ACTION_DOWN;

public class KeyHandler extends CameraManager.AvailabilityCallback
        implements DeviceKeyHandler {
    private static final int KEYCODE_SLIDER_UP = 594;
    private static final int KEYCODE_SLIDER_DOWN = 595;

    private final Context mContext;
    private final Vibrator mVibrator;
    private static Handler mHandler = new Handler();
    private final CameraManager mCameraManager;
    private final PowerManager mPowerManager;
    private SliderPreferences mSliderPreferences;
    private String[] mSoundNames = {"popup_muqin_up.ogg", "popup_muqin_down.ogg", "popup_yingyan_up.ogg", "popup_yingyan_down.ogg", "popup_mofa_up.ogg", "popup_mofa_down.ogg", "popup_jijia_up.ogg", "popup_jijia_down.ogg", "popup_chilun_up.ogg", "popup_chilun_down.ogg", "popup_cangmen_up.ogg", "popup_cangmen_down.ogg"};
    private SoundPool mSoundPool;
    private int [] mSounds = new int [mSoundNames.length];
    private boolean mIsCameraAppOpen = false;
    private boolean mIsDefaultCameraAppOpen = false;
    private Timer mCameraInUseTimer;

    public KeyHandler(Context context) {
        mContext = context;

        mVibrator = mContext.getSystemService(Vibrator.class);
        mCameraManager = mContext.getSystemService(CameraManager.class);
        mPowerManager = mContext.getSystemService(PowerManager.class);

        if (mCameraManager != null) {
            mCameraManager.registerAvailabilityCallback(this, null /* handler */);
        }
    }

    @Override
    public void onCameraAvailable(String cameraId) {
        super.onCameraAvailable(cameraId);
        mSliderPreferences = new SliderPreferences(this);
        mSoundPool = new SoundPool.Builder().setMaxStreams(1)
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                        .build()).build();
        int i = 0;
        for (String soundName : mSoundNames) {
            mSounds[i] = mSoundPool.load("/system/media/audio/ui/" + soundName, 1);
            i++;
        }

        mCameraInUseTimer = new Timer();
        mCameraInUseTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mIsCameraAppOpen = false;
                mIsDefaultCameraAppOpen = false;
                mCameraInUseTimer.cancel();
            }
        }, 1000);
    }

    @Override
    public void onCameraUnavailable(String cameraId) {
        super.onCameraUnavailable(cameraId);
        mIsCameraAppOpen = true;

        try {
            mCameraInUseTimer.cancel();
        } catch (Exception e) {}
    }

    public KeyEvent handleKeyEvent(KeyEvent event) {
        if (!mPowerManager.isInteractive()) {
            return event;
        }

        int action = event.getAction();
        if (action != ACTION_DOWN) {
            return event;
        }

        int scanCode = event.getScanCode();
        switch (scanCode) {
            case KEYCODE_SLIDER_UP:
                handleSliderUp();
                break;
            case KEYCODE_SLIDER_DOWN:
                handleSliderDown();
                break;
            default:
                return event;
        }

        doHapticFeedback();

        return null;
    }

    boolean isUserSetupComplete() {
        return Settings.Secure.getIntForUser(mContext.getContentResolver(),
                Settings.Secure.USER_SETUP_COMPLETE, 0, UserHandle.USER_CURRENT) != 0;
    }

    private void startActivityAsUser(Intent intent, UserHandle handle) {
        if (isUserSetupComplete()) {
            mContext.startActivityAsUser(intent, handle);
        }
    }

    private void openDefaultCameraApp() {
        KeyguardManager keyguardManager = mContext.getSystemService(KeyguardManager.class);
        if (keyguardManager == null) {
            return;
        }

        Intent intent;

        if (keyguardManager.isDeviceLocked()) {
            intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE);
        } else {
            intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        }

        intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivityAsUser(intent, UserHandle.CURRENT_OR_SELF);

        mIsDefaultCameraAppOpen = true;
    }

    private void doHapticFeedback() {
        if (mVibrator == null || !mVibrator.hasVibrator()) {
            return;
        }

        mVibrator.vibrate(40);
    }

    private void handleSliderDown() {
        if (mIsCameraAppOpen && mIsDefaultCameraAppOpen) {
            return;
        }

        openDefaultCameraApp();
    }

    private void handleSliderUp() {
        if (!mIsDefaultCameraAppOpen) {
            return;
        }

    private void handleSliderSound() {
        if(!mIsDefaultCameraAppOpen){
            playSoundEffect(!mIsDefaultCameraAppOpen)
        }
        else if (mIsCameraAppOpen && mIsDefaultCameraAppOpen){
            playSoundEffect(mIsCameraAppOpen && mIsDefaultCameraAppOpen)
        }
    }
    
    private void SliderEnabled() {
    if (mSliderPreferences.isSliderAllowed()){
        FileUtils.writeLine(KEYCODE_SLIDER_UP, "594");
        FileUtils.writeLine(KEYCODE_SLIDER_DOWN, "595");
        
        mHandler.postDelayed(new Runnable {
            @Override
            public void run() {
                    FileUtils.writeLine(KEYCODE_SLIDER_UP, "0");
                    FileUtils.writeLine(KEYCODE_SLIDER_DOWN, "0");           
            }
        }, 1200);
    }
    }

    private void playSoundEffect(String state) {
        int soundEffect = Integer.parseInt(mSliderPreferences.getSoundEffect());
        if (soundEffect != -1) {
            if (state.equals(mIsDefaultCameraAppOpen)) {
                soundEffect++;
            }
            mSoundPool.play(mSounds[soundEffect], 1.0f, 1.0f, 0, 0, 1.0f);
        }
        
        Instrumentation m_Instrumentation = new Instrumentation();
        m_Instrumentation.sendKeyDownUpSync( KeyEvent.KEYCODE_BACK );
    }
}
