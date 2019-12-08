package com.weimu.chewu.module.wallet.bank.add;

import com.weimu.chewu.backend.http.core.CombineDisposable;
import com.weimu.chewu.backend.http.observer.OnRequestObserver;
import com.weimu.chewu.backend.remote.AddCardCase;

/**
 * Created by huangjinfu on 18/5/1.
 */

public class AddCardPresenterImpl implements AddCardContract.Presenter {
    private CombineDisposable combineDisposable = new CombineDisposable();

    private AddCardContract.View mView;
    private AddCardCase mCase;

    public AddCardPresenterImpl(AddCardContract.View mView) {
        this.mView = mView;
        mCase = new AddCardCaseImpl();
    }

    @Override
    public void destroy() {
        combineDisposable.dispose();
    }

    @Override
    public void addBankCard(String cardholder, String card_no, String bank) {
        mCase.addBankCard(cardholder, card_no, bank)
                .subscribe(new OnRequestObserver<String>() {
                    @Override
                    protected boolean onFailure(String message) {
                        mView.addFailed(message);
                        return super.onFailure(message);
                    }

                    @Override
                    protected boolean OnSucceed(String result) {
                        mView.addSuccess();
                        return super.OnSucceed(result);
                    }
                });
    }

    @Override
    public void updateBankCard(String id, String cardholder, String card_no, String bank) {
        mCase.updateBankCard(id, cardholder, card_no, bank)
                .subscribe(new OnRequestObserver<String>() {
                    @Override
                    protected boolean OnSucceed(String result) {
                        mView.addSuccess();
                        return super.OnSucceed(result);
                    }

                    @Override
                    protected boolean onFailure(String message) {
                        mView.addFailed(message);

                        return super.onFailure(message);
                    }
                });
    }
}
