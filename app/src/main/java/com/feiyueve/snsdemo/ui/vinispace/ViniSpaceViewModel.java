package com.feiyueve.snsdemo.ui.vinispace;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViniSpaceViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ViniSpaceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is vinispace fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}