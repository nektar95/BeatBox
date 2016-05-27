package com.bignerdranch.android.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olo35 on 16.03.2016.
 */
public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;

    private AssetManager mAssets;
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;

    public BeatBox(Context context)
    {
        mAssets = context.getAssets();
        /*
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP)
        {
            AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            mSoundPool =  new SoundPool.Builder().setAudioAttributes(attributes).build();
        }*/
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        loadSounds();
    }

    private void load(Sound sound) throws IOException
    {
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        int SoundId  = mSoundPool.load(afd,1);
        sound.setSoundId(SoundId);
    }

    private void loadSounds()
    {
        String soundNames[];
        try{
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found" + soundNames.length + " sounds");
        }catch (IOException e)
        {
            Log.e(TAG,"Could not list assets",e);
            return;
        }

        for (String filename : soundNames) {
            try {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            }catch (IOException e)
        {
            Log.e(TAG, "Could not load sound " + filename, e);
        }
        }

    }

    public void play(Sound sound)
    {
        Integer soundId = sound.getSoundId();
        if(soundId == null)
        {
            return;
        }
        mSoundPool.play(soundId,1.0f,1.0f,1,0,1.0f);
    }

    public void release()
    {
        mSoundPool.release();
    }

    public List<Sound> getSounds()
    {
        return mSounds;
    }
}
