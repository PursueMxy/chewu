package com.weimu.chewu.module.order_detail_arrival;

import com.weimu.chewu.backend.http.core.CombineDisposable;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.remote.AddArrivalPicCase;

import io.reactivex.disposables.Disposable;

/**
 * Created by huangjinfu on 18/9/2.
 */

public class AddArrivalPicPresenterImpl implements AddArrivalPicContract.Presenter {
    private CombineDisposable combineDisposable = new CombineDisposable();

    private AddArrivalPicContract.View mView;
    private AddArrivalPicCase mCase;

    public AddArrivalPicPresenterImpl(AddArrivalPicContract.View mView) {
        mCase = new AddArrivalPicCaseImpl();
        this.mView = mView;
    }

    @Override
    public void upLoadPic(int order_id, String car_images) {
        boolean disposable = combineDisposable.isDisposable("upLoadPic");
        if (!disposable) return;
        mCase.uploadPic(order_id, car_images)
                .subscribe(new OnRequestObserver<String>() {

                    @Override
                    protected void onStart(Disposable d) {
                        combineDisposable.addDisposable("upLoadPic", d);
                    }

                    @Override
                    protected boolean OnSucceed(String result) {
                        mView.onUploadPicSuccess();
                        return super.OnSucceed(result);
                    }

                    @Override
                    protected boolean onFailure(String message) {
//                        mView.onUploadPicFail(message);
                        return super.onFailure(message);
                    }
                });
    }

    @Override
    public void destroy() {
        combineDisposable.dispose();
    }
}
