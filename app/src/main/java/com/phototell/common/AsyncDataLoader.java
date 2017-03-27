package com.phototell.common;

/**
 * Created by user on 23/03/2017.
 */

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

/*
base class for AsyncTasks data loaders.
D: data type.
*/
public abstract class AsyncDataLoader<D> implements DataLoader<D> {

    private OnDataLoadedListener listener;

    protected abstract List<D> fetchDataInBackground(DataLoadTask task);

    @Override
    public void loadData(@NonNull OnDataLoadedListener listener) {
        this.listener = listener;
        DataLoadTask task = new DataLoadTask();
        task.execute();
    }

    protected void onDataLoaded(@Nullable List<D> data) {
        if (listener != null && data != null) {
            listener.onSuccess(data);
        }
    }

    protected void onFailure(String errorMessage) {
        if (listener != null) {
            listener.onFailure(errorMessage);
        }
    }

    public class DataLoadTask extends AsyncTask<Void, Void, List<D>> {

        @Override
        protected List<D> doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            return fetchDataInBackground(this);
        }

        @Override
        protected void onPostExecute(@Nullable List<D> result) {
            if (isCancelled()) {
                return;
            }
            result = result == null ? Collections.<D>emptyList() : result;
            onDataLoaded(result);
        }
    }
}
